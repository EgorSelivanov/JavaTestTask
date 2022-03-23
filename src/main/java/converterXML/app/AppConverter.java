package converterXML.app;

import converterXML.parsing.ParserFromXMLFile;
import converterXML.parsing.ParserToXMLFile;
import converterXML.save.ISaverDataToFile;
import converterXML.save.SaverDataToFile;
import converterXML.sqlRequests.DataRecipientFromPostgre;
import converterXML.connection.DBConnection;
import converterXML.sync.ISyncData;
import converterXML.sync.SyncData;
import org.apache.log4j.Logger;

import java.sql.SQLException;


public class AppConverter {
    private static final Logger log = Logger.getLogger(AppConverter.class);

    public static void main(String[] args){
        try {
            DBConnection.getConnection();
        } catch (Exception e){
            System.out.println("Error connecting to database.");
            return;
        }

        if (args.length != 2){
            showMessages();
            return;
        }

        if (!args[1].contains(".xml")) {
            System.out.println("Incorrect name of file!");
            return;
        }

        switch (args[0]) {
            case "save": {
                ISaverDataToFile saverDataToFile = new SaverDataToFile(new DataRecipientFromPostgre(),
                        new ParserToXMLFile());
                if (saverDataToFile.saveDataToFile(args[1]))
                    System.out.println("Data successfully saved to file.");
                else
                    System.out.println("Save data error.");
                break;
            }
            case "sync": {
                ISyncData syncData = new SyncData(new ParserFromXMLFile(), new DataRecipientFromPostgre());
                try {
                    if (syncData.SyncDataDB(args[1])) {
                        System.out.println("Data sync done successfully.");
                        break;
                    }
                } catch (SQLException e) {
                    log.error("Data sync error. {}", e);
                }
                System.out.println("Data sync error.");
                break;
            }
            default: {
                System.out.println("Invalid function name entered!");
                showMessages();
            }
        }

        try {
            DBConnection.connection().close();
        } catch (SQLException e) {
            log.error("Connection close error. {}", e);
        }
    }

    /**
     * Функция вывода сообщений для пользователя
     */
    private static void showMessages(){
        System.out.println("You need to enter two parameters:");
        System.out.println("save - save data from database to XML file");
        System.out.println("OR");
        System.out.println("sync - upload data from XML file to database ");
        System.out.println("In the second parameter, enter the path to the file (with the extension .xml)");
    }
}
