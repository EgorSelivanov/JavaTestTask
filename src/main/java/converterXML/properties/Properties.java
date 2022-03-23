package converterXML.properties;

import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Используется для получения настроек приложения из DB.properties
 */
public class Properties {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    private static final Logger log = Logger.getLogger(Properties.class);

    private static final java.util.Properties properties = new java.util.Properties();

    /**
     * @param name имя параметра настройки
     * @return значение данного параметра
     */
    public static String getProperty(String name) {
        if (properties.isEmpty()){
            try (InputStream is = Properties.class.getClassLoader().getResourceAsStream("DB.properties")) {
                properties.load(is);
            } catch (Exception ex){
                log.error("Error reading database settings file. {}", ex);
                throw new RuntimeException("Error reading database settings file.");
            }
        }
        log.info("Reading database" + name + " settings file was successful.");
        return properties.getProperty(name);
    }
}
