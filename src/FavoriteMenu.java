import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** 
 * FavoriteMenu.java
 * The FavoriteMenu class.
 * 
 * In this class the favorite menu GUI is represented to the user
 */
public class FavoriteMenu extends JFrame implements ActionListener, MouseListener, ChangeListener, DeviceHandlerListener{
	private static final long serialVersionUID = 1L;
	private DeviceHandler handler;
	private RadiostationHandler rHandler;
	private ArrayList<Radiostation> list = new ArrayList<Radiostation>();
	private ArrayList<ImagePanel> pFlist = new ArrayList<ImagePanel>();
	
	private int selectedStation;

	private ImagePanel pPlayedImage;
	private JToggleButton tglBtnPlay;
	private JButton btnStop;
	private JSlider slVolume;
	private JButton btnHome;
	private JButton btnRemove;
	private boolean volumeChangedByUser = true;
	
	private MusicController myMusicController;
	private MainMenu mainMenu;
	
	/**
	 * Constructor
	 * 
	 * Builds GUI
	 * 
	 * @param musicController The instance to the MusicController, required for controlling the music stream
	 */
	public FavoriteMenu(MusicController musicController, MainMenu mainMenu) {
		UI ui = UI_Handler.readLayout("FavoriteMenu");
		handler = DeviceHandler.getInstance();
		rHandler = new RadiostationHandler();
		list = rHandler.getPreferredStations();
		selectedStation = -1;
		myMusicController = musicController;
		this.mainMenu = mainMenu;
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		//Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		  //  cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		//getContentPane().setCursor(blankCursor);
		
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		
		for(int i = 0; i < 9; i++){
			pFlist.add(new ImagePanel(new File(handler.DefaultImagePath,"noimage.png").getPath()));
			pFlist.get(i).setBorder(new MatteBorder(1, 2, 3, 2, (Color) new Color(0, 0, 0)));			
			pFlist.get(i).setVisible(false);
			pFlist.get(i).addMouseListener(this);				
		}
		pFlist.get(0).setBounds(206, 11, 81, 81);
		pFlist.get(1).setBounds(298, 11, 81, 81);
		pFlist.get(2).setBounds(389, 11, 81, 81);
		pFlist.get(3).setBounds(206, 96, 81, 81);
		pFlist.get(4).setBounds(298, 96, 81, 81);
		pFlist.get(5).setBounds(389, 96, 81, 81);
		pFlist.get(6).setBounds(206, 182, 81, 81);
		pFlist.get(7).setBounds(298, 182, 81, 81);
		pFlist.get(8).setBounds(389, 182, 81, 81);
		
		for(int x=0; x<9;x++){
			getContentPane().add(pFlist.get(x));	
		}	
		
		favImages();
		
		pPlayedImage = new ImagePanel("");
		pPlayedImage.setBorder(new MatteBorder(1, 2, 3, 2, (Color) new Color(0, 0, 0)));
		pPlayedImage.setBounds(10, 11, 81, 81);
		pPlayedImage.setVisible(true);
		if(myMusicController.getCurrentRadiostation() != null){
			ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,myMusicController.getCurrentRadiostation().getImage()).getPath());
			pPlayedImage.setPic(icon);
		}
		else
		{
			ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,"noradiostationselected.png").getPath());
			pPlayedImage.setPic(icon);
		}
		getContentPane().add(pPlayedImage);
		
	    UI_Element play = ui.getElementById("tglBtnPlay");
		tglBtnPlay = new JToggleButton("Play");
		//tglBtnPlay.setFont(new Font("Tahoma", Font.BOLD, 10));
		tglBtnPlay.setFont(play.getFont());
		//tglBtnPlay.setBounds(10, 96, 81, 38);
		tglBtnPlay.setBounds(play.getxPos(), play.getyPos(), play.getxLength(), play.getyLength());
		tglBtnPlay.addActionListener(this);
		getContentPane().add(tglBtnPlay);
		
		UI_Element stop = ui.getElementById("btnStop");
		btnStop = new JButton("Stop");
		//btnStop.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnStop.setFont(stop.getFont());
		//btnStop.setBounds(10, 139, 81, 38);
		btnStop.setBounds(stop.getxPos(), stop.getyPos(), stop.getxLength(), stop.getyLength());
		btnStop.addActionListener(this);
		getContentPane().add(btnStop);
		
		UI_Element vol = ui.getElementById("slVolume2");
		slVolume = new JSlider();
		//slVolume.setFont(new Font("Tahoma", Font.BOLD, 10));
		slVolume.setFont(vol.getFont());
		slVolume.setOrientation(SwingConstants.VERTICAL);
		//slVolume.setMinimumSize(new Dimension(36, 43));
		//slVolume.setMaximumSize(new Dimension(32767, 43));
		slVolume.setMinorTickSpacing(1);
		slVolume.setSnapToTicks(true);
		slVolume.setPaintTicks(true);
		slVolume.setPaintLabels(true);
		slVolume.setMajorTickSpacing(10);
		slVolume.setBackground(new Color(224, 255, 255));
		slVolume.setForeground(new Color(0, 0, 0));
		slVolume.setBorder(new MatteBorder(1, 2, 3, 2, (Color) new Color(0, 0, 0)));
		//slVolume.setBounds(101, 11, 95, 252);
		slVolume.setBounds(vol.getxPos(), vol.getyPos(), vol.getxLength(), vol.getyLength());
		getContentPane().add(slVolume);
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
		
		UI_Element home = ui.getElementById("btnHome");
		btnHome = new JButton("Home");
		btnHome.setFont(home.getFont());
		btnHome.setBounds(home.getxPos(), home.getyPos(), home.getxLength(), home.getyLength());
		//btnHome.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnHome.setBounds(10, 222, 81, 38);
		btnHome.addActionListener(this);	
		getContentPane().add(btnHome);
		
		UI_Element remove = ui.getElementById("btnRemove");		
		btnRemove = new JButton("Remove");
		btnRemove.setFont(remove.getFont());
		//btnRemove.setFont(new Font("Tahoma", Font.BOLD, 10));
		//btnRemove.setBounds(10, 180, 81, 38);
		btnRemove.setBounds(remove.getxPos(), remove.getyPos(), remove.getxLength(), remove.getyLength());
		btnRemove.addActionListener(this);
		getContentPane().add(btnRemove);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		updateButtons();
		handler.addListener(this); //listen to events for rotaryencoder (not used) and volumechanged (used)
	}
	
	/**
	 * Gets called by various GUI controls on action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == tglBtnPlay){
			Radiostation rs = null;		
			if (tglBtnPlay.isSelected() == true){
				if(selectedStation == -1){
					ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,"noradiostationselected.png").getPath());
					pPlayedImage.setPic(icon);
					//System.out.println("No radiostation selected");
				}
				else{
					rs = list.get(selectedStation);
					ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,list.get(selectedStation).getImage()).getPath());
					pPlayedImage.setPic(icon);
					myMusicController.playMusic(rs);
					//System.out.println("Selected station: " + selectedStation);
				}								
			}
			else{
				myMusicController.pauseMusic();
			}
			updateButtons();
		}
		else if(e.getSource() == btnStop){
			myMusicController.stopMusic();
			updateButtons();
		}
		else if(e.getSource() == btnHome){
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			mainMenu.setVisible(true);
		}
		else if(e.getSource() == btnRemove){
			Radiostation rs;	
			if(selectedStation != -1){
				rs = list.get(selectedStation);
				String message = "Are you sure?";
				String title = "Remove: " + rs.getName();
				int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					//JOptionPane.showMessageDialog(null, rs.getName() + " deleted.");
					rHandler.removePreferredStation(rs);
					favImages();
		        }
		        else {
		        	JOptionPane.showMessageDialog(null, "Nothing deleted.");
		        }
			}
			else{
				JOptionPane.showMessageDialog(null, "Please select a radiostation to\nremove it from your favorites.");
			}
		}
	}
	
	/**
	 * The updateButtons function.
	 * 
	 * In this function the @tglBtnPlay text and state of the button will be changed according to play state
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
	
	@Override
	public void mouseClicked(MouseEvent e) {	
	}
	
	/**
	 * mousePressed event by pFlist
	 * 
	 * Occures when mouse is clicked in pFlist
	 * 
	 * @param e The MouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		for(int x=0;x<list.size();x++){
			if(e.getSource() == pFlist.get(x)){
				selectedStation = x;
				Radiostation rs;	
				rs = list.get(selectedStation);
				ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,list.get(selectedStation).getImage()).getPath());
				pPlayedImage.setPic(icon);
				myMusicController.playMusic(rs);
				updateButtons();
			}	
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	 * The favImages function.
	 * 
	 * This function set the favorite images.
	 */
	public void favImages(){
		list = rHandler.getPreferredStations();
		for(int x=0; x < 9;x++){
			if(x < list.size()){
				ImageIcon icon = new ImageIcon(new File(handler.DefaultImagePath,list.get(x).getImage()).getPath());
				pFlist.get(x).setPic(icon);	
				pFlist.get(x).setVisible(true);
				//System.out.println("setted image at index " + x);
			}
			else{
				
				pFlist.get(x).setPic(null);
				pFlist.get(x).setVisible(false);
				//System.out.println("setted null at index " + x);
			}
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
	 * Called when the rotary encoder is turned clockwise
	 * 
	 * This function increases the volume slider value.
	 */
	@Override
	public synchronized void rotaryTurnedClockwise() {
		if(handler.getVolume() < slVolume.getMaximum()){
			volumeChangedByUser = true;
			slVolume.setValue(slVolume.getValue()+1);
		}
	}

	/**
	 * Called when the rotary encoder is turned counter clockwise
	 * 
	 * This function decreases the volume slider value.
	 */
	@Override
	public synchronized void rotaryTurnedCounterclockwise() {
		if(handler.getVolume() > slVolume.getMinimum()){
			volumeChangedByUser = true;
			slVolume.setValue(slVolume.getValue() - 1);
		}
	}

	/**
	 * Gets called by DeviceHandler when the master volume is changed
	 * 
	 * This function changes the slider volume.
	 * 
	 * @param volume The volume percentage. Range 0 - 100
	 */
	@Override
	public void volumeChanged(int volume) {
		volumeChangedByUser = false;
		slVolume.setValue(volume);
	}
}
