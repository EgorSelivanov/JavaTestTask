package converterXML.parsing;

import converterXML.exception.AppConverterException;
import converterXML.model.KeyPosition;
import converterXML.model.Position;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Используется для преобразования данных из XML файла в HashMap
 */
public class ParserFromXMLFile implements IParserDataFromFile, ITagsXML{

    private static final Logger log = Logger.getLogger(ParserFromXMLFile.class);

    /**
     * @param fileName имя файла для чтения данных
     * @return HashMap, ключ - KeyPosition, значение - Position (должность)
     */
    @Override
    public HashMap<KeyPosition, Position> parseDataFromFile(String fileName) {
        Document document = buildDocument(fileName);

        Node rootNode = document.getFirstChild();
        if (!rootNode.getNodeName().equals(TAG_ROOT)) {
            log.debug("Unexpected value in xml file: " + rootNode.getNodeName());
            throw new AppConverterException("Unexpected value in xml file: " + rootNode.getNodeName());
        }

        NodeList rootChilds = rootNode.getChildNodes();

        return parsePositions(rootChilds);
    }

    /**
     * Парсинг XML, создание структуры Document для доступа ко всем элементам
     * @param fileName путь до xml файла
     * @return структура Document
     */
    private Document buildDocument(String fileName) {
        File file = new File(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder().parse(file);
        } catch (Exception e) {
            log.error("Error document build. {}", e);
            throw new AppConverterException("Error document build.");
        }
    }

    /**
     * Преобразует список узлов в HashMap
     * @param rootChilds производные узлы тега <>POSITIONS</>
     * @return HashMap должностей
     */
    private HashMap<KeyPosition, Position> parsePositions(NodeList rootChilds){
        if (rootChilds == null) {
            log.warn("Invalid xml file structure.");
            return null;
        }
        HashMap<KeyPosition, Position> positionMap = new HashMap<>();

        for(int i = 0; i < rootChilds.getLength(); i++) {
            if(rootChilds.item(i).getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (!rootChilds.item(i).getNodeName().equals(TAG_POSITION)) {
                continue;
            }

            Position position = parseElement(rootChilds.item(i));
            KeyPosition keyPosition = new KeyPosition(position.getDepCode(), position.getDepJob());

            if (positionMap.containsKey(keyPosition)) {
                log.warn("Duplicate key values in xml file.");
                throw new AppConverterException("Duplicate key values in xml file.");
            }
            positionMap.put(keyPosition, position);
        }
        log.info("A map of positions obtained from a file has been generated.");
        return positionMap;
    }

    /**
     * Преобразует узел <POSITION></POSITION> в должность
     * @param elementNode узел <POSITION></POSITION>
     * @return должность с заполненными полями
     */
    private Position parseElement(Node elementNode){
        NodeList positionChilds = elementNode.getChildNodes();

        String depCode = "";
        String depJob = "";
        String description = "";
        for (int j = 0; j < positionChilds.getLength(); j++){
            if(positionChilds.item(j).getNodeType() != Node.ELEMENT_NODE)
                continue;

            //Определяем текущий тег
            switch (positionChilds.item(j).getNodeName()){
                case TAG_DEPCODE:{
                    depCode = positionChilds.item(j).getTextContent();
                    break;
                }
                case TAG_DEPJOB:{
                    depJob = positionChilds.item(j).getTextContent();
                    break;
                }
                case TAG_DESCRIPTION:{
                    description = positionChilds.item(j).getTextContent();
                    break;
                }
                default:
                    log.warn("Unexpected value in xml file: " +
                            positionChilds.item(j).getNodeName());
                    throw new AppConverterException("Unexpected value in xml file: " +
                            positionChilds.item(j).getNodeName());
            }
        }
        return new Position(depCode, depJob, description);
    }
}
