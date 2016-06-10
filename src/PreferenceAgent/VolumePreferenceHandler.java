package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import PreferenceAgent.Exceptions.UnableToParseVolumeFileException;
import RadioPlayer.MainMenu;

import javax.xml.transform.TransformerException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by nick on 17-5-2016.
 */
public class VolumePreferenceHandler implements MainMenu.OnVolumeChangedListener {

    private Timer timer;
    private VolumeFile volumeFile;
    private static final int TIMER_DELAY_MINUTES = 1;
    private int lastVolume = 0;
    private boolean taskActive = false;

    public VolumePreferenceHandler() {
        timer = new Timer();
        try {
            volumeFile = VolumeFile.load("volumeHandler.xml");
        } catch (UnableToParseVolumeFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolumeChanged(int volume) {
        lastVolume = volume;
        if (taskActive) {
            taskActive = false;
            timer.cancel();
            timer.purge();
            timer = new Timer();
        }

        writeVolumeAfterDelay();
    }

    private void writeVolumeAfterDelay() {
        taskActive = true;
        timer.schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    try {
                        int currentVolume = volumeFile.getVolume(getCurrentDay(), getCurrentDayPart());
                        volumeFile.setVolume(getCurrentDay(), getCurrentDayPart(), (lastVolume + currentVolume) / 2);
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    } finally {
                        taskActive = false;
                    }
                }
            },
            TIMER_DELAY_MINUTES * 60 * 1000
        );
    }

    private VolumeFile.Day getCurrentDay(){
        return VolumeFile.Day.values()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
    }

    private VolumeFile.DayPart getCurrentDayPart(){
        Date date = Calendar.getInstance().getTime();
         DateFormat.getDateInstance().format(date);

        SimpleDateFormat format = new SimpleDateFormat("HH");
        int currentHour = Integer.parseInt(format.format(date));
        System.out.println(currentHour);

        if (currentHour >= 0 && currentHour < 6) { // night
            return VolumeFile.DayPart.values()[0];
        } else if (currentHour >= 6 && currentHour < 12) { // morning
            return VolumeFile.DayPart.values()[1];
        } else if (currentHour >= 12 && currentHour < 18) { // afternoon
            return VolumeFile.DayPart.values()[2];
        } else if (currentHour >= 18 && currentHour < 24) { // evening
            return VolumeFile.DayPart.values()[3];
        }
        return null;
    }
}
