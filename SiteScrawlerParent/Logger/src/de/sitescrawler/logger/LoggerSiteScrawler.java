package de.sitescrawler.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger zum Sammeln von Informationen während der Ausführung der Applikatione. Diese Daten werden in eine externe
 * Datei gespeichert. Die Datei hat einen sprechenden Namen: "LoggingSiteScrawler_HH_mm_ss", wobei HH_mm_ss für den
 * aktuellen Zeitpunkt der Erstellung der Datei steht.
 *
 */
public class LoggerSiteScrawler
{
    static private FileHandler     loggerDateiTxt;
    static private SimpleFormatter loggingFormatterTxt;
    static private String          dateiName = "LoggingSiteScrawler_";

    static public void setup() throws IOException
    {

        // Namen der Datei konfigurieren ("LoggingSiteScrawler_HH_mm_ss")
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
        String zeitStempel = sdf.format(new Date());
        LoggerSiteScrawler.dateiName = LoggerSiteScrawler.dateiName.concat(zeitStempel);
        LoggerSiteScrawler.dateiName = LoggerSiteScrawler.dateiName.concat(".txt");

        // Globalen Logger holen zum Konfigurieren
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // Angabe, welche Level geloggt werden sollen
        logger.setLevel(Level.ALL);

        // Neue Datei für Logger-Ausgabe erzeugen
        // TODO: korrekte Ablage auf dem Server
        LoggerSiteScrawler.loggerDateiTxt = new FileHandler(LoggerSiteScrawler.dateiName);

        // Text für txt-Datei formatieren
        LoggerSiteScrawler.loggingFormatterTxt = new SimpleFormatter();
        LoggerSiteScrawler.loggerDateiTxt.setFormatter(LoggerSiteScrawler.loggingFormatterTxt);
        logger.addHandler(LoggerSiteScrawler.loggerDateiTxt);
    }
}
