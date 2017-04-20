package de.sitescrawler.applikation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;import javax.servlet.ServletRequest;

public class ProjectConfig {

	private String domain;
	
	String result = "";
	InputStream inputStream;
 
	public void loadConfig(){
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			String fileName = System.getProperty("jboss.server.config.dir") + "\\config.properties";
 
			Properties properties = new Properties();
			
			try(FileInputStream fis = new FileInputStream(fileName)) {
				  properties.load(fis);
				}
			
			domain = properties.getProperty("domain");
			
			 
  
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
		 
		} 
	}

	public String getDomain() {
		return domain;
	}
}
