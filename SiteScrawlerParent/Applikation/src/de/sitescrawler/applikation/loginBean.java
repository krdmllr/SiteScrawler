package de.sitescrawler.applikation;

import java.io.IOException;
import java.io.Serializable;

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

import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.qualifier.Produktiv;

/**
 * 
 * @author robin loginBean, alle Methoden zum Login/Logout
 */
@SessionScoped
@Named("login")
public class loginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String uid;
	private String password;
	private String originalURL;
	private FacesContext context = FacesContext.getCurrentInstance();
	private boolean registriert = true;

	@Inject
	private ProjectConfig config;

	@Inject
	@Produktiv
	private INutzerDatenService nutzerDatenService;

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

}
