package converterXML.exception;

public class AppConverterException extends RuntimeException{

    public AppConverterException(String message) {
        super(message);
    }

    public AppConverterException(String message, Throwable e) {
        super(message, e);
    }
}
