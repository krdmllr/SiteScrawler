package de.sitescrawler.dokumentation;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named("index")
public class IndexBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProjectConfig config = new ProjectConfig();

	public IndexBean(){
		config.loadConfig();
	}

	public ProjectConfig getConfig() {
		return config;
	} 
}
