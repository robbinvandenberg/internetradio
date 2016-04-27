import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @file RadiostationWebservice.java
 * 
 * @brief The RadiostationWebservice contains database connectability to get radiostation from an external server. It has also
 * favorite XML read and write functionality.
 */
public class RadiostationWebservice {
	private static final String webserviceUri = "http://muelders.nl/api.php?query=";
	
	/**
	 * 
	 * Constructor
	 * 
	 */
	public RadiostationWebservice() {
	}

	/**
	 * Executes an SQL query to return the stations matching the given filters.
	 * 
	 * "Inclusive" parameter is used to set whether all a station has to conform to both filters or only one
	 *
	 * @return ArrayList<RadioStation>
	 */
	public ArrayList<RadioStation> getRadiostations() {
		ArrayList<RadioStation> stations = new ArrayList<RadioStation>();
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
	 * @return Arraylist<RadioStation>
	 * @throws SQLException
	 */
	private ArrayList<RadioStation> buildRadiostations(JSONArray resultSet) {
		ArrayList<RadioStation> stations = new ArrayList<RadioStation>();

		// ResultSet is initially before the first data set
		for (int i = 0; i < resultSet.length(); ++i) {
			try {
				JSONObject jsonObject = resultSet.getJSONObject(i);
				URL streamURL = new URL(jsonObject.getString("url"));
				RadioStation station = new RadioStation(
						jsonObject.getInt("ID"),
						jsonObject.getString("name"),
						jsonObject.getString("genre"),
						jsonObject.getString("country"),
						streamURL
				);
				stations.add(station);
			} catch (MalformedURLException e){
				//e.printStackTrace();
			}
		}
		return stations;
	}
	


	private JSONArray HTTPGetDataFromQuery(String query) throws IOException {
		URL url = new URL(webserviceUri + query);
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = streamReader.readLine()) != null) {
			responseStrBuilder.append(inputStr);
		}
		JSONArray jsonArray = new JSONArray(responseStrBuilder.toString());
		return jsonArray;
	}
}
