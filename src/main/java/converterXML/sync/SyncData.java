package converterXML.sync;

import converterXML.connection.DBConnection;
import converterXML.exception.AppConverterException;
import converterXML.model.KeyPosition;
import converterXML.model.Position;
import converterXML.parsing.IParserDataFromFile;
import converterXML.sqlRequests.IDataRecipient;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

/**
 * Используется для синхронизации данных из файла и данных в базе данных
 */
public class SyncData implements ISyncData{
    //поле-объект, используемый для получения данных из файла
    private final IParserDataFromFile parserDataFromFile;
    //поле-объект, используемый для получения данных
    private final IDataRecipient dataRecipientFromDB;

    private static final Logger log = Logger.getLogger(SyncData.class);

    public SyncData(IParserDataFromFile parserDataFromFile, IDataRecipient dataRecipientFromDB) {
        this.parserDataFromFile = parserDataFromFile;
        this.dataRecipientFromDB = dataRecipientFromDB;
    }

    /**
     * @param fileName имя файла, из которого необходимо получить данные
     * @return true - данные синхронизированны, false - синхронизация неуспешна
     * @throws SQLException
     */
    @Override
    public boolean SyncDataDB(String fileName) throws SQLException {
        HashMap<KeyPosition, Position> positionMapFile;
        try {
            positionMapFile = parserDataFromFile.parseDataFromFile(fileName);
        } catch (AppConverterException e){
            log.error(fileName + " XML file parsing error {}", e);
            return false;
        }

        HashMap<KeyPosition, Position> positionMapDB = null;

        try {
            positionMapDB = dataRecipientFromDB.getPositions();
        } catch (SQLException e) {
            log.error("Error getting data from database. {}", e);
            return false;
        }

        DBConnection.connection().setAutoCommit(false);

        Savepoint savepoint = DBConnection.connection().setSavepoint("SavePointOne");

        for (Map.Entry<KeyPosition, Position> entry : positionMapDB.entrySet()) {
            if (positionMapFile.containsKey(entry.getKey())){
                Position positionFile = positionMapFile.get(entry.getKey());
                Position positionDB = entry.getValue();

                if (positionFile.getDescription().equals(positionDB.getDescription())) {
                    positionMapFile.remove(entry.getKey());
                    continue;
                }

                try{
                    updateDescription(positionFile);
                    log.info("Field value is ready to be updated. Depcode value: " +
                            positionFile.getDepCode() + " Depjob value: " + positionFile.getDepJob());
                } catch (SQLException e){
                    log.error("Error updating table field values. {}", e);
                    DBConnection.connection().rollback(savepoint);
                    return false;
                }

                positionMapFile.remove(entry.getKey());
                continue;
            }

            try {
                deletePosition(entry.getValue());
                log.info("Field value is ready to be deleted. Depcode value: " +
                        entry.getValue().getDepCode() + " Depjob value: " + entry.getValue().getDepJob());
            } catch (SQLException e){
                log.error("Error deleting table values. {}", e);
                DBConnection.connection().rollback(savepoint);
                return false;
            }
        }

        try {
            log.info("Start preparing to insert new values:");
            insertNewPositions(positionMapFile);
        } catch (SQLException e){
            log.error("Error inserting table values. {}", e);
            DBConnection.connection().rollback(savepoint);
            return false;
        }

        DBConnection.connection().commit();
        log.info("Database synced successfully.");
        return true;
    }

    private void updateDescription(Position positionFile) throws SQLException {
        String sql = "UPDATE departmentpositions SET description = ?\n" +
                "WHERE depcode = ? AND depjob = ?";
        PreparedStatement preparedStatement = DBConnection.connection().prepareStatement(sql);
        preparedStatement.setString(1, positionFile.getDescription());
        preparedStatement.setString(2, positionFile.getDepCode());
        preparedStatement.setString(3, positionFile.getDepJob());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private void deletePosition(Position position) throws SQLException {
        String sql = "DELETE FROM departmentpositions\n" +
                "WHERE depcode = ? AND depjob = ?";
        PreparedStatement preparedStatement = DBConnection.connection().prepareStatement(sql);
        preparedStatement.setString(1, position.getDepCode());
        preparedStatement.setString(2, position.getDepJob());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private void insertNewPositions(HashMap<KeyPosition, Position> positionMapFile) throws SQLException {
        for (Map.Entry<KeyPosition, Position> entry : positionMapFile.entrySet()) {
            Position position = entry.getValue();
            String sql = "INSERT INTO departmentpositions (depcode, depjob, description)\n" +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = DBConnection.connection().prepareStatement(sql);
            preparedStatement.setString(1, position.getDepCode());
            preparedStatement.setString(2, position.getDepJob());
            preparedStatement.setString(3, position.getDescription());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            log.info("Field value is ready to be inserting. Depcode value: " +
                    position.getDepCode() + " Depjob value: " + position.getDepJob());
        }
    }
}
