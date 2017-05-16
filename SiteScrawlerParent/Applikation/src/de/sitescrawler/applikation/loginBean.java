package de.sitescrawler.applikation;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import de.sitescrawler.qualifier.Produktiv;

/**
 * 
 * @author robin loginBean, alle Methoden zum Login/Logout
 */
@SessionScoped
@Named("login")
public class loginBean implements Serializable {

	private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");
	
	private static final long serialVersionUID = 1L;
	private String uid;
	private String password;
	private String originalURL;
	private FacesContext context = FacesContext.getCurrentInstance();
	private boolean registriert = true;
	private String emailPasswortVergessen;

	@Inject
	private ProjectConfig config;

	@Inject
	private INutzerDatenService nutzerDatenService;
	
	@Inject
	private INutzerService nutzerService;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = this.context.getExternalContext();
		this.originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
		if (this.originalURL == null) {
			this.originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
		} else {
			String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				this.originalURL += "?" + originalQuery;
			}
		}

	}
	
	public void passwortZuruecksetzen(){
		
		if(nutzerService.isEmailVerfuegbar(emailPasswortVergessen))
		{
			//Email ist nicht vergeben!
			LOGGER.info("Nutzer hat versucht, Konto mit nicht existenter E-Mail Adresse " + emailPasswortVergessen + " zurückzusetzen.");
		}
		else
		{
			LOGGER.info("Setze Passwort von Account mit E-Mail Adresse " + emailPasswortVergessen + " zurück.");
			Nutzer nutzer = nutzerService.getNutzer(emailPasswortVergessen);
			try {
				nutzerService.passwortZuruecksetzen(nutzer);
			} catch (ServiceUnavailableException e) {
				LOGGER.log(Level.SEVERE,"Passwort zurücksetzen fehlgeschlagen", e);
			}
		}
		
		emailPasswortVergessen = "";
	}

	public void login() {
		System.out.println("Login: " + this.uid + " | " + this.password);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try {
			request.login(this.uid, this.password);
			externalContext.redirect(this.originalURL);
			this.nutzerDatenService.setNutzer(this.uid);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}

	}

	public void logout() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try {
			request.logout();
			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (ServletException | IOException e) {
			if(e.getMessage().toLowerCase().contains("login failed")){
				//Login fehlgeschlagen, unterrichte Nutzer.
				
		        FacesContext context = FacesContext.getCurrentInstance(); 
		        context.addMessage(null, new FacesMessage("Anmeldung fehlgeschlagen", "Passwort oder E-Mail Adresse falsch."));
			}
			e.printStackTrace();
		}
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ProjectConfig getConfig() {
		return this.config;
	}

	public boolean isRegistriert() {
		return this.registriert;
	}

	public void setRegistriert(boolean registriert) {
		this.registriert = registriert;
	}

	public String getEmailPasswortVergessen() {
		return emailPasswortVergessen;
	}

	public void setEmailPasswortVergessen(String emailPasswortVergessen) {
		this.emailPasswortVergessen = emailPasswortVergessen;
	}

}
