import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
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
		this.addBehaviour(new NowPlayingBehaviour(this, 1000));
	}
	
	/**
	 * The NowPlayingBehaviour inner class
	 * 
	 * This is an inner class of the PrefenceAgent class. This class have an extend to a TickerBehaviour that has been set in the
	 * setup function in the PrefenceAgent class.
	 */
	private class NowPlayingBehaviour extends TickerBehaviour implements RadiostationListener
	{
		private static final long serialVersionUID = 1L;
		private MusicController musicController;
		private int playTime = 0;
		private Radiostation playingRadiostation;
		
		/**
		 * The NowPlayingBehaviour constructor.
		 * 
		 * Retrieves MusicController instance from MainMenu
		 * 
		 * @param a The agent associated to the behaviour
		 * @param time The interval time to use.
		 */
		private NowPlayingBehaviour(Agent a,long time)
		{
			super(a, time);
			musicController = mainMenu.myMusicController;
			musicController.addListener(this);
		}
		
		/**
		 * The onTick function.
		 * 
		 * This function activates with a periodic timer that has been initialized in de update function.
		 * Further it checks the playstate. If it's playing the playTime increments with the periodic time. 
		 * Otherwise the playTime of the current radiostation is updated.
		 */
		@Override
		protected void onTick()
		{
			if (musicController.getPlayState() == MusicController.PlayState.PLAYING)
			{
				playTime += getPeriod() / 1000;
				playingRadiostation = musicController.getCurrentRadiostation();
			} 
			else if (musicController.getPlayState() == MusicController.PlayState.STOPPED)
			{
				if (playTime > 0 && musicController.getCurrentRadiostation() != null)
				{
					updateFavoriteXML(musicController.getCurrentRadiostation(), playTime);
				}
				playTime = 0;
			}
		}

		/**
		 * The onChange function
		 * 
		 * This is a function thats called when the current radiostation changed.
		 * 
		 * @param station The new radiostation.
		 */
		@Override
		public void onChange(Radiostation station) 
		{
			if (playTime > 0 && playingRadiostation != null)
			{
				updateFavoriteXML(playingRadiostation, playTime);
			}
			playTime = 0;
		}		
		
		/**
		 * The onEnd overridden agent function.
		 * 
		 * This function removes the listener.
		 */
		@Override
		public int onEnd()
		{
			musicController.removeListener(this);
			return super.onEnd();
		}
		
		/**
		 * The updateFavoriteXML function.
		 * 
		 * This function write the new playTime of a radiostation to the xml.
		 * 
		 * @param station The radiostation to write.
		 * @param time The time to write.
		 */
		private void updateFavoriteXML(Radiostation station, int time)
		{
			PreferenceAgentHandler handler = new PreferenceAgentHandler();
			HashMap<String, Integer> map = handler.readFavoritesXML();
			if (map.containsKey(station.getId()))
				time += map.get(station.getId());
			RadiostationHandler rHandler = new RadiostationHandler();
			ArrayList<Radiostation> stations = rHandler.getPreferredStations();
			//remove non favorite stations
			for(Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
		      Map.Entry<String, Integer> entry = it.next();
		      boolean found = false;
		      for (Radiostation iteratorStation : stations)
		      {
		    	  if (iteratorStation.getId().equals(entry.getKey()))
		    	  {
		    		  found = true;
		    	  }
		      }
		      if (!found)
		    	  it.remove();
		    }			
			map.put(station.getId(), time);
			handler.writeFavoritesToXml(map);
			stations = sortPreferredStations(stations, map);
			rHandler.savePreferredStations(stations);
		}
		
		/**
		 * The updateFavoriteXML function.
		 * 
		 * This function sort the incoming radiostations arraylist with the data of the incoming map.
		 * 
		 * @param stations The radiostations to sort
		 * @param map  Is a hashmap with the type <String, Integer>. This map contains the data to be used to sort the radiostations.
		 * @return Returns an sorted radiostation arraylist
		 * @see sortByValue()
		 */
		private ArrayList<Radiostation> sortPreferredStations(ArrayList<Radiostation> stations, HashMap<String, Integer> map)
		{
			System.out.println("sorting");
			Map<String, Integer> sortedMap = sortByValue(map);
			ArrayList<Radiostation> sortedStations = new ArrayList<Radiostation>();
			System.out.println("sorted map size: " + sortedMap.size());
			ArrayList<String> sortedKeys = new ArrayList<String>(sortedMap.keySet());
			for(String sortedKey : sortedKeys){
		        Radiostation foundStation = null;
				for (Radiostation station : stations)
				{
				  if (station.getId().equals(sortedKey)) //compare station id
				  {
					  System.out.println("Found station " + station.getId());
					  foundStation = station;
				  }
				}
				if (foundStation != null)
				{
					System.out.println("adding " + foundStation.getId());
					sortedStations.add(foundStation);
				}
			}
			for (Radiostation station : stations)
			{
				if (!sortedStations.contains(station)) {
					sortedStations.add(station);
					System.out.println("added non sorted station " + station.getId());
				}
			}
			return sortedStations;
		}
		
		/**
		 * The sortByValue function.
		 * 
		 * This function sort the incoming hashmap on the type Integer with the use of the class ValueComparator().
		 * 
		 * @param unsortedMap Is a hashmap with the type <String, Integer>. this is the unsorted hashmap with radiostations and their playtimes.
		 * @return Is a hashmap with the type <String, Integer>. This map is sorted of with the type Integer from hight to low.
		 * @see ValueComparator()
		 */
		private Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
			Map<String, Integer> sortedMap = new TreeMap<String, Integer>(new ValueComparator(unsortedMap));
			sortedMap.putAll(unsortedMap);
			return sortedMap;
		}
		
		/**
		 * The ValueComparator class
		 * 
		 * This class is needed so a hashmap with two types can be sorted as the way we want it to.
		 */
		private class ValueComparator implements Comparator<String> {
			Map<String, Integer> map;
			
			/**
			 * The ValueComparator constructor
			 * 
			 * This constructor
			 * 
			 * @param map Is a hashmap with the type <String, Integer>.
			 */
			public ValueComparator(Map<String, Integer> map) {
				this.map = map;
			}
			
			/**
			 * The compare function
			 * 
			 * This function is used to compare a valueA with a valueB
			 * 
			 * @param keyA  Is a String with the key of the map that will be compared with keyB.
			 * @param keyB  Is a String with the key of the map that will be compared with keyA.
			 * @return The return is the compare of valueB to valueA.
			 */
			public int compare(String keyA, String keyB) {
				Integer valueA = map.get(keyA);
				Integer valueB = map.get(keyB);
				return valueB.compareTo(valueA);
			}
		}
	}
}