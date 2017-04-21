package de.sitescrawler.dokumentation;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.resource.spi.ConfigProperty;

import de.sitescrawler.model.ProjectConfig;

import java.io.Serializable;

@SessionScoped
@Named("index")
public class IndexBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProjectConfig config; 

	public ProjectConfig getConfig() {
		return config;
	}
}
