package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseVolumeFileException;
import PreferenceAgent.Utils.DateUtils;
import RadioPlayer.DeviceHandler;
import RadioPlayer.MainMenu;

import javax.xml.transform.TransformerException;
import java.util.Timer;

/**
 * Created by nick on 17-5-2016.
 */
public class VolumePreferenceHandler implements MainMenu.OnVolumeChangedListener {

    private Timer timer;
    private VolumeFile volumeFile;
    private static final int TIMER_DELAY_MINUTES = 10;
    private static final int DAYPART_CHECKRATE_MINUTES = 10;
    private int lastVolume = 0;
    private boolean taskActive = false;
    private Thread checkDayPartThread;

    private Runnable StartHandlingDayPartChange = new Runnable(){
        public void run(){
            System.out.println("Runnable running");
            while(!Thread.currentThread().isInterrupted()){
                DateUtils.DayPart lastDaypart = DateUtils.getCurrentDayPart();
                while (lastDaypart == DateUtils.getCurrentDayPart()){
                    try {
                        Thread.sleep(DAYPART_CHECKRATE_MINUTES * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setDeviceVolume(volumeFile.getVolume(DateUtils.getCurrentDay(), DateUtils.getCurrentDayPart()));
            }
        }
    };

    public VolumePreferenceHandler() {
        timer = new Timer();
        checkDayPartThread = new Thread(StartHandlingDayPartChange);
        checkDayPartThread.start();
        try {
            volumeFile = VolumeFile.load("volumeHandler.xml");
            setVolumeOnBoot();
        } catch (UnableToParseVolumeFileException e) {
            e.printStackTrace();
        }
    }

    private void setVolumeOnBoot(){
        setDeviceVolume(volumeFile.getVolume(DateUtils.getCurrentDay(), DateUtils.getCurrentDayPart()));
    }

    private void setDeviceVolume(int volume){
        DeviceHandler.getInstance().setVolume(volume);
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
                        int currentVolume = volumeFile.getVolume(DateUtils.getCurrentDay(), DateUtils.getCurrentDayPart());
                        volumeFile.setVolume(DateUtils.getCurrentDay(), DateUtils.getCurrentDayPart(), (lastVolume + currentVolume) / 2);
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
}
