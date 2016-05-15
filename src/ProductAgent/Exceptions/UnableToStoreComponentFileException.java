package ProductAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 */
public class UnableToStoreComponentFileException extends Exception{
    public UnableToStoreComponentFileException() {
        super();
    }

    public UnableToStoreComponentFileException(String message) {
        super(message);
    }

    public UnableToStoreComponentFileException(Exception innerException) {
        super(innerException);
    }

    public UnableToStoreComponentFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
