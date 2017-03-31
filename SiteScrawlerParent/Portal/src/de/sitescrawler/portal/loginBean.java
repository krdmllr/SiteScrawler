package de.sitescrawler.portal;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@SessionScoped
@Named("login") 
public class loginBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private String                   uid;
    private String                   password;
    private String                   originalURL;
    private FacesContext context = FacesContext.getCurrentInstance();
    
    @PostConstruct
    public void init()
    {
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
    }
    
    public void login()
    {
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
}
