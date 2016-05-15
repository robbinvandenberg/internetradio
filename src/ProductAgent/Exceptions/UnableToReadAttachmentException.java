package ProductAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 */
public class UnableToReadAttachmentException extends Exception{

    public UnableToReadAttachmentException() {
        super();
    }

    public UnableToReadAttachmentException(String message) {
        super(message);
    }

    public UnableToReadAttachmentException(Exception innerException) {
        super(innerException);
    }

    public UnableToReadAttachmentException(String message, Exception innerException) {
        super(message, innerException);
    }
}
