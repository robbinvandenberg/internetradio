package ProductAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 */
public class UnableToParseComponentFileException extends Exception{

    public UnableToParseComponentFileException() {
        super();
    }

    public UnableToParseComponentFileException(String message) {
        super(message);
    }

    public UnableToParseComponentFileException(Exception innerException) {
        super(innerException);
    }

    public UnableToParseComponentFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
