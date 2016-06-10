package PreferenceAgent;

import RadioPlayer.MainMenu;
import RadioPlayer.MusicController;
import jade.core.Agent;

/**
 * RadioPlayer.PreferenceAgent.java
 * The RadioPlayer.PreferenceAgent class
 *
 * This class is used to sort the favorite menu. The favorites are sorted from most played radiostations descending.
 */
public class PreferenceAgent extends Agent {
    private static final long serialVersionUID = 1L;
    private MainMenu mainMenu;
    private MusicController musicController;
    private StationPreferenceHandler stationPreferenceHandler;
    private VolumePreferenceHandler volumePreferenceHandler;

    /**
     * The setup function.
     *
     * This function is to set the instance of RadioPlayer.MainMenu and set the time interval of the tickerbehaviour.
     */
    public void setup() {
        stationPreferenceHandler = new StationPreferenceHandler();

        musicController = new MusicController();
        musicController.setOnRadioStationChangedListener(stationPreferenceHandler);

        mainMenu = new MainMenu(musicController);

        volumePreferenceHandler = new VolumePreferenceHandler();

        mainMenu.setOnVolumeChangedListener(volumePreferenceHandler);
        mainMenu.setVisible(true);
    }


}