package de.sitescrawler.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectConfig {

	private String domain;
	private String username;
	private String password;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;

	String result = "";
	InputStream inputStream;

	public ProjectConfig() {

		try{
			String configPath = System.getProperty("jboss.server.config.dir");

			if (configPath == null || configPath.isEmpty()) {
				//Perhabs server is not running
				System.out.println("Could not resolve config path. Searching in project path for config instead.");
				String currentDir = System.getProperty("user.dir");
				File currentFile = new File(currentDir);
				
				while(!currentFile.getName().toLowerCase().equals("sitescrawler")){
					currentFile = currentFile.getParentFile();
				}
				configPath =  currentFile.getAbsolutePath();
				System.out.println("Present Project Directory : " + configPath);
			}

			String configName = "config.properties";
			Path fileName = Paths.get(configPath, configName);

			Properties properties = new Properties();

			try (FileInputStream fis = new FileInputStream(fileName.toFile())) {
				properties.load(fis);

				domain = properties.getProperty("domain");
				username = properties.getProperty("username");
				password = properties.getProperty("password");
				consumerKey = properties.getProperty("consumerKey");
				consumerSecret = properties.getProperty("consumerSecret");
				accessToken = properties.getProperty("accessToken");
				accessTokenSecret = properties.getProperty("accessTokenSecret");

				System.out.println("Successfully loaded config.");
			} catch (Exception e) {
				System.out.println("Failed to load conifg");
				e.printStackTrace();
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDomain() {
		return domain;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

}
