package converterXML.sqlRequests;

import converterXML.model.KeyPosition;
import converterXML.model.Position;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * используется для получения данных из базы данных
 */
public interface IDataRecipient {
    HashMap<KeyPosition, Position> getPositions() throws SQLException;
}
