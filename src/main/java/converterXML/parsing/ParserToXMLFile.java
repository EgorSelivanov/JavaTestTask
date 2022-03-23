package converterXML.parsing;

import converterXML.model.KeyPosition;
import converterXML.model.Position;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Используется для преобразования данных из HashMap в файл XML
 */
public class ParserToXMLFile implements IParserDataToFile, ITagsXML{

    private static final Logger log = Logger.getLogger(ParserToXMLFile.class);

    /**
     * @param positionHashMap HashMap, ключ - KeyPosition, значение - Position (должность)
     * @param fileName имя файла для записи
     * @return true - если запись успешна, false - если запись не происходила
     */
    @Override
    public boolean parseDataFromMapToFile(HashMap<KeyPosition, Position> positionHashMap, String fileName) {
        Document document = null;
        try {
            document = buildDocument();
        } catch (ParserConfigurationException e) {
            log.error("Error document build. {}", e);
            return false;
        }

        formingContentOfXMLFile(document, positionHashMap);

        try {
            buildNewXMLFile(document, fileName);
        } catch (TransformerException | FileNotFoundException e) {
            log.error("XML file creation error. {}", e);
            return false;
        }
        return true;
    }

    /**
     * Создает новую структуру Document для записи
     * @return структура Document
     * @throws ParserConfigurationException
     */
    private Document buildDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.newDocument();
    }

    /**
     * Формирует содержание XML-файла
     * @param document созданная структура Document
     * @param positionHashMap HashMap, ключ - KeyPosition, значение - Position (должность)
     */
    private void formingContentOfXMLFile(Document document, HashMap<KeyPosition, Position> positionHashMap){
        Element root = document.createElement(TAG_ROOT);
        Element position, depCode, depJob, description;
        document.appendChild(root);
        for (Map.Entry<KeyPosition, Position> entry : positionHashMap.entrySet()) {
            Position pos = entry.getValue();

            position = document.createElement(TAG_POSITION);
            root.appendChild(position);

            depCode = document.createElement(TAG_DEPCODE);
            position.appendChild(depCode);
            depCode.appendChild(document.createTextNode(pos.getDepCode()));

            depJob = document.createElement(TAG_DEPJOB);
            position.appendChild(depJob);
            depJob.appendChild(document.createTextNode(pos.getDepJob()));

            description = document.createElement(TAG_DESCRIPTION);
            position.appendChild(description);
            description.appendChild(document.createTextNode(pos.getDescription()));
        }
        log.info("XML file content created successfully.");
    }

    /**
     * Преобразование DOMSource XML в результируюший файл.
     * @param document созданная структура Document
     * @param fileName имя результирующего XML файла
     * @throws TransformerException
     * @throws FileNotFoundException
     */
    private void buildNewXMLFile(Document document, String fileName) throws
            TransformerException, FileNotFoundException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(fileName)));
        log.info("XML file created successfully.");
    }
}
