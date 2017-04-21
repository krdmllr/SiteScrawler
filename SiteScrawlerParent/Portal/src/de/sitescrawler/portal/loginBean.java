package de.sitescrawler.portal;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.resource.spi.ConfigProperty;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import de.sitescrawler.model.ProjectConfig;

import java.io.Serializable;

@SessionScoped
@Named("login") 
public class loginBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private String                   uid; 
	private String                   password;  
	private String                   originalURL;
    private FacesContext context = FacesContext.getCurrentInstance();
    
    @Inject
    private ProjectConfig config;
    
    @PostConstruct
    public void init()
    { 
    	return;
    	
    	/**
        ExternalContext externalContext = this.context.getExternalContext();
        this.originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        if (this.originalURL == null)
        {
            this.originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
        }
        else
        {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null)
            {
                this.originalURL += "?" + originalQuery;
            }
        }
        **/
    }
    
    public void login()
    {
    	return;
    	/**
    	System.out.println("Login:  " + uid + " | " + password);
        HttpServletRequest request = (HttpServletRequest) this.context.getExternalContext().getRequest();
        try
        {
            request.login(this.uid, this.password);
            this.context.getExternalContext().redirect(this.originalURL);
        }
        catch (ServletException | IOException e)
        {
        	e.printStackTrace();
        }
        **/
    }

    public void logout()
    {
        ExternalContext externalContext = this.context.getExternalContext();
        externalContext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try
        {
            request.logout();
            externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
        }
        catch (ServletException | IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ProjectConfig getConfig() {
		return config;
	} 
}
