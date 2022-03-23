package converterXML.connection;

import converterXML.properties.Properties;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Используется для установления подключения к БД и доступа к нему
 */
public class DBConnection {

    private static Connection connection;
    private static final Logger log = Logger.getLogger(DBConnection.class);

    private DBConnection(){}

    /**
     * функция установления подключения
     */
    public static void getConnection()
    {
        try{
            connection = DriverManager.getConnection(
                    Properties.getProperty(Properties.DB_URL),
                    Properties.getProperty(Properties.DB_LOGIN),
                    Properties.getProperty(Properties.DB_PASSWORD));
        } catch (SQLException | RuntimeException ex) {
            log.error("Error connecting to database. {}", ex);
            throw new RuntimeException("Error connecting to database.");
        }
    }

    /**
     * @return возвращает установленное подключение
     */
    public static Connection connection() {
        return connection;
    }
}
