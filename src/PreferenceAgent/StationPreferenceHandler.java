package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import RadioPlayer.MusicController;
import RadioPlayer.RadioStation;

import javax.xml.transform.TransformerException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 17-5-2016.
 */
public class StationPreferenceHandler implements MusicController.OnRadioStationChangedListener {

    private RadioStation lastChoosenStation = null;
    private final static String DAYS[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private Timer timer;
    private int timerCounter = 0;

    private String beginHour;

    private static FavouritesFile testFile = null;

    private static final int UPDATE_THRESHOLD = 0;

    private static final int TIMER_DELAY_HOURS = 1;

    public StationPreferenceHandler() {

        timer = new Timer();

    }

    @Override
    public void onRadioStationChanged(RadioStation rs) {
        if(lastChoosenStation != null){
            int listenTime = stopListenTimer();

            if(listenTime >= UPDATE_THRESHOLD)
                return;
                //xmlManager.updateRadiostation(lastChoosenStation, listenTime, beginHour, getCurrentHour(), getCurrentDay());
        }
        lastChoosenStation = rs;

        startListenTimer();
    }

    private String getCurrentDay(){
        return DAYS[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2];
    }



    private void startListenTimer(){
        beginHour = getCurrentHour();
        System.out.println(beginHour);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerCounter++;
            }
        }, TIMER_DELAY_HOURS * 60 * 1000, 1 * 60 * 1000);
    }

    private int stopListenTimer(){
        int returnValue;
        timer.cancel();
        timer.purge();
        returnValue = timerCounter;
        timerCounter = 0;
        return returnValue;
    }

    private String getCurrentHour() {
        DateFormat df = new SimpleDateFormat("HH");
        Calendar cal = Calendar.getInstance();
        return df.format(cal.getTime());
    }


}


