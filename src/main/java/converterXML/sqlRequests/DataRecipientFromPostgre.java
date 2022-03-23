package converterXML.sqlRequests;

import converterXML.connection.DBConnection;
import converterXML.model.KeyPosition;
import converterXML.model.Position;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


/**
 * используется для получения данных из базы данных PostgreSQL
 */
public class DataRecipientFromPostgre implements IDataRecipient{

    private static final Logger log = Logger.getLogger(DataRecipientFromPostgre.class);

    /**
     * @return HashMap, ключ: KeyPosition, значение: Position (должность)
     * @throws SQLException
     */
    @Override
    public HashMap<KeyPosition, Position> getPositions() throws SQLException {
        Statement statement = DBConnection.connection().createStatement();
        ResultSet resultSet =  statement.executeQuery("SELECT depcode, depjob, " +
                "description FROM departmentpositions");

        HashMap<KeyPosition, Position> positionHashMap = new HashMap<>();

        while (resultSet.next()) {
            String depCode = resultSet.getString("depcode");
            String depJob = resultSet.getString("depjob");
            String description = resultSet.getString("description");

            Position position = new Position(depCode, depJob, description);
            KeyPosition keyPosition = new KeyPosition(depCode, depJob);
            positionHashMap.put(keyPosition, position);
        }
        statement.close();
        log.info("A map of positions obtained from the database has been generated.");
        return positionHashMap;
    }
}
