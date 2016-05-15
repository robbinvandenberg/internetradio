package ProductAgent;

import java.util.Date;

/**
 * Created by Bart on 15-5-2016.
 */
public class Log {
    private Date timeStamp;
    private String message;

    public Log(Date timeStamp, String message){
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }

    public String getMessage(){
        return message;
    }
}
