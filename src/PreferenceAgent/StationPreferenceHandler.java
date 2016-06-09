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
    private Timer timer;
    private int timerCounter = 0;

    private static FavouritesFile favoritesFile = null;
    private static final int UPDATE_THRESHOLD = 10;
    private static final int TIMER_DELAY_MINUTES = 1;

    public StationPreferenceHandler() {
        try {
            favoritesFile = FavouritesFile.load("radiostationFavorites.xml");
        }
        catch (UnableToParseFavoritesFileException e){
            e.printStackTrace();
        }
        timer = new Timer();
    }

    @Override
    public void onRadioStationChanged(RadioStation rs) {
        if(lastChoosenStation != null){
            int listenTime = stopListenTimer();

            if(listenTime >= UPDATE_THRESHOLD) {
                if(favoritesFile != null) {
                    FavouritesFile.Day currentDay = getCurrentDay();
                    Long currentListenTime = favoritesFile.getTime(lastChoosenStation, currentDay);
                    try {
                        System.out.println("Listening time: " + (currentListenTime + listenTime));
                        favoritesFile.setTime(lastChoosenStation, currentDay, currentListenTime + listenTime);
                    }
                    catch (TransformerException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        lastChoosenStation = rs;
        startListenTimer();
    }

    private FavouritesFile.Day getCurrentDay(){
        return FavouritesFile.Day.values()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
    }



    private void startListenTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer++");
                timerCounter++;
            }
        }, TIMER_DELAY_MINUTES * 60 * 1000, 1 * 60 * 1000);
    }

    private int stopListenTimer(){
        int returnValue;
        timer.cancel();
        timer.purge();
        timer = new Timer();
        returnValue = timerCounter;
        timerCounter = 0;
        return returnValue;
    }
}


