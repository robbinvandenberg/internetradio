package PreferenceAgent;

import RadioPlayer.DeviceHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by thomas on 10-6-16.
 */
public class Constants {

    /**
     * Default name for the favorites file used in the whole project
     */
    public final static String FAVORITES_FILE_FILENAME = "radiostationFavorites.xml";

    /**
     * Default name for the volume file used in the the whole project
     */
    public final static String VOLUME_FILE_FILENAME = "volumeHandler.xml";

    /**
     * The default path directory to the internet radio directory
     */

    public static final String FILEPREFIX = DeviceHandler.getInstance().ExecutionPath.getAbsolutePath()+ File.separator;
}
