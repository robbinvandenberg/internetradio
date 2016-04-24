import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import org.bulldog.beagleboneblack.BBBNames;
import org.bulldog.core.gpio.DigitalInput;
import org.bulldog.core.gpio.DigitalOutput;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.devices.switches.IncrementalRotaryEncoder;
import org.bulldog.devices.switches.RotaryEncoderListener;

/**
 * DeviceHandler.java
 * 
 * This static class has to ability to control some hardware.
 * With this class you can change volume, run linux commands, listen to rotary encoder changes etc.
 */
public final class DeviceHandler {
	private static DeviceHandler instance;
	/**
	 * Possible Hardware controls
	 */
	private enum Hardware {
	    BRIGHTNESS, LED_USR0, LED_USR1, LED_USR2, LED_USR3
	}
	/**
	 * Volume controls, to change master volume percentage use PCT
	 */
	private enum Volume {
	    PCT_PLUS, PCT_MINUS, PCT, Value
	}
	//private Board board = null;
	private IncrementalRotaryEncoder encoder = null;
	private ArrayList<DeviceHandlerListener> listeners;
	/**
	 * The directory where the .jar file is located
	 */
	public File ExecutionPath = null;
	/**
	 * The directory containing all images
	 */
	public File DefaultImagePath = null;
	
	private int sVOLUME = 10;
	private String amixerControl;
	//private int sBRIGHTNESS = 100; // cant set brightness on HDMI
	private int sTREBLE = 0;
	private int sBASS = 0;

	//Dont use pi4j. Testing on pc
	/*private static GpioController gpioController;
	private static GpioPinDigitalOutput amplifierSignal;*/
	
	/**
	 * Constructor
	 * Initializes rotary encoder, amplifier pin and sets some path variables.
	 */
	protected DeviceHandler() {
		// Exists only to defeat instantiation.
		//Set up two interrupts for the clockwise resp. counter clockwise signals on
        //the encoder
		//board = Platform.createBoard();
        //DigitalInput clockwiseSignal = board.getPin(BBBNames.P8_8).as(DigitalInput.class);
        //DigitalInput counterClockwiseSignal = board.getPin(BBBNames.P8_10).as(DigitalInput.class);

		/*if(gpioController == null) {
			gpioController = GpioFactory.getInstance();
			if(amplifierSignal == null){
				amplifierSignal = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
			}
		}*/

		//Create the encoder with these digital inputs
        //encoder = new IncrementalRotaryEncoder(clockwiseSignal, counterClockwiseSignal);
        //encoder.addListener(this);
        listeners = new ArrayList<>();
        ExecutionPath = getJarDir(this.getClass());
        //System.out.println("DeviceHandler: ExecutionPath: " + ExecutionPath.toString());
        DefaultImagePath = new File(ExecutionPath, File.separator + "images");
        System.out.println("DeviceHandler: DefaultImagePath: " + DefaultImagePath.toString());
        //System.out.println("DeviceHandler: test1: " + new File(DefaultImagePath,"noimage.png").getPath());
		amixerControl = getAmixerControl();
		setVolume(sVOLUME);
	}

	/**
	 * Should be used to get an instance of the DeviceHandler static class
	 */
	public static DeviceHandler getInstance() {
	  if(instance == null) {
	     synchronized(DeviceHandler.class) {
	       if(instance == null) {
	    	   instance = new DeviceHandler();
	       }
	    }
	  }
	  return instance;
	}
	
	/**
	 * Compute the absolute file path to the jar file.
	 * The framework is based on http://stackoverflow.com/a/12733172/1614775
	 * But that gets it right for only one of the four cases.
	 * 
	 * @param aclass A class residing in the required jar.
	 * 
	 * @return A File object for the directory in which the jar file resides.
	 * During testing with NetBeans, the result is ./build/classes/,
	 * which is the directory containing what will be in the jar.
	 */
	private File getJarDir(Class aclass) {
	    URL url;
	    String extURL;      //  url.toExternalForm();

	    // get an url
	    try {
	        url = aclass.getProtectionDomain().getCodeSource().getLocation();
	          // url is in one of two forms
	          //        ./build/classes/   NetBeans test
	          //        jardir/JarName.jar  froma jar
	    } catch (SecurityException ex) {
	        url = aclass.getResource(aclass.getSimpleName() + ".class");
	        // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
	        //          file:/U:/Fred/java/Tools/UI/build/classes
	        //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
	    }

	    // convert to external form
	    extURL = url.toExternalForm();

	    // prune for various cases
	    if (extURL.endsWith(".jar"))   // from getCodeSource
	        extURL = extURL.substring(0, extURL.lastIndexOf("/"));
	    else {  // from getResource
	        String suffix = "/"+(aclass.getName()).replace(".", "/")+".class";
	        extURL = extURL.replace(suffix, "");
	        if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
	            extURL = extURL.substring(4, extURL.lastIndexOf("/"));
	    }

	    // convert back to url
	    try {
	        url = new URL(extURL);
	    } catch (MalformedURLException mux) {
	        // leave url unchanged; probably does not happen
	    }

	    // convert url to File
	    try {
	        return new File(url.toURI());
	    } catch(URISyntaxException ex) {
	        return new File(url.getPath());
	    }
	}
	
	/**
	 * Retrieves the amixer volume simple control. This can change overtime
	 * In development time we found the values 'PCM' and 'Speaker'.
	 */
	private String getAmixerControl()
	{
		ArrayList<String> response = command("amixer scontrols",".");
		if (response == null)
		{
			JOptionPane.showMessageDialog(null,
				    "Failed retrieving simple controls from amixer.",
				    "amixer failed",
				    JOptionPane.ERROR_MESSAGE);
		}
		else if (response.size() <= 0)
		{
			JOptionPane.showMessageDialog(null,
				    "Failed retrieving simple controls from amixer.",
				    "amixer failed",
				    JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			int index = response.get(0).indexOf("'");
			if (index <= 0)
			{
				JOptionPane.showMessageDialog(null,
					    "Failed retrieving simple controls from amixer.",
					    "amixer failed",
					    JOptionPane.ERROR_MESSAGE);
			}
			index += 1;
			int endindex = response.get(0).indexOf("'", index);
			return response.get(0).substring(index, endindex);
		}
		//System.out.println("amixer control: " + amixerControl);
		return null;
	}
	
	/**
	 * Set linux hardware values
	 * 
	 * @param hw The hardware to control
	 * @param Value The value to set
	 */
	/*private void setLinuxHardwareValues(Hardware hw, int Value) {
		String command = "";
		switch (hw)
		{
		case BRIGHTNESS:
			command = "/sys/class/backlight/backlight.11/brightness";
			break;
		case LED_USR0:
			command = "/sys/class/leds/beaglebone:green:usr0/brightness";
			break;
		case LED_USR1:
			command = "/sys/class/leds/beaglebone:green:usr1/brightness";
			break;
		case LED_USR2:
			command = "/sys/class/leds/beaglebone:green:usr2/brightness";
			break;
		case LED_USR3:
			command = "/sys/class/leds/beaglebone:green:usr3/brightness";
			break;
		}
        ArrayList<String> output = command("echo "+Value+" > "+command, ".");
        if (null == output)
            System.out.println("\n\n\t\tCOMMAND FAILED: " + command);
        else
            for (String line : output)
                System.out.println(line);
    }
    Not usefull for the raspi
    */

	/**
	 * Execute a linux command, default wait till command is finished
	 * 
	 * @param cmdline The command to execute, may contains arguments
	 * @param directory the directory from where to execute the command
	 * @return An std::out from the issued command
	 */
	public ArrayList<String> command(final String cmdline, final String directory) {
		return command(cmdline, directory, true);
	}
	
	/**
	 * Execute a linux command
	 * 
	 * @param cmdline The command to execute, may contains arguments
	 * @param directory the directory from where to execute the command
	 * @param wait True to wait until command is finished and return std::out or false to return immediately, will return empty arraylist
	 * @return An std::out from the issued command
	 */
    public ArrayList<String> command(final String cmdline, final String directory, boolean wait) {
        try {
            Process process = 
                new ProcessBuilder(new String[] {"bash", "-c", cmdline})
                    .redirectErrorStream(true)
                    .directory(new File(directory))
                    .start();
            ArrayList<String> output = new ArrayList<String>();
            if (wait)
            {
	            BufferedReader br = new BufferedReader(
	                new InputStreamReader(process.getInputStream()));
	            String line = null;
	            while ( (line = br.readLine()) != null )
	                output.add(line);
	            if (0 != process.waitFor())
	            	return null;
            }
            return output;

        } catch (Exception e) {
            return null;
        }
    }	
    
    /**
     * Listen to rotary encoder and volume changes.
     * Only the last added listener will receive rotary encoder changes!
     * 
     * @param listener The listener to add
     * @return True if added successfully. Returns false only when already added
     */
    public boolean addListener(DeviceHandlerListener listener)
    {
		if(listeners != null) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
				return true;
			} else {
				return false;
			}
		}
		return false;
    }
    
    /**
     * Remove a listener. Should be called when closing GUI.
     * @param listener The listener to remove
     * @return False if already removed
     */
    public boolean removeListener(DeviceHandlerListener listener)
    {
    	if (listeners.contains(listener))
    	{
    		listeners.remove(listener);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
	
	/**
	 * Switches the amplifier on or off
	 * 
	 * @param state The state to set the amplifier to
	 */
	public void amplifierSwitch(boolean state){
		if(state)
		{
			//amplifierSignal.high();
			System.out.println("Ga aaaaan");
		}
		else
		{
			//amplifierSignal.low();
			System.out.println("Ga uuiutiuitt");
		}
	}	
	
	/**
	 * Runs volume change linux command using amixer
	 * 
	 * @param vl The volume control, can be increase with, decrease with, set percentage or raw value
	 * @param x The amount to adjust with
	 */
	private void changeVolume(Volume vl, int x){
		String command = "";
		switch (vl)
		{
		case PCT_PLUS:
			command = "amixer set '" + amixerControl + "' " + x + "%+";
			break;
		case PCT_MINUS:
			command = "amixer set '" + amixerControl + "' " + x + "%-";
			break;
		case PCT:
			command = "amixer set '" + amixerControl + "' " + x + "%";
			break;
		case Value:
			command = "amixer set '" + amixerControl + "' " + x;
			break;
		}
        
        if (vl == Volume.PCT)
        {
        	for (DeviceHandlerListener listener : listeners)
        	{
        		listener.volumeChanged(x);
        	}
        }
        ArrayList<String> output = command(command, ".");
        if (null == output)
            System.out.println("\n\n\t\tCOMMAND FAILED: " + command);
        /*else
            for (String line : output)
                System.out.println(line);*/
    }
	
	/**
	 * Retrieves master volume. 
	 * 
	 * @return the volume. Range 0 - 100
	 */
	public int getVolume(){
		return sVOLUME;
	}
	
	/**
	 * Set master volume.
	 * 
	 * @param x The percentage. Range 0 - 100
	 */
	public void setVolume(int x){
		changeVolume(DeviceHandler.Volume.PCT, x);
		sVOLUME = x;
	}
	
	/**
	 * Retrieve screen brightness percentage
	 * 
	 * @return The brightness. Range 0 - 100
	 */
	/*public int getBrightness(){
		return sBRIGHTNESS;
	}*/
	
	/**
	 * Set screen brightness
	 * 
	 * @param x The brightness percentage. Range 0 - 100
	 */
	/*public void setBrightness(int x){
		setLinuxHardwareValues(DeviceHandler.Hardware.BRIGHTNESS, x);	
		sBRIGHTNESS = x;
	}*/
	
	/**
	 * Get treble value. Currently not implemented
	 * 
	 * @return the treble value
	 */
	public int getTreble(){
		return sTREBLE;
	}
	
	/**
	 * Set treble value. Currently not implemented
	 * 
	 * @param x The treble value
	 */
	public void setTreble(int x){
		sTREBLE = x;
	}
	
	/**
	 * Get bass value. Currently not implemented
	 * 
	 * @return the bass value
	 */
	public int getBass(){
		return sBASS;
	}
	
	/**
	 * Set bass value. Currently not implemented
	 * 
	 * @param x The bass value
	 */
	public void setBass(int x){
		sBASS = x;
	}

}
