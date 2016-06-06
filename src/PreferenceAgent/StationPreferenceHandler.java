package PreferenceAgent;

import RadioPlayer.MusicController;
import RadioPlayer.RadioStation;

/**
 * Created by nick on 17-5-2016.
 */
public class StationPreferenceHandler implements MusicController.OnRadioStationChangedListener {

    private static XMLManager xmlManager = null;

    public StationPreferenceHandler() {
        xmlManager = XMLManager.getInstance();

        xmlManager.writeStations();
    }

    @Override
    public void onRadioStationChanged(RadioStation rs) {

    }
}
