package ProductAgent;

import java.util.Date;

/**
 * Created by Bart on 15-5-2016.
 *
 * Log class for logs.
 */
public class Log {
    private Date timeStamp;
    private String message;

    /**
     * Construcotr of log.
     *
     * @param timeStamp date and time of event.
     * @param message message of event.
     */
    public Log(Date timeStamp, String message){
        this.timeStamp = timeStamp;
        this.message = message;
    }

    /**
     * Get the date and time of the log event.
     *
     * @return date and time of event.
     */
    public Date getTimeStamp(){
        return timeStamp;
    }

    /**
     * Get the message of the log event.
     *
     * @return message of event.
     */
    public String getMessage(){
        return message;
    }
}
