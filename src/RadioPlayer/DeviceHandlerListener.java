package RadioPlayer;

/**
 * Interface for listening to volume and rotary encoder changes. Used by RadioPlayer.DeviceHandler
 */
interface DeviceHandlerListener {
	/**
	 * Set master volume.
	 * 
	 * @param x The percentage. Range 0 - 100
	 */
	void volumeChanged(int volume);
}
