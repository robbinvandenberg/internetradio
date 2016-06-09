package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import RadioPlayer.MainMenu;

import javax.xml.transform.TransformerException;
import java.util.Timer;

/**
 * Created by nick on 17-5-2016.
 */
public class VolumePreferenceHandler implements MainMenu.OnVolumeChangedListener {

    private Timer timer;
    private static final int TIMER_DELAY =  30 * 1000;


    public VolumePreferenceHandler() {
        timer = new Timer();
        try {
            VolumeFile volumeFile = VolumeFile.load("volumeHandler.xml");
            try {
                volumeFile.setVolume(VolumeFile.Day.FRIDAY, VolumeFile.DayPart.AFTERNOON, 56);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            int test = volumeFile.getVolume(VolumeFile.Day.FRIDAY, VolumeFile.DayPart.AFTERNOON);
            System.out.println("Volume: " + test);

        } catch (UnableToParseFavoritesFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolumeChanged(int volume) {


    }
}
