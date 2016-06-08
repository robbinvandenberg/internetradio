package PreferenceAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 *
 * Exception class in case a componentfile cannot be parsed.
 */
public class UnableToParseFavoritesFileException extends Exception{

    public UnableToParseFavoritesFileException() {
        super();
    }

    public UnableToParseFavoritesFileException(String message) {
        super(message);
    }

    public UnableToParseFavoritesFileException(Exception innerException) {
        super(innerException);
    }

    public UnableToParseFavoritesFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
