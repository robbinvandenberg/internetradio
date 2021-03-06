package PreferenceAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 *
 * Exception class in case a componentfile cannot be parsed.
 */
public class UnableToParseVolumeFileException extends Exception{

    /**
     * Unable to parse volume file exception with default contructor
     */
    public UnableToParseVolumeFileException() {
        super();
    }

    /**
     * Unable to parse volume file exception with message contructor
     * @param message the exception message
     */
    public UnableToParseVolumeFileException(String message) {
        super(message);
    }

    /**
     * Unable to parse volume file exception with message and innerexception contructor
     * @param innerException the inner exception
     */
    public UnableToParseVolumeFileException(Exception innerException) {
        super(innerException);
    }

    /**
     * Unable to parse volume file exception with message and innerexception contructor
     * @param message the exception message
     * @param innerException the inner exception
     */
    public UnableToParseVolumeFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
