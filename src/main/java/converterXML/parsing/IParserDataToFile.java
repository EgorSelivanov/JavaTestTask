package converterXML.parsing;

import converterXML.model.KeyPosition;
import converterXML.model.Position;

import java.util.HashMap;

/**
 * Используется для преобразования данных из HashMap в файл
 */
public interface IParserDataToFile {
    boolean parseDataFromMapToFile(HashMap<KeyPosition, Position> positionHashMap, String fileName);
}
