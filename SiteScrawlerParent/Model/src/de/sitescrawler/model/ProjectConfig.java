package de.sitescrawler.model;

import java.io.FileInputStream; 
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped; 

@ApplicationScoped
public class ProjectConfig {

	private String domain;
	
	String result = "";
	InputStream inputStream;
 
	public ProjectConfig(){
		try { 
			String configPath = System.getProperty("jboss.server.config.dir");
			String configName = "\\config.properties";
			Path fileName = Paths.get(configPath, configName);
 
			Properties properties = new Properties();
			
			try(FileInputStream fis = new FileInputStream(fileName.toFile())) {
				  properties.load(fis);
				}
			catch (Exception e) {
				System.out.println("Failed to load conif");
				e.printStackTrace();
			}
			
			domain = properties.getProperty("domain"); 
			 
			System.out.println("Successfully loaded config.");
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
		 
		} 
	}

	public String getDomain() {
		return domain;
	}
}
