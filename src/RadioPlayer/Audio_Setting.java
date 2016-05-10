package RadioPlayer;

/**
 * RadioPlayer.Audio_Setting.java
 * The RadioPlayer.Audio_Setting class
 * 
 * This data holder class defines the different audio settings that can be done.
 */
public class Audio_Setting {

	private String id;
	private int bass;
	private int treble;
	private int gain;
	private int pan;
	
	/**
	 * The constructor of the RadioPlayer.Audio_Setting class.
	 * 
	 * @param id Unique identifier
	 * @param bass bass value
	 * @param treble treble value
	 * @param gain gain value. Range 0 - 10
	 * @param pan pan value. Range -10 - 10
	 */
	public Audio_Setting(String id, int bass, int treble, int gain, int pan) {
		this.id = id;
		this.bass = bass;
		this.treble = treble;
		this.gain = gain;
		this.pan = pan;
	}
	
	/**
	 * Gets unique identifier.
	 * 
	 * Returns the id.
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Get the bass value.
	 * 
	 * Returned the bass. Type int.
	 * 
	 * @return bass
	 */
	public int getBass() {
		return bass;
	}
	
	/**
	 * Get the treble value.
	 * 
	 * Returned the treble. Type int.
	 * 
	 * @return treble
	 */
	public int getTreble() {
		return treble;
	}
	
	/**
	 * Get the gain value.
	 * 
	 * Returned the gain. Type int.
	 * 
	 * @return gain
	 */
	public int getGain() {
		return gain;
	}
	
	/**
	 * Gets the pan value.
	 * 
	 * Returned the pan. Type int.
	 * 
	 * @return pan
	 */
	public int getPan() {
		return pan;
	}
	
	/**
	 * The toString override.
	 * 
	 * @return String
	 */
	public String toString() {
		return ("" + id + ", "  + bass + ", " + treble + ", " + gain + ", " + pan);
	}
}
