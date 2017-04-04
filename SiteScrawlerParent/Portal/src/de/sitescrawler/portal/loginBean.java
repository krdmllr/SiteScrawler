package de.sitescrawler.portal;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@SessionScoped
@Named("login")
public class loginBean implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String            uid;
    private String            password;
    private String            originalURL;

    @PostConstruct
    public void init()
    {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        this.originalURL = (String) requestMap.get(RequestDispatcher.FORWARD_REQUEST_URI);
        if (this.originalURL == null)
        {
            String requestContextPath = externalContext.getRequestContextPath();
            // nur lokale url zum testen
            this.originalURL = requestContextPath + "http://app.localhost:8080";
            // produktiv url
            // this.originalURL = requestContextPath + "http://app.sitescrawler.de";
        }
        else
        {
            String originalQuery = (String) requestMap.get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null)
            {
                this.originalURL += "?" + originalQuery;
            }
        }
    }

    public void login()
    {
        System.out.println("Login:  " + this.uid + " | " + this.password);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try
        {
            request.login(this.uid, this.password);
            externalContext.redirect(this.originalURL);
        }
        catch (ServletException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public void logout()
    {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
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

    public String getUid()
    {
        return this.uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
