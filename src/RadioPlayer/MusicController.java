package RadioPlayer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 * The RadioPlayer.MusicController class
 * 
 * This class provides radiostreaming functionality and holds current radiostation
 */
public class MusicController {
	private DeviceHandler deviceHandler = new DeviceHandler();
	private BasicPlayer myMusicPlayer;
	private BasicController playerController;
	private RadioStation currentRadiostation;
	private URLConnection conn;
	private InputStream is;
	private BufferedInputStream bis;
	private PlayState playState;
	
	/**
	 * The current play state
	 */
	public enum PlayState
	{
		PLAYING,
		PAUSED,
		STOPPED
	}
	
	/**
	 * Constructor, instantiate streaming audioplayer
	 */
	public MusicController() {
		playState = PlayState.STOPPED;
		myMusicPlayer=new BasicPlayer();
		playerController=(BasicController)myMusicPlayer;	
	}


	
    /**
     * Start streaming a radiostation
     * 
     * @param rs The radiostation to start streaming
     */
	public void playMusic(RadioStation rs){
	    try {
	    	deviceHandler.amplifierSwitch(true);
	    	try {
	    		conn = rs.getStreamUrl().openConnection();
			} catch (IOException e) {
				return;
			}
	    	try {
				is = conn.getInputStream();
			} catch (IOException e) {
				return;
			}
	    	bis = new BufferedInputStream(is);
			playerController.open(bis);
			playerController.play();
			playState = PlayState.PLAYING;
			currentRadiostation = rs;
			System.out.println("Now playing: " + rs.getName());
		} catch (BasicPlayerException e) {
		}
	}
	
	/**
	 * Pause radio stream
	 */
	public void pauseMusic(){
		try {
			playerController.pause();
			playState = PlayState.PAUSED;
		} catch (BasicPlayerException e) {
		}
	}
	
	/**
	 * Resume radio stream
	 */
	public void resumeMusic(){
		try {
			playerController.resume();
			playState = PlayState.PLAYING;
		} catch (BasicPlayerException e) {
		}
	}
	
	/**
	 * Stop streaming radio. This closes the active connection
	 */
	public void stopMusic(){
		try {
			deviceHandler.amplifierSwitch(false);
			playerController.stop();
			playState = PlayState.STOPPED;
		} catch (BasicPlayerException e) {
		}
	}

	
	/**
	 * Get current selected radiostation
	 * 
	 * @return The current radiostation
	 */
	public RadioStation getCurrentRadiostation(){
		return currentRadiostation;
	}
	
	/**
	 * Get current play state
	 * 
	 * @return The current play state
	 */
	public PlayState getPlayState()
	{
		return playState;
	}
	
	/**
	 * Get volume gain. Default 10
	 * 
	 * @return The volume gain. Range 0 - 10
	 */
	public int getGain(){
		float t = myMusicPlayer.getGainValue() * 10; 
		return (int)t;
	}
	
	/**
	 * Set volume gain. Default 10
	 * 
	 * @param x The volume gain to set to. Range 0 - 10
	 */
	public void setGain(int x){
		double t = ((double)x)/10;
		try {
			myMusicPlayer.setGain(t);
		} catch (BasicPlayerException e) {
		}
	}
	
	/**
	 * Get volume balance. This is actually the pan
	 * 
	 * @return The balance. Range -10 - 10
	 */
	public int getBalance(){
		float t = myMusicPlayer.getPan() * 10;
		return (int)t;
	}
	
	/**
	 * Set volume balance. The is actually the pan
	 * 
	 * @param x The balance to set. Range -10 - 10
	 */
	public void setBalance(int x){
		double t = ((double)x)/10;
		try {
			myMusicPlayer.setPan(t);
		} catch (BasicPlayerException e) {
		}
	}
}
