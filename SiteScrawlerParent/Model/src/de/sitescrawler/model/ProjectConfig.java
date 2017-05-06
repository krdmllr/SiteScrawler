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
	private String username;
	private String password;

	String result = "";
	InputStream inputStream;

	public ProjectConfig() {

		String configPath = System.getProperty("jboss.server.config.dir");

		if (configPath == null || configPath.isEmpty()) {
			//Perhabs server is not running
			System.out.println("Could not resolve config path.");
			configPath = "C:/Users/klmue/Source/server/standalone/configuration";
		}

		String configName = "config.properties";
		Path fileName = Paths.get(configPath, configName);

		Properties properties = new Properties();

		try (FileInputStream fis = new FileInputStream(fileName.toFile())) {
			properties.load(fis);

			domain = properties.getProperty("domain");
			username = properties.getProperty("username");
			password = properties.getProperty("password");

			System.out.println("Successfully loaded config.");
		} catch (Exception e) {
			System.out.println("Failed to load conifg");
			e.printStackTrace();
		} finally {

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
}
