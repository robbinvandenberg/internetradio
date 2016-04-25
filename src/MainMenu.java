import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.metal.MetalSliderUI;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * MainMenu.java
 * The main menu GUI of the internetradio
 * 
 * In this class the main menu GUI is represented to the user
 */
public class MainMenu extends JFrame implements ActionListener,ChangeListener, DeviceHandlerListener, RadiostationListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	//private PreferenceAgent PrefAgent;
	private DeviceHandler handler;
	private RadiostationHandler rhandler;
	private JButton btnStop;
	private JButton btnPrev;
	private JButton btnNext;
	private JSlider slVolume;
	private JLabel lbVolume;
	private JToggleButton tglBtnPlay;
	private ImagePanel jpImage;
	private JButton btnSettings;
	private JButton btnStationList;
	private JButton btnFavorites;
	private boolean volumeChangedByUser = true;
	
	public MusicController myMusicController;
	
	/**
	 * Constructor, builds GUI 
	 */
	public MainMenu() 
	{
		UI ui = UI_Handler.readLayout("MainMenu");
		//PrefAgent = pa;
		handler = DeviceHandler.getInstance();
		rhandler = new RadiostationHandler();
		myMusicController = new MusicController();
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		//Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    //cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		//getContentPane().setCursor(blankCursor);
		 
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		UI_Element stop = ui.getElementById("btnStop");
		btnStop = new JButton("Stop");
		btnStop.setFont(stop.getFont());
		//btnStop.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnStop.setBounds(371, 223, 99, 38);
		btnStop.setBounds(stop.getxPos(), stop.getyPos(), stop.getxLength(), stop.getyLength());
		//System.out.println("" + stop.getxPos() + ", " + stop.getyPos() + ", " + stop.getxLength() + ", " + stop.getyLength());
		btnStop.addActionListener(this);
		getContentPane().add(btnStop);
		
		UI_Element prev = ui.getElementById("btnPrev");
		btnPrev = new JButton("Prev");
		btnPrev.setBounds(prev.getxPos(), prev.getyPos(), prev.getxLength(), prev.getyLength());
		btnPrev.setFont(prev.getFont());
		//btnPrev.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnPrev.setBounds(10, 223, 78, 38);
		btnPrev.addActionListener(this);
		getContentPane().add(btnPrev);
		
		UI_Element next = ui.getElementById("btnNext");
		btnNext = new JButton("Next");
		btnNext.setBounds(next.getxPos(), next.getyPos(), next.getxLength(), next.getyLength());
		btnNext.setFont(next.getFont());
		//btnNext = new JButton("Next");
		//btnNext.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnNext.setBounds(288, 223, 78, 38);
		btnNext.addActionListener(this);
		getContentPane().add(btnNext);
		
		UI_Element slider = ui.getElementById("slVolume");
		slVolume = new JSlider();
		slVolume.setMinorTickSpacing(1);
		slVolume.setSnapToTicks(true);
		slVolume.setPaintTicks(true);
		slVolume.setPaintLabels(true);
		slVolume.setMajorTickSpacing(10);
		//slVolume.setFont(new Font("Tahoma", Font.BOLD, 11));
		slVolume.setFont(slider.getFont());
		//slVolume.setMinimumSize(new Dimension(36, 43));
		//slVolume.setMaximumSize(new Dimension(32767, 43));
		slVolume.setBackground(new Color(224, 255, 255));
		slVolume.setForeground(new Color(0, 0, 0));
		slVolume.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), null, new Color(0, 0, 0)));
		slVolume.setBounds(slider.getxPos(), slider.getyPos(), slider.getxLength(), slider.getyLength());
		//slVolume.setBounds(10, 146, 460, 66);
		slVolume.setUI(new MetalSliderUI() 
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
		slVolume.setValue(handler.getVolume());
		slVolume.addChangeListener(this);
		getContentPane().add(slVolume);
		
		UI_Element vol = ui.getElementById("lbVolume");
		lbVolume = new JLabel("Volume");
		lbVolume.setFont(vol.getFont());
		//lbVolume.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbVolume.setLabelFor(slVolume);
		//lbVolume.setBounds(10, 128, 460, 14);
		lbVolume.setBounds(vol.getxPos(), vol.getyPos(), vol.getxLength(), vol.getyLength());
		getContentPane().add(lbVolume);
		
		UI_Element play = ui.getElementById("tglBtnPlay");
		tglBtnPlay = new JToggleButton("Play");
		tglBtnPlay.setFont(play.getFont());
		//tglBtnPlay.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglBtnPlay.setBounds(play.getxPos(), play.getyPos(), play.getxLength(), play.getyLength());
		//tglBtnPlay.setBounds(93, 223, 190, 38);
		tglBtnPlay.addActionListener(this);
		getContentPane().add(tglBtnPlay);
		
		UI_Element image = ui.getElementById("jpImage");
		jpImage = new ImagePanel("");
		jpImage.setBorder(new MatteBorder(1, 2, 3, 2, (Color) new Color(0, 0, 0)));
		jpImage.setBackground(new Color(224, 255, 255));
		jpImage.setBounds(image.getxPos(), image.getyPos(), image.getxLength(), image.getyLength());
		//jpImage.setBounds(10, 11, 106, 106);
		jpImage.setVisible(true);
		getContentPane().add(jpImage);		
		
		UI_Element settings = ui.getElementById("btnSettings");
		btnSettings = new JButton("Settings");
		btnSettings.setFont(settings.getFont());
		//btnSettings.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSettings.setBounds(settings.getxPos(), settings.getyPos(), settings.getxLength(), settings.getyLength());
		//btnSettings.setBounds(176, 11, 190, 38);
		btnSettings.addActionListener(this);
		getContentPane().add(btnSettings);
		
		UI_Element stationList = ui.getElementById("btnStationList");
		btnStationList = new JButton("Radiostations");
		btnStationList.setFont(stationList.getFont());
		btnStationList.setBounds(stationList.getxPos(), stationList.getyPos(), stationList.getxLength(), stationList.getyLength());
		//btnStationList.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnStationList.setBounds(176, 77, 190, 38);
		btnStationList.addActionListener(this);
		getContentPane().add(btnStationList);
		
		UI_Element favorites = ui.getElementById("btnFavorites");
		btnFavorites = new JButton("Favorites");
		btnFavorites.setFont(favorites.getFont());
		btnFavorites.setBounds(favorites.getxPos(), favorites.getyPos(), favorites.getxLength(), favorites.getyLength());
		btnFavorites.addActionListener(this);
		getContentPane().add(btnFavorites);
		handler.addListener(this); //uses rotaryencoder and volumechanged listeners
		myMusicController.addListener(this);
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
		if(e.getSource() == tglBtnPlay){
			if(myMusicController.getCurrentRadiostation() != null)
			{
				if (tglBtnPlay.isSelected() == true){
					myMusicController.playMusic(myMusicController.getCurrentRadiostation());
				}
				else{
					myMusicController.pauseMusic();
				}
				updateButtons();
			}
			else
			{
				ArrayList<Radiostation> list = rhandler.getPreferredStations();
				if (list.size() > 0)
				{
					Radiostation rs = list.get(0);
					ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,list.get(0).getImage()).getPath());
					jpImage.setPic(icon);
					myMusicController.playMusic(rs);
				}
		    }
		}
		else if(e.getSource() == btnStop){
			myMusicController.stopMusic();
			updateButtons();
		}
		else if(e.getSource()== btnSettings){

			SettingsController frame = new SettingsController(myMusicController, this);
			frame.StartGUI();
			frame.setVisible(true);

			this.setVisible(false);
		}
		else if(e.getSource()== btnStationList){
			StationsMenu frame = new StationsMenu(myMusicController, this);
			frame.setVisible(true);

			this.setVisible(false);
		}
		else if(e.getSource()== btnFavorites){
			FavoriteMenu frame = new FavoriteMenu(myMusicController, this);
			frame.setVisible(true);

			this.setVisible(false);
		}
		else if(e.getSource()== btnNext){
			int listSize = rhandler.getPreferredStations().size();
			int temp = 0;
			for(int x=0; x<listSize;x++){
				if(rhandler.getPreferredStations().get(x).equals(myMusicController.getCurrentRadiostation())){
					if(x==(listSize-1)){
						x = -1;
					}					
					temp = x;
					break;
				}
			}		
			myMusicController.playMusic(rhandler.getPreferredStations().get(temp+1));
		}
		else if(e.getSource()== btnPrev){			
			int listSize = rhandler.getPreferredStations().size();
			int temp = 0;
			for(int x=(listSize-1); x >= 0; x--){
				if(rhandler.getPreferredStations().get(x).equals(myMusicController.getCurrentRadiostation())){
					if(x==0){
						x = listSize;
					}					
					temp = x;
					break;
				}
			}		
			myMusicController.playMusic(rhandler.getPreferredStations().get(temp-1));
		}
	}
	
	/**
	 * updateButtons function to update tglBtnPlay
	 * 
	 * Updates tglBtnPlay selection state and text according to play state
	 */
	private void updateButtons()
	{
		if (myMusicController.getPlayState() == MusicController.PlayState.PLAYING)
		{
			tglBtnPlay.setSelected(true);
			tglBtnPlay.setText("Pause");
		}
		else if (myMusicController.getPlayState() == MusicController.PlayState.PAUSED)
		{
			tglBtnPlay.setSelected(false);
			tglBtnPlay.setText("Resume");
		}
		else
		{
			tglBtnPlay.setSelected(false);
			tglBtnPlay.setText("Play");
		}
	}
	
	/**
	 * Gets called by JSlider
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(!source.getValueIsAdjusting() && volumeChangedByUser){
			handler.setVolume(slVolume.getValue());
			volumeChangedByUser = true; //reset
		}
	}

    /**
     * This function is called when the rotary encoder value is changed
     */
	@Override
	public void rotaryValueChanged(int oldValue, int newValue) {
	}
	
	/**
	 * This function is called when the rotary encoder is turned clockwise
	 */
	@Override
	public synchronized void rotaryTurnedClockwise() {
		if(handler.getVolume() < slVolume.getMaximum()){
			volumeChangedByUser = true;
			slVolume.setValue(slVolume.getValue()+1);
			//DeviceHandler.sVOLUME = slVolume.getValue();
			//System.out.println("+ : " + slVolume.getValue());
		}
	}

	/**
	 * This function is called when the rotary encoder is turned counter clockwise
	 */
	@Override
	public synchronized void rotaryTurnedCounterclockwise() {
		if(handler.getVolume() > slVolume.getMinimum()){
			volumeChangedByUser = true;
			slVolume.setValue(slVolume.getValue()-1);
			//DeviceHandler.sVOLUME = slVolume.getValue();
			//System.out.println("- : " + slVolume.getValue());
		}
	}

	/**
	 * Gets called by DeviceHandler when the master volume is changed
	 * 
	 * @param volume The volume percentage. Range 0 - 100
	 */
	@Override
	public void volumeChanged(int volume) {
		volumeChangedByUser = false;
		slVolume.setValue(volume);
	}

	/**
	 * Gets called by MusicController when the radiostation is changed
	 * 
	 * @param station The new current radiostation
	 */
	@Override
	public void onChange(Radiostation station) {
		//radio station changed
		ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,myMusicController.getCurrentRadiostation().getImage()).getPath());
		jpImage.setPic(icon);
		jpImage.setVisible(true);
		updateButtons();
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
		myMusicController.removeListener(this);
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
	
}
	        

