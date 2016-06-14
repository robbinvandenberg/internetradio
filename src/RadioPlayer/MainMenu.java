package RadioPlayer;

import PreferenceAgent.VolumePreferenceHandler;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.metal.MetalSliderUI;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * RadioPlayer.MainMenu.java
 * The main menu GUI of the internetradio
 * 
 * In this class the main menu GUI is represented to the user
 */
public class MainMenu extends JFrame implements ActionListener,ChangeListener, DeviceHandlerListener, WindowListener, PlayerMenu {
	public interface OnVolumeChangedListener {
		void onVolumeChanged (int volume);
	}

	private static final long serialVersionUID = 1L;
	
	//private RadioPlayer.PreferenceAgent PrefAgent;
	private DeviceHandler handler;
	private RadioStation currentRadioStation;
	private JSlider volumeSlider;
	private JLabel volumeLabel;
	private JToggleButton playButton;
	private JLabel stationNameLabel;
	private JLabel stationGenreLabel;
	private JButton settingsButton;
	private JButton radioStationsButton;

	private boolean volumeChangedByUser = true;
	
	public MusicController myMusicController;

	private ArrayList<OnVolumeChangedListener> listeners = new ArrayList<>();
	
	/**
	 * Constructor, builds GUI 
	 */
	public MainMenu(MusicController musicController)
	{
		UI ui = UI_Handler.readLayout("MainMenu");

		handler = DeviceHandler.getInstance();
		myMusicController = musicController;

		 
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		UI_Element slider = ui.getElementById("slVolume");
		volumeSlider = new JSlider();
		volumeSlider.setMinorTickSpacing(1);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.setMajorTickSpacing(10);
		volumeSlider.setFont(slider.getFont());
		volumeSlider.setBackground(new Color(224, 255, 255));
		volumeSlider.setForeground(new Color(0, 0, 0));
		volumeSlider.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), null, new Color(0, 0, 0)));
		volumeSlider.setBounds(slider.getxPos(), slider.getyPos(), slider.getxLength(), slider.getyLength());
		//volumeSlider.setBounds(10, 146, 460, 66);
		volumeSlider.setUI(new MetalSliderUI()
		{
			protected TrackListener createTrackListener(JSlider slider) 
			{
			    return new TrackListener() 
			    {
			      @Override public void mousePressed(MouseEvent e) 
			      {
			        JSlider slider = (JSlider)e.getSource();
			        volumeChangedByUser = true;
			        switch (slider.getOrientation()) {
			          case JSlider.VERTICAL:
			            slider.setValue(valueForYPosition(e.getY()));
			            break;
			          case JSlider.HORIZONTAL:
			            slider.setValue(valueForXPosition(e.getX()));
			            break;
			        }
			        super.mousePressed(e); //isDragging = true;
			        super.mouseDragged(e);
			      }
			      @Override public boolean shouldScroll(int direction) 
			      {
			          return false;
			      }
			    };
			}
		});
		volumeSlider.setValue(handler.getVolume());
		volumeSlider.addChangeListener(this);
		getContentPane().add(volumeSlider);
		
		UI_Element vol = ui.getElementById("lbVolume");
		volumeLabel = new JLabel("Volume");
		volumeLabel.setFont(vol.getFont());
		volumeLabel.setLabelFor(volumeSlider);
		volumeLabel.setBounds(vol.getxPos(), vol.getyPos(), vol.getxLength(), vol.getyLength());
		getContentPane().add(volumeLabel);
		
		UI_Element play = ui.getElementById("tglBtnPlay");
		playButton = new JToggleButton("Play");
		playButton.setFont(play.getFont());
		playButton.setBounds(play.getxPos(), play.getyPos(), play.getxLength(), play.getyLength());
		playButton.addActionListener(this);
		getContentPane().add(playButton);

		UI_Element stationName = ui.getElementById("stationName");
		stationNameLabel = new JLabel("");
		stationNameLabel.setFont(stationName.getFont());
		stationNameLabel.setBounds(stationName.getxPos(), stationName.getyPos(), stationName.getxLength(), stationName.getyLength());
		stationNameLabel.setVisible(true);
		getContentPane().add(stationNameLabel);

		UI_Element stationGenre = ui.getElementById("stationGenre");
		stationGenreLabel = new JLabel("");
		stationGenreLabel.setFont(stationGenre.getFont());
		stationGenreLabel.setBounds(stationGenre.getxPos(), stationGenre.getyPos(), stationGenre.getxLength(), stationGenre.getyLength());
		stationGenreLabel.setVisible(true);
		getContentPane().add(stationGenreLabel);
		
		UI_Element settings = ui.getElementById("btnSettings");
		settingsButton = new JButton("Settings");
		settingsButton.setFont(settings.getFont());
		settingsButton.setBounds(settings.getxPos(), settings.getyPos(), settings.getxLength(), settings.getyLength());
		settingsButton.addActionListener(this);
		getContentPane().add(settingsButton);
		
		UI_Element stationList = ui.getElementById("btnStationList");
		radioStationsButton = new JButton("Radiostations");
		radioStationsButton.setFont(stationList.getFont());
		radioStationsButton.setBounds(stationList.getxPos(), stationList.getyPos(), stationList.getxLength(), stationList.getyLength());
		radioStationsButton.addActionListener(this);
		radioStationsButton.setVisible(true);
		getContentPane().add(radioStationsButton);
		

		handler.addListener(this); //uses rotaryencoder and volumechanged listeners
		updateButtons();
		SettingsController settingsController = new SettingsController(myMusicController, this);
		settingsController.parseDefaultValues();
	}

	/**
	 * actionPer
	 * 
	 * Gets called by various GUI controls on action
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		if(e.getSource() == playButton){
			if(myMusicController.getCurrentRadiostation() != null)
			{
				if (playButton.isSelected() == true){
					myMusicController.playMusic(myMusicController.getCurrentRadiostation());
				}
				else{
					myMusicController.pauseMusic();
				}
				updateButtons();
			}
		}
		else if(e.getSource() == settingsButton){

			SettingsController frame = new SettingsController(myMusicController, this);
			frame.StartGUI();
			frame.setVisible(true);

			this.setVisible(false);
		}
		else if(e.getSource() == radioStationsButton){
			StationsMenu frame = new StationsMenu(this);
			frame.setVisible(true);

			this.setVisible(false);
		}
	}
	
	/**
	 * updateButtons function to update playButton
	 * 
	 * Updates playButton selection state and text according to play state
	 */
	private void updateButtons()
	{
		if (myMusicController.getPlayState() == MusicController.PlayState.PLAYING)
		{
			playButton.setSelected(true);
			playButton.setText("Pause");
		}
		else if (myMusicController.getPlayState() == MusicController.PlayState.PAUSED)
		{
			playButton.setSelected(false);
			playButton.setText("Resume");
		}
		else
		{
			playButton.setSelected(false);
			playButton.setText("Play");
		}
	}
	
	/**
	 * Gets called by JSlider
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(!source.getValueIsAdjusting() && volumeChangedByUser){
			handler.setVolume(volumeSlider.getValue());
			volumeChangedByUser = true; //reset

			for(OnVolumeChangedListener listener : listeners) {
				listener.onVolumeChanged(handler.getVolume());
			}
		}
	}

	/**
	 * Gets called by RadioPlayer.DeviceHandler when the master volume is changed
	 * 
	 * @param volume The volume percentage. Range 0 - 100
	 */
	@Override
	public void volumeChanged(int volume) {
		volumeChangedByUser = false;
		volumeSlider.setValue(volume);
	}


	@Override
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Event that occures when the GUI is closing
	 * 
	 * @param e The WindowEvent
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// on close
		handler.removeListener(this);
		myMusicController.stopMusic();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Used to set the radiostation for the main menu
	 * @param radioStation The radiostation to be set
     */

	@Override
	public void setRadioStation(RadioStation radioStation) {
		currentRadioStation = radioStation;
		myMusicController.playMusic(currentRadioStation);
		stationNameLabel.setText(currentRadioStation.getName());
		stationGenreLabel.setText(currentRadioStation.getGenre());
	}

	/**
	 * Set the volume on changed listener
	 * @param listener The Volume listener
     */
	public void setOnVolumeChangedListener(OnVolumeChangedListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove the on volume changed listener
	 * @param listener The listener to be removed
     */
	public void removeOnVolumeChangedListener (OnVolumeChangedListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
}
	        

