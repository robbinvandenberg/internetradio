import sun.applet.Main;

import java.awt.Color;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * StationsMenu.java 
 * The StationsMenu class.
 * 
 * In this class the stations menu GUI is represented to the user
 */
public class StationsMenu extends JFrame implements ActionListener, MouseListener, DeviceHandlerListener, WindowListener{
	private static final long serialVersionUID = 1L;
	private JList<JListEntry> list;
	private JScrollPane scrollBar;
	private DefaultListModel<JListEntry> model;
	private JButton btnHome;
	private JButton btnBack;
	private JButton btnAddBlacklist;
	private JButton btnAddFav;
	private JButton btnPlay;
	private JLabel lblPath;
	private ImageIcon imageIcon;
	private JLabel imageLabel;
	// test array list
	private HashMap<String, String> filters = new HashMap<String, String>();
	private String currentFilterName = "";
	private Radiostation selectedRadiostation = null;
	private MusicController myMusicController;
	private MainMenu mainMenu;
	private DeviceHandler handler;
	private RadiostationHandler rHandler;
	
	/**
	 * The constructor of StationsMenu.
	 * 
	 * In this constructor the GUI frame of the stations menu is build
	 * 
	 * @param musicController The instance to the MusicController, required for controlling the music stream
	 */
	public StationsMenu(MusicController musicController, MainMenu mainMenu) {
		UI ui = UI_Handler.readLayout("StationsMenu");
		myMusicController = musicController;
		this.mainMenu = mainMenu;
		handler = DeviceHandler.getInstance();
		rHandler = new RadiostationHandler();
		filters.put("country", "");
		filters.put("genre", "");
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		
		UI_Element home = ui.getElementById("btnHome");
		btnHome = new JButton("Home");
		btnHome.setFont(home.getFont());
		btnHome.setBounds(home.getxPos(), home.getyPos(), home.getxLength(), home.getyLength());
		//btnHome.setFont(new Font("Tahoma", Font.BOLD, 14));
		//btnHome.setBounds(352, 0, 128, 32);
		btnHome.addActionListener(this);
		getContentPane().add(btnHome);
		
		UI_Element back = ui.getElementById("btnBack");
		btnBack = new JButton("Back");
		btnBack.setFont(back.getFont());
		btnBack.setBounds(back.getxPos(), back.getyPos(), back.getxLength(), back.getyLength());
		//btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		//btnBack.setBounds(0, 0, 128, 32);
		btnBack.addActionListener(this);
		getContentPane().add(btnBack);
		
		UI_Element scrollbar = ui.getElementById("scrollBar");
		model = new DefaultListModel<JListEntry>();
		list = new JList<JListEntry>(model);
		scrollBar = new JScrollPane(list);
		list.setFont(scrollbar.getFont());
		
		//list.setFont(new Font("Tahoma", Font.PLAIN, 24));
		list.setVisibleRowCount(6);
		//list.setValueIsAdjusting(true);
		//list.addListSelectionListener(this);
		list.addMouseListener(this);
		scrollBar.setBounds(scrollbar.getxPos(), scrollbar.getyPos(), scrollbar.getxLength(), scrollbar.getyLength());
		//scrollBar.setBounds(10, 40, 460, 189);
		getContentPane().add(scrollBar);
		
		UI_Element addFav = ui.getElementById("btnAddFav");
		btnAddFav = new JButton("Add to favorites");
		btnAddFav.setFont(addFav.getFont());
		btnAddFav.setBounds(addFav.getxPos(), addFav.getyPos(), addFav.getxLength(), addFav.getyLength());
		//btnAddFav.setFont(new Font("Tahoma", Font.BOLD, 14));
		//btnAddFav.setBounds(0, 240, 190, 32);
		btnAddFav.addActionListener(this);
		btnAddFav.setEnabled(false);
		getContentPane().add(btnAddFav);
		
		UI_Element addBlackList = ui.getElementById("btnAddBlacklist");
		btnAddBlacklist = new JButton("Add to blacklist");
		btnAddBlacklist.setFont(addBlackList.getFont());
		btnAddBlacklist.setBounds(addBlackList.getxPos(), addBlackList.getyPos(), addBlackList.getxLength(), addBlackList.getyLength());
		//btnAddBlacklist.setFont(new Font("Tahoma", Font.BOLD, 14));
		//btnAddBlacklist.setBounds(290, 240, 190, 32);
		btnAddBlacklist.addActionListener(this);
		btnAddBlacklist.setEnabled(false);
		getContentPane().add(btnAddBlacklist);
		
		UI_Element play = ui.getElementById("btnPlay");
		btnPlay = new JButton("Play");
		btnPlay.setFont(play.getFont());
		btnPlay.setBounds(play.getxPos(), play.getyPos(), play.getxLength(), play.getyLength());
		//btnPlay.setFont(new Font("Tahoma", Font.BOLD, 14));
		//btnPlay.setBounds(190, 240, 100, 32);
		btnPlay.addActionListener(this);
		btnPlay.setEnabled(false);
		getContentPane().add(btnPlay);
		
		UI_Element path = ui.getElementById("lblPath");
		lblPath = new JLabel("");
		lblPath.setFont(path.getFont());
		lblPath.setBounds(path.getxPos(), path.getyPos(), path.getxLength(), path.getyLength());
		//lblPath.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPath.setHorizontalAlignment(SwingConstants.LEFT);
		//lblPath.setBounds(140, 8, 200, 22);
		getContentPane().add(lblPath);
		
		UI_Element image = ui.getElementById("image2");
		imageIcon = new ImageIcon(new File(handler.DefaultImagePath,"PleaseWait.gif").getPath());
		imageLabel = new JLabel(imageIcon);
		imageLabel.setBounds(image.getxPos(), image.getyPos(), image.getxLength(), image.getyLength());
		//imageLabel.setBounds(10, 40, 460, 189);
		getContentPane().add(imageLabel);
		
		setLocationRelativeTo(null);
		setUndecorated(true);
		handler.addListener(this);
		this.addWindowListener(this);
		new ImageLoadingWorker().execute();
	}
	
	/**
	 * The removeUnusedFilters function.
	 * 
	 * The filters that has been set can be undone by this function.
	 * 
	 * @return returned a HashMap<String, String> with the filtered radiostations.
	 */
	private HashMap<String, String> removeUnusedFilters()
	{
		HashMap<String, String> filtered = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : filters.entrySet())
		{
			if (!entry.getValue().equals(""))
			{
				filtered.put(entry.getKey(), entry.getValue());
			}
		}
		return filtered;
	}
	
	/**
	 * The buildList function.
	 * 
	 * This function build the list of radiostations and show them in the GUI.
	 * 
	 * The function also managed that the search results are filtered if a filter has been set.
	 */
	private void buildList()
	{
		if (currentFilterName.equals(""))
		{
			//build filter menu
			model.clear();
			for (Map.Entry<String, String> entry : filters.entrySet())
			{
				JListEntry jlistEntry = new JListEntry(entry.getKey());
				model.addElement(jlistEntry);
			}
			int count = rHandler.getRadiostationsCount(removeUnusedFilters(), true);
			JListEntry jlistEntry = new JListEntry("results", "Show results (" + count + ")");
			model.addElement(jlistEntry);
			lblPath.setText("");
		}
		else if (currentFilterName.equals("results"))
		{
			//download all radiostations
			ArrayList<Radiostation> result = rHandler.getRadiostations(removeUnusedFilters(), true);
			System.out.println(result.toString());
			model.clear();
			for (Radiostation r : result)
			{
				model.addElement(new JListEntry(r));
			}
			lblPath.setText("Show results");
		}
		else
		{
			//download filterByKey
			ArrayList<String> result = rHandler.getFilteredList(currentFilterName);
			model.clear();
			for (String s : result)
			{
				model.addElement(new JListEntry(s));
			}
			lblPath.setText("Filter by: " + currentFilterName);
		}
	}
	
	/**
	 * Gets called by various GUI controls on action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnHome){
			//handler.removeListener(this);
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			mainMenu.setVisible(true);
		}
		else if (e.getSource() == btnBack)
		{
			if (currentFilterName.equals(""))
			{
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			else 
			{
				currentFilterName = "";
				buildList();
				setRadiostationButtonsEnabled(false);
			}
		}
		else if (e.getSource() == btnAddFav && selectedRadiostation != null)
		{
			RadiostationHandler.RadiostationError re = rHandler.addPreferredStation(selectedRadiostation);
			if (re == RadiostationHandler.RadiostationError.LIST_FULL)
			{
				JOptionPane.showMessageDialog(null, "Please remove a favorite before adding a new one!", "Favorite radiostation full", JOptionPane.ERROR_MESSAGE);
			}
			else if (re == RadiostationHandler.RadiostationError.CONTAINS)
			{
				JOptionPane.showMessageDialog(null,"Your favoritelist already contains: " + selectedRadiostation.getName() + "!", "Favorite radiostation contains", JOptionPane.WARNING_MESSAGE);
			}	
		}
		else if (e.getSource() == btnPlay && selectedRadiostation != null)
		{
			myMusicController.playMusic(selectedRadiostation);
		}
		else if (e.getSource() == btnAddBlacklist && selectedRadiostation != null)
		{
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to move this radiostation: " + selectedRadiostation.getName() + " to your blacklist?", "Add radiostation to blacklist", JOptionPane.INFORMATION_MESSAGE);
			
			if(answer == JOptionPane.YES_OPTION){
				rHandler.addBlacklistStation(selectedRadiostation);
            }
		}
	}

	/**
	 * This function is called when the rotary encoder value is changed
	 */
	@Override
	public void rotaryValueChanged(int oldValue, int newValue) {
	}

	/**
	 * The rotaryTurnedClockwise function.
	 * 
	 * Called when the rotary encoder is turned clockwise
	 * This has as effect that you can scroll downwards through the list of radiostations.
	 */
	@Override
	public synchronized void rotaryTurnedClockwise() {
		int index = list.getSelectedIndex();
		if (index == -1)
		{
			list.setSelectedIndex(0);
		}
		else if (index + 1 < list.getModel().getSize())
		{
			list.setSelectedIndex(index+1);
		}
		list.ensureIndexIsVisible(list.getSelectedIndex());
	}
	
	/**
	 * The rotaryTurnedCounterclockwise function.
	 * 
	 * Called when the rotary encoder is turned counter clockwise
	 * This has as effect that you can scroll upwards through the list of radiostations.
	 */
	@Override
	public synchronized void rotaryTurnedCounterclockwise() {
		int index = list.getSelectedIndex();
		if (index == -1)
		{
			list.setSelectedIndex(0);
		}
		else if (index > 0)
		{
			list.setSelectedIndex(index-1);
		}
		list.ensureIndexIsVisible(list.getSelectedIndex());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int index = list.locationToIndex(e.getPoint());
        if (index >= 0)
        {
        	JListEntry entry  = (JListEntry)list.getModel().getElementAt(index);
			//System.out.println(entry.getFilterName());
			if (!currentFilterName.equals(""))
			{
				//we have a filter selected and a item has been pressed.
				//Save the selected item and return to main menu.
				//possibilities are that we have selected a radio station or a filter
				if (currentFilterName.equals("results"))
				{
					//a radio station has been selected
					//System.out.println(entry.getStation().getName());
					selectedRadiostation = entry.getStation();
					setRadiostationButtonsEnabled(true);
				}
				else
				{
					filters.put(currentFilterName, entry.getFilterName());
					currentFilterName = "";
					buildList();
					setRadiostationButtonsEnabled(false);
				}
			}
			else
			{
				currentFilterName = entry.getFilterName();
				buildList();
				setRadiostationButtonsEnabled(false);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * The setRadiostationButtonEnabled function
	 * 
	 * This function set the clickable of the buttons:"add to favorite, play, add to blacklist".
	 * 
	 * @param state  this is the state of the buttons set to clickable or not clickable.
	 * @see mouseReleased()
	 */
	private void setRadiostationButtonsEnabled(boolean state)
	{
		btnAddFav.setEnabled(state);
		btnPlay.setEnabled(state);
		btnAddBlacklist.setEnabled(state);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		DeviceHandler.getInstance().removeListener(this);
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
	 * Inner classe JListEntry of the class StationsMenu.
	 * 
	 * This class is made so you can build easier the radio station list of the StationsMenu class.
	 */
	private class JListEntry {
		private String filtername;
		private String optionalText;
		private Radiostation station;
		
		public JListEntry(String filtername)
		{
			this.filtername = filtername;
		}
		
		public JListEntry(String filtername, String optionalText)
		{
			this.filtername = filtername;
			this.optionalText = optionalText;
		}
		
		public JListEntry(Radiostation r)
		{
			this.station = r;
		}
		
		public String getFilterName()
		{
			return filtername;
		}
		
		public Radiostation getStation()
		{
			return station;
		}
		
		@Override
		public String toString()
		{
			//We have no filter, so build main menu
			if (currentFilterName.equals(""))
			{
				if (optionalText != null)
				{
					return optionalText;
				}
				else 
				{
					if (filters.containsKey(filtername))
					{
						if (filters.get(filtername).equals(""))
						{
							return "Filter by " + filtername;
						}
						else
						{
							return "Filter by " + filtername + " (" + filters.get(filtername) + ")";
						}
					}
					else
					{
						return filtername;
					}
				}
			} 
			else
			{
				if (currentFilterName.equals("results") && station != null)
				{
					//we have results filter so we are returning radiostations
					return station.getName();
				}
				return filtername;
			}
		}
	}

	@Override
	public void volumeChanged(int volume) {		
	}
	
	/**
	 * Inner class ImageLoadingWorker of the class StationsMenu.
	 * 
	 * This class set a waiting.gif if the list isn't fully readed.
	 * If there is no connection with the database the GUI shows a pop-up with "connection to server failed".
	 */
	class ImageLoadingWorker extends SwingWorker<Boolean, Void> {

		public ImageLoadingWorker() {
		imageLabel.setVisible(true);
		scrollBar.setVisible(false);
		}

		// In the EDT
		@Override
		protected void done() {

		try {
			if(get() == false){
				JOptionPane.showMessageDialog(StationsMenu.this,
						"Connection to server failed!\nMake sure your internet connection is available.",
						"Connection to server failed",
						JOptionPane.ERROR_MESSAGE);
				handler.removeListener(StationsMenu.this);
				dispatchEvent(new WindowEvent(StationsMenu.this, WindowEvent.WINDOW_CLOSING));
			}
			else
			{
				imageLabel.setVisible(false);
				scrollBar.setVisible(true);
				buildList();
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		}

		// In a thread
		@Override
		public Boolean doInBackground() {
			return rHandler.checkDatabaseConnection();
		}
	}
}



