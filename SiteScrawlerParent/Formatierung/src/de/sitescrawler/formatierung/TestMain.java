package de.sitescrawler.formatierung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class TestMain
{

    public static void main(String[] args) throws URISyntaxException, FileNotFoundException, IOException
    {
        String resource = FormatiererService.class.getResource("hilfsdateien/HTMLvorArtikel.html").getFile();
        System.out.println(resource);
        String uriHtmlVorherFile = FormatiererService.class.getResource("hilfsdateien/HTMLvorArtikel.html").toString();
        System.out.println(uriHtmlVorherFile);
        File htmlVorherFile = new File(resource);
        try (FileReader fileReader = new FileReader(htmlVorherFile))
        {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.lines().forEach(System.out::println);
        }

        System.out.println("Root:" + System.getProperty("user.dir"));
    }

}
