/**
 * RadiostationListener.java
 * Interface class for use to check for current radiostation change. Used by MusicController
 * 
 */
interface RadiostationListener {

	/**
	 * The onChange function
	 * 
	 * This is a function thats called when the current radiostation changed.
	 * 
	 * @param station The new radiostation.
	 */
	void onChange(Radiostation station);
}
