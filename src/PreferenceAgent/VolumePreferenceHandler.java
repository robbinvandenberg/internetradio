package PreferenceAgent;

import RadioPlayer.MainMenu;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 17-5-2016.
 */
public class VolumePreferenceHandler implements MainMenu.OnVolumeChangedListener {

    private Timer timer;
    private static final int TIMER_DELAY =  30 * 1000;


    public VolumePreferenceHandler() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, TIMER_DELAY);
    }

    @Override
    public void onVolumeChanged(int volume) {


    }
}
