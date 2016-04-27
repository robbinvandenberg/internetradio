import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * StationsMenu.java 
 * The StationsMenu class.
 * 
 * In this class the stations menu GUI is represented to the user
 */
public class StationsMenu extends JFrame implements ActionListener, MouseListener, WindowListener{
	private static final long serialVersionUID = 1L;

	private JList<JListEntry> radioStationList;
	private JScrollPane scrollBar;
	private DefaultListModel<JListEntry> model;
	private JButton backButton;
	private JButton playButton;
	private RadioStation selectedRadiostation = null;
	private MainMenu mainMenu;
	private RadiostationWebservice radioStationWebservice;
	
	/**
	 * The constructor of StationsMenu.
	 * 
	 * In this constructor the GUI frame of the stations menu is build
	 * 
	 * @param musicController The instance to the MusicController, required for controlling the music stream
	 */
	public StationsMenu(MainMenu mainMenu) {
		UI ui = UI_Handler.readLayout("StationsMenu");
		this.mainMenu = mainMenu;
		radioStationWebservice = new RadiostationWebservice();

		
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		UI_Element back = ui.getElementById("btnBack");
		backButton = new JButton("Back");
		backButton.setFont(back.getFont());
		backButton.setBounds(back.getxPos(), back.getyPos(), back.getxLength(), back.getyLength());
		backButton.addActionListener(this);
		getContentPane().add(backButton);
		
		UI_Element scrollbar = ui.getElementById("scrollBar");
		model = new DefaultListModel<JListEntry>();
		radioStationList = new JList<JListEntry>(model);
		scrollBar = new JScrollPane(radioStationList);
		radioStationList.setFont(scrollbar.getFont());

		radioStationList.setVisibleRowCount(6);
		radioStationList.addMouseListener(this);
		scrollBar.setBounds(scrollbar.getxPos(), scrollbar.getyPos(), scrollbar.getxLength(), scrollbar.getyLength());
		getContentPane().add(scrollBar);
		
		UI_Element play = ui.getElementById("btnPlay");
		playButton = new JButton("Play");
		playButton.setFont(play.getFont());
		playButton.setBounds(play.getxPos(), play.getyPos(), play.getxLength(), play.getyLength());
		playButton.addActionListener(this);
		playButton.setEnabled(false);
		getContentPane().add(playButton);

		this.addWindowListener(this);
	}
	
	/**
	 * The buildList function.
	 * 
	 * This function build the radioStationList of radiostations and show them in the GUI.
	 * 
	 * The function also managed that the search results are filtered if a filter has been set.
	 */
	private void buildList()
	{
			//download all radiostations
			ArrayList<RadioStation> result = radioStationWebservice.getRadiostations();
			System.out.println(result.toString());
			model.clear();
			for (RadioStation r : result)
			{
				JListEntry jlistEntry = new JListEntry(r);
				model.addElement(jlistEntry);
			}
	}
	
	/**
	 * Gets called by various GUI controls on action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton)
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			mainMenu.setVisible(true);setRadiostationButtonsEnabled(false);

		}
		else if (e.getSource() == playButton && selectedRadiostation != null)
		{
			mainMenu.setRadioStation(selectedRadiostation);
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int index = radioStationList.locationToIndex(e.getPoint());
        if (index >= 0)
        {
        	JListEntry entry  = (JListEntry) radioStationList.getModel().getElementAt(index);
			selectedRadiostation = entry.getStation();
			setRadiostationButtonsEnabled(true);
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
		playButton.setEnabled(state);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		buildList();
	}

	@Override
	public void windowClosing(WindowEvent e) {
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
	 * This class is made so you can build easier the radio station radioStationList of the StationsMenu class.
	 */
	private class JListEntry {
		private RadioStation station;

		
		public JListEntry(RadioStation r)
		{
			this.station = r;
		}
		
		public RadioStation getStation()
		{
			return station;
		}
		
		@Override
		public String toString()
		{
			return station.getName();
		}
	}
}



