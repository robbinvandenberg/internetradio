package ProductAgent.Exceptions;

/**
 * Created by Bart on 15-5-2016.
 *
 * Exception class in case a componentfile cannot be parsed.
 */
public class UnableToParseSimulationFileException extends Exception{

    public UnableToParseSimulationFileException() {
        super();
    }

    public UnableToParseSimulationFileException(String message) {
        super(message);
    }

    public UnableToParseSimulationFileException(Exception innerException) {
        super(innerException);
    }

    public UnableToParseSimulationFileException(String message, Exception innerException) {
        super(message, innerException);
    }
}
