package converterXML.sync;

import java.sql.SQLException;

/**
 * Используется для синхронизации данных из файла и данных в базе данных
 */
public interface ISyncData {
    boolean SyncDataDB(String fileName) throws SQLException;
}
