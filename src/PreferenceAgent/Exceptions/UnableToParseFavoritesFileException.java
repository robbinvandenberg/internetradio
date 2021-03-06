package PreferenceAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 *
 * Exception class in case a componentfile cannot be parsed.
 */
public class UnableToParseFavoritesFileException extends Exception{

    /**
     * Unable to parse favorites file exception for super class with default contructor
     */
    public UnableToParseFavoritesFileException() {
        super();
    }

    /**
     * Unable to parse favorites file exception for super class with message constructor
     * @param message the exception message
     */
    public UnableToParseFavoritesFileException(String message) {
        super(message);
    }

    /**
     * Unable to parse favorites file exception for super class with inner exception
     * @param innerException the inner exception
     */
    public UnableToParseFavoritesFileException(Exception innerException) {
        super(innerException);
    }

    /**
     * Unable to parse favorites file exception for super class with message and inner exception
     * @param message the exception message
     * @param innerException the inner exception
     */
    public UnableToParseFavoritesFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
