package de.sitescrawler.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
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

        // Consolenausgabe unterdrücken
        Logger wurzelLogger = Logger.getLogger("");
        Handler[] handlers = wurzelLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler)
        {
            wurzelLogger.removeHandler(handlers[0]);
        }

        // Neue Datei für Logger-Ausgabe erzeugen
        logger.setLevel(Level.INFO);
        LoggerSiteScrawler.loggerDateiTxt = new FileHandler(LoggerSiteScrawler.dateiName);

        // Text für txt-Datei formatieren
        LoggerSiteScrawler.loggingFormatterTxt = new SimpleFormatter();
        LoggerSiteScrawler.loggerDateiTxt.setFormatter(LoggerSiteScrawler.loggingFormatterTxt);
        logger.addHandler(LoggerSiteScrawler.loggerDateiTxt);
    }
}
