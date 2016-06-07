package PreferenceAgent;

import RadioPlayer.MusicController;
import RadioPlayer.RadioStation;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 17-5-2016.
 */
public class StationPreferenceHandler implements MusicController.OnRadioStationChangedListener {

    private static XMLManager xmlManager = null;
    private RadioStation lastChoosenStation = null;
    private final static String DAYS[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private Timer timer;
    private int timerCounter = 0;

    public StationPreferenceHandler() {
        xmlManager = XMLManager.getInstance();
        timer = new Timer();
    }

    @Override
    public void onRadioStationChanged(RadioStation rs) {
        startListenTimer();
        if(lastChoosenStation != null){
            int listenTime = stopListenTimer();
            xmlManager.updateRadiostation(lastChoosenStation, listenTime, 0 ,0, getCurrentDay());
        }
        lastChoosenStation = rs;
        //xmlManager.updateRadiostation();
    }

    private String getCurrentDay(){
        return DAYS[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2];
    }



    private void startListenTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerCounter ++;
            }
        }, 1 * 10 * 1000, 1*10 * 1000);
    }

    private int stopListenTimer(){
        int returnValue;
        timer.cancel();
        timer.purge();
        returnValue = timerCounter;
        timerCounter = 0;
        return returnValue;
    }


}
