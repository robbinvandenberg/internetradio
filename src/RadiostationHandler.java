import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * @file RadiostationHandler.java
 * 
 * @brief The RadiostationHandler contains database connectability to get radiostation from an external server. It has also 
 * favorite XML read and write functionality.
 */
public class RadiostationHandler {
	
	private File preferredXML;
	private File blacklistXML;
	/**
	 * Max saveable preferred radiostations
	 */
	public static final int MAX_PREFERRED_RADIOSTATIONS = 9;
	//private static final String DATABASE_CONNECTION = "jdbc:mysql://145.89.103.148/internetradio?user=radio&password=internetradio";
	//private static final String DATABASE_CONNECTION = "jdbc:mysql://localhost/internetradio?user=root&password=";
	private static final String DATABASE_CONNECTION = "http://muelders.nl/api.php?query=";
	//private static Connection connect = null;
	private PreparedStatement preparedStatement = null;
	//private ResultSet resultSet = null;
	

	public enum RadiostationError{
		LIST_FULL,
		CONTAINS,
		OK
	}
	
	/**
	 * 
	 * Constructor
	 * 
	 */
	public RadiostationHandler() {
		preferredXML = new File(DeviceHandler.getInstance().ExecutionPath, "favorites.xml");
		blacklistXML = new File(DeviceHandler.getInstance().ExecutionPath, "blacklist.xml");
	}
	/**
	 * Returns the list of preferred radiostations
	 * 
	 * @return ArrayList<RadioStation>
	 */
	public ArrayList<Radiostation> getPreferredStations() {
		ArrayList<Radiostation> sl = readXML(preferredXML);
		return sl;
	}
	
	/**
	 * 
	 * Adds a new radiostation to the preferred stations list and calls the
	 * method to write it to XML
	 * 
	 * Unusual placeholder for proper exception throwing is used as an experiment to deal with errors.
	 * The experiment did not go well.
	 * 
	 * @param Radiostation station
	 * @return RadiostationError
	 */
	public RadiostationError addPreferredStation(Radiostation station) {
		ArrayList<Radiostation> sl = getPreferredStations();
		if (sl.size() >= MAX_PREFERRED_RADIOSTATIONS)
			return RadiostationError.LIST_FULL;
		if (sl.contains(station))
			return RadiostationError.CONTAINS;
		sl.add(station);
		writeStationsToXml(preferredXML, sl);
		return RadiostationError.OK;
	}
	
	/**
	 * Removes a station from the preference list and calls the method to rewrite
	 * the XML file
	 * 
	 * @param station The radiostation to remove
	 * @return boolean True if successful
	 */
	public boolean removePreferredStation(Radiostation station) {
		ArrayList<Radiostation> sl = getPreferredStations();
		if (sl.contains(station)) 
		{
			sl.remove(station);
			writeStationsToXml(preferredXML, sl);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Saves the current list of preferred radiostations, provided new additions have been made
	 * 
	 * @param sl The radiostations to save
	 * @return boolean True if successful
	 */
	public boolean savePreferredStations(ArrayList<Radiostation> sl)
	{
		if (sl.size() > MAX_PREFERRED_RADIOSTATIONS)
			return false;
		writeStationsToXml(preferredXML, sl);
		return true;
	}
	
	/**
	 * Returns the blacklist
	 * 
	 * @return sl List of blacklisted radiostations
	 */
	public ArrayList<Radiostation> getBlacklistStations() {
		ArrayList<Radiostation> sl = readXML(blacklistXML);
		return sl;
	}
	
	/**
	 * Adds a station to the blacklist and calls the method to rewrite
	 * the XML file
	 * 
	 * @param station The radiostation to add to the blacklist
	 * @return boolean True if successful
	 */
	public boolean addBlacklistStation(Radiostation station) {
		ArrayList<Radiostation> sl = getBlacklistStations();
		if (sl.contains(station))
			return false;
		sl.add(station);
		writeStationsToXml(blacklistXML, sl);
		return true;
	}
	
	/**
	 * Saves the blacklist
	 * 
	 * @param rs
	 */
	public void saveBlacklistStations(ArrayList<Radiostation> rs)
	{
		writeStationsToXml(blacklistXML, rs);
	}
	
	/**
	 * Checks if the database is accessible
	 * 
	 * @return boolean True if successful
	 */
	public boolean checkDatabaseConnection()
	{
		return true;
		/*boolean result = false;
		try
		{
			connect = DriverManager.getConnection(DATABASE_CONNECTION);
			if (connect.isClosed())
			{
				result = false;
			}
			else
			{
				result = true;
			}
		}
		catch(Exception e)
		{
			result =  false;
		}
		finally
		{
			sqlClose();		
		}
		return result;*/
	}
	
	/**
	 * Executes a SELECT query on the COUNT of a given filter
	 * and returns the count
	 * 
	 * @param key
	 * @return int
	 */
	public int getFilteredCount(String key) {
		int count = -1;
		/*try {
			connect = DriverManager.getConnection(DATABASE_CONNECTION);

			// Statements allow to issue SQL queries to the database
			//statement = connect.createStatement();
			// Result set get the result of the SQL query
			preparedStatement = connect.prepareStatement("SELECT COUNT(DISTINCT " + key
					+ ") AS count FROM stations");
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				//System.out.println(Integer.toString(resultSet.getInt(1)));
				count = resultSet.getInt(1);
			}
		} catch (Exception e) {
		} finally
		{
			sqlClose();
		}*/
		return count;
	}
	
	/**
	 * Executes a SELECT query with a given key for the column name
	 * and returns the results
	 * 
	 * @param key
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getFilteredList(String key) {
		ArrayList<String> countries = new ArrayList<String>();
		/*try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(DATABASE_CONNECTION);

			// Statements allow to issue SQL queries to the database
			//statement = connect.createStatement();
			// Result set get the result of the SQL query
			preparedStatement = connect.prepareStatement("SELECT DISTINCT "
					+ key + " FROM stations");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				countries.add(resultSet.getString(key));
			}
		} catch (Exception e) {
		} finally {
			sqlClose();
		}*/
		JSONArray jsonArray = HTTPGetDataFromQuery("radiostations");
		for (Object object : jsonArray) {
			JSONObject jsonObject = new JSONObject(object);
			countries.add(jsonObject.getString(key));
		}

		return countries;
	}
	
	/**
	 * Executes an SQL query for the given radiostation id and returns
	 * the matching station
	 * 
	 * @param id
	 * @return Radiostation
	 */
	public Radiostation getRadiostation(int id) {
		ArrayList<Radiostation> stations = new ArrayList<Radiostation>();
		try {
			// This will load the MySQL driver, each DB has its own driver
			/*Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(DATABASE_CONNECTION);

			// Statements allow to issue SQL queries to the database
			//statement = connect.createStatement();
			// Result set get the result of the SQL query
			preparedStatement = connect
					.prepareStatement("SELECT * FROM stations WHERE id="
							+ Integer.toString(id));
			resultSet = preparedStatement.executeQuery();*/
			stations = buildRadiostations(HTTPGetDataFromQuery("radiostations"));

		} catch (Exception e) {
		} finally {
			//sqlClose();
		}
		if (stations.size() == 1)
		{
			return stations.get(0);
		}
		else
		{
			return null;
		}
	}
	
	// blacklisted radiostation don't get removed from this calculated count.
	/**
	 * Selects radiostations by the given filters and returns the count.
	 * This does not filter away blacklisted stations.
	 * 
	 * "Inclusive" parameter is used to set whether all a station has to conform to both filters or only one
	 * 
	 * @param map
	 * @param inclusive
	 * @return int
	 */
	public int getRadiostationsCount(HashMap<String, String> map, boolean inclusive) {
		int stationsCount = -1;
		try {
			/*connect = DriverManager.getConnection(DATABASE_CONNECTION);

			// Statements allow to issue SQL queries to the database
			//statement = connect.createStatement();
			// Result set get the result of the SQL query
			String st = "SELECT COUNT(*) FROM stations" + (map.entrySet().size() > 0 ? " WHERE " : "");
			int length = map.entrySet().size();
			int count = 0;
			for(Map.Entry<String, String> entry : map.entrySet()) {
				if(count == length-1) {
					st += entry.getKey() +  "=\"" + entry.getValue() + "\"";
				}
				else {
					st += entry.getKey() +  "=\"" + entry.getValue() + "\" " + (inclusive ? "AND " : "OR ");
				}
				count++;
				
			}
			//System.out.println("Size of map: " + map.entrySet().size());
			//System.out.println("Query: " + st);
			preparedStatement = connect.prepareStatement(st);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				//System.out.println(Integer.toString(resultSet.getInt(1)));
				stationsCount = resultSet.getInt(1);
			}*/
			HTTPGetDataFromQuery("radiostations").length();
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			//sqlClose();
		}
		return stationsCount;
	}
	
	/**
	 * Executes an SQL query to return the stations matching the given filters.
	 * 
	 * "Inclusive" parameter is used to set whether all a station has to conform to both filters or only one
	 *
	 * @return ArrayList<Radiostation>
	 */
	public ArrayList<Radiostation> getRadiostations() {
		ArrayList<Radiostation> stations = new ArrayList<Radiostation>();
		try {
			stations = buildRadiostations(HTTPGetDataFromQuery("radiostations"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stations;
	}
	
	/**
	 * Builds a list of radiostations from the ResultSet of a SQL query
	 * 
	 * @param resultSet
	 * @return Arraylist<Radiostation>
	 * @throws SQLException
	 */
	private ArrayList<Radiostation> buildRadiostations(JSONArray resultSet) throws SQLException {
		ArrayList<Radiostation> stations = new ArrayList<Radiostation>();
		ArrayList<Radiostation> blacklists = getBlacklistStations();
		// ResultSet is initially before the first data set
		for (int i = 0; i < resultSet.length(); ++i) {
			try {
				JSONObject jsonObject = resultSet.getJSONObject(i);
				URL streamURL = new URL(jsonObject.getString("url"));
				System.out.println(streamURL.toString());
				Radiostation station = new Radiostation(streamURL, jsonObject.getString("ID"),
						jsonObject.getString("name"), jsonObject.getString("genre"), jsonObject.getString("country"), jsonObject.getString("image"));
				if(blacklists.contains(station))
					continue;
				stations.add(station);
			} catch (MalformedURLException e){
				e.printStackTrace();
			}

		}
		/*while (resultSet.next()) {
			try {

				Radiostation station = new Radiostation(new URL(resultSet.getString("url")), resultSet.getString("ID"), 
						resultSet.getString("name"), resultSet.getString("genre"), resultSet.getString("country"), resultSet.getString("image"));
				if(blacklists.contains(station))
					continue;
				stations.add(station);
			} catch (Exception e) {
			}
		}*/
		return stations;
	}
	
	/**
	 * Closes the database connection, preparedStatement and resultSet
	 */
	/*private void sqlClose() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}*/
	
	/**
	 * Writes a list of Radiostations to an XML file using the DocumentBuilderFactory.
	 * 
	 * @param targetXML
	 * @param stations
	 */
	private void writeStationsToXml(File targetXML, ArrayList<Radiostation> stations) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Stations");
			doc.appendChild(root);
			for(Iterator<Radiostation> i = stations.iterator(); i.hasNext();) {
				Radiostation st = i.next();
				Element station = doc.createElement("Station");
				station.setAttribute("id", st.getId());
				station.setAttribute("name", st.getName());
				station.setAttribute("genre", st.getGenre());
				station.setAttribute("country", st.getCountry());
				station.setAttribute("img", st.getImage());
				station.setAttribute("source", st.getUrl().toString());
				root.appendChild(station);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(targetXML);
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			//System.out.println("File saved!");
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Reads out the given XML file. The SAX Parser is used to read out the file.
	 * The results are returned as a list of radiostations.
	 * 
	 * @param xmlPath
	 * @return ArrayList<Radiostation>
	 */
	private ArrayList<Radiostation> readXML(File xmlPath) {
		final ArrayList<Radiostation> stations = new ArrayList<Radiostation>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException {
			 
					if (qName.equalsIgnoreCase("STATION")) {					
						try {
							String id = attributes.getValue("id");
							String name = attributes.getValue("name");
							String genre = attributes.getValue("genre");
							String country = attributes.getValue("country");
							String source = attributes.getValue("source");
							String img = attributes.getValue("img");
							stations.add(new Radiostation(new URL(source), id, name, genre, country, img));
						} catch (MalformedURLException e) {
						}						
					}
				}
				
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				 				 
				}
				
			};
			saxParser.parse(xmlPath, handler);
			

		} catch (Exception e) {
		}
		return stations;
	}

	private JSONArray HTTPGetDataFromQuery(String query){
		JSONArray jsonArray = null;
		try {
			URL url = new URL(DATABASE_CONNECTION + query);
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			jsonArray = new JSONArray(responseStrBuilder.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonArray;
	}
}
