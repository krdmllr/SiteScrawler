package de.sitescrawler.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectConfig
{

    private String domain;
    private String username;
    private String password;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    String         result = "";
    InputStream    inputStream;

    public ProjectConfig()
    {

        try
        {
            String configPath = System.getProperty("jboss.server.config.dir");

            if (configPath == null || configPath.isEmpty())
            {
                // Perhabs server is not running
                System.out.println("Could not resolve config path. Searching in project path for config instead.");
                String currentDir = System.getProperty("user.dir");
                File currentFile = new File(currentDir);

                while (!currentFile.getName().toLowerCase().equals("sitescrawler"))
                {
                    currentFile = currentFile.getParentFile();
                }
                configPath = currentFile.getAbsolutePath();
                System.out.println("Present Project Directory : " + configPath);
            }

            String configName = "config.properties";
            Path fileName = Paths.get(configPath, configName);

            Properties properties = new Properties();

            try (FileInputStream fis = new FileInputStream(fileName.toFile()))
            {
                properties.load(fis);

                this.domain = properties.getProperty("domain");
                this.username = properties.getProperty("username");
                this.password = properties.getProperty("password");
                this.consumerKey = properties.getProperty("consumerKey");
                this.consumerSecret = properties.getProperty("consumerSecret");
                this.accessToken = properties.getProperty("accessToken");
                this.accessTokenSecret = properties.getProperty("accessTokenSecret");

                System.out.println("Successfully loaded config.");
            }
            catch (Exception e)
            {
                System.out.println("Failed to load conifg");
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getDomain()
    {
        return this.domain;
    }

    public String getConsumerKey()
    {
        return this.consumerKey;
    }

    public String getConsumerSecret()
    {
        return this.consumerSecret;
    }

    public String getAccessToken()
    {
        return this.accessToken;
    }

    public String getAccessTokenSecret()
    {
        return this.accessTokenSecret;
    }

    public String getRessourcenDomain()
    {
        String configPath = System.getProperty("jboss.server.config.dir");
        if (configPath == null || configPath.isEmpty())
        {
            // Falls der Server nicht läuft
            System.out.println("Could not resolve config path. Searching in project path for config instead.");
            configPath = System.getProperty("user.dir") + "/src/de/sitescrawler/formatierung/hilfsdateien";
        }
        return configPath;
    }

}
