/**
 * Interface for listening to volume and rotary encoder changes. Used by DeviceHandler
 */
interface DeviceHandlerListener {
    /**
     * This function is called when the rotary encoder value is changed
     */
	void rotaryValueChanged(int oldValue, int newValue);
	
	/**
	 * This function is called when the rotary encoder is turned clockwise
	 */
	void rotaryTurnedClockwise();
	
	/**
	 * This function is called when the rotary encoder is turned counter clockwise
	 */
	void rotaryTurnedCounterclockwise();
	/**
	 * Set master volume.
	 * 
	 * @param x The percentage. Range 0 - 100
	 */
	void volumeChanged(int volume);
}
