package converterXML.save;

import converterXML.model.KeyPosition;
import converterXML.model.Position;
import converterXML.parsing.IParserDataToFile;
import converterXML.sqlRequests.IDataRecipient;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Используется для получения и дальнейшего сохранения данных в файл
 */
public class SaverDataToFile implements ISaverDataToFile{
    //поле-объект, используемый для получения данных
    private final IDataRecipient dataRecipient;
    //поле-объект, используемый для записи полученных данных в файл
    private final IParserDataToFile parserDataToFile;

    private static final Logger log = Logger.getLogger(SaverDataToFile.class);

    public SaverDataToFile(IDataRecipient dataRecipient, IParserDataToFile parserDataToFile) {
        this.dataRecipient = dataRecipient;
        this.parserDataToFile = parserDataToFile;
    }

    /**
     * @param fileName имя файла для записи
     * @return true - файл успешно создан, false - ошибка создания файла
     */
    @Override
    public boolean saveDataToFile(String fileName){
        HashMap<KeyPosition, Position> positionHashMap;
        try {
            positionHashMap = dataRecipient.getPositions();
        } catch (SQLException e) {
            log.error("Error in getting data from DB. {}", e);
            return false;
        }

        return parserDataToFile.parseDataFromMapToFile(positionHashMap, fileName);
    }
}
