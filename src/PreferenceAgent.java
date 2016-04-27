import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * PreferenceAgent.java
 * The PreferenceAgent class
 * 
 * This class is used to sort the favorite menu. The favorites are sorted from most played radiostations descending.
 */
public class PreferenceAgent extends Agent {
	private static final long serialVersionUID = 1L;
	private MainMenu mainMenu;
	
	/**
	 * The setup function.
	 * 
	 * This function is to set the instance of MainMenu and set the time interval of the tickerbehaviour.
	 */
	public void setup() {
		mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}
}