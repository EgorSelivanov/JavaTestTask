package converterXML.parsing;

import converterXML.model.KeyPosition;
import converterXML.model.Position;

import java.util.HashMap;

/**
 * Используется для преобразования данных из файла в HashMap
 */
public interface IParserDataFromFile {
        /**
         * @param fileName имя файла для чтения данных
         * @return HashMap, ключ - KeyPosition, значение - Position (должность)
         */
        HashMap<KeyPosition, Position> parseDataFromFile(String fileName);
}
