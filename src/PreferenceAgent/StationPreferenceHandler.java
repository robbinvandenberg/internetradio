package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import PreferenceAgent.Utils.DateUtils;
import RadioPlayer.MusicController;
import RadioPlayer.RadioStation;

import javax.xml.transform.TransformerException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 17-5-2016.
 */
public class StationPreferenceHandler implements MusicController.OnRadioStationChangedListener, MusicController.OnPauseListener {

    private RadioStation lastChosenStation = null;
    private Timer timer;
    private int timerCounter = 0;

    private static FavouritesFile favoritesFile = null;
    private static final int UPDATE_THRESHOLD = 10;
    private static final int TIMER_DELAY_MINUTES = 1;

    /**
     * Constructor of the StationPreferenceHandler
     */
    public StationPreferenceHandler() {
        try {
            favoritesFile = FavouritesFile.load(Constants.FAVORITES_FILE_FILENAME);
        }
        catch (UnableToParseFavoritesFileException e){
            e.printStackTrace();
        }
        timer = new Timer();
    }

    /**
     * called when the Radiostation is changed by the user
     * @param rs
     */
    @Override
    public void onRadioStationChanged(RadioStation rs) {
        if(lastChosenStation != null){
            int listenTime = stopListenTimer();

            if(listenTime >= UPDATE_THRESHOLD) {
                if(favoritesFile != null) {
                    DateUtils.Day currentDay = DateUtils.getCurrentDay();
                    try {
                        favoritesFile.appendTime(lastChosenStation, currentDay, listenTime);
                    }
                    catch (TransformerException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        lastChosenStation = rs;
        startListenTimer();
    }

    /**
     * Called when the user pauses the playing Radiostation
     */
    @Override
    public void onPause() {
        if(lastChosenStation != null) {
            int listenTime = stopListenTimer();
            if (listenTime >= UPDATE_THRESHOLD) {
                if (favoritesFile != null) {
                    DateUtils.Day currentDay = DateUtils.getCurrentDay();
                    try {
                        favoritesFile.appendTime(lastChosenStation, currentDay, listenTime);
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Starts the timer that keeps the time listened to a Radiostation in minutes
     */
    private void startListenTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerCounter++;
            }
        }, TIMER_DELAY_MINUTES * 60 * 1000, 1 * 60 * 1000);
    }

    /**
     * Stops the current running timer
     * @return the time listened to a Radiostation
     */
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


