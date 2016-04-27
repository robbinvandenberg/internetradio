/**
 * Interface for listening to volume and rotary encoder changes. Used by DeviceHandler
 */
interface DeviceHandlerListener {
	/**
	 * Set master volume.
	 * 
	 * @param x The percentage. Range 0 - 100
	 */
	void volumeChanged(int volume);
}
