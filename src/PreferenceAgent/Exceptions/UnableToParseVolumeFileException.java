package PreferenceAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 *
 * Exception class in case a componentfile cannot be parsed.
 */
public class UnableToParseVolumeFileException extends Exception{

    public UnableToParseVolumeFileException() {
        super();
    }

    public UnableToParseVolumeFileException(String message) {
        super(message);
    }

    public UnableToParseVolumeFileException(Exception innerException) {
        super(innerException);
    }

    public UnableToParseVolumeFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
