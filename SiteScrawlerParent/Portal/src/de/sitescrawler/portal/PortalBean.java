package de.sitescrawler.portal;

import java.io.IOException;
import java.io.Serializable;
 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named; 
 
import de.sitescrawler.model.ProjectConfig; 

@SessionScoped
@Named("portal")
public class PortalBean implements Serializable
{

    private static final long serialVersionUID = 1L;
     
    @Inject
    private ProjectConfig   	config;

	public ProjectConfig getConfig() {
		return config;
	} 
}
