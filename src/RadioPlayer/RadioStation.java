package RadioPlayer;

import java.net.URL;


/**
 * @file RadioPlayer.RadioStation.java
 *
 * @brief      RadioPlayer.RadioStation data object
 * @details    The RadioPlayer.RadioStation class contains all the variables needed to connect to a given radiostation
 */

public class RadioStation {
	private int id;
	private String name;
	private String genre;
	private String country;
	private URL streamUrl;


	/**
	 * Constructor
	 *
	 * @param id
	 * @param name
	 * @param genre
	 * @param country
	 */
	public RadioStation(int id, String name, String genre, String country, URL streamUrl) {
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.country = country;
		this.streamUrl = streamUrl;
	}
	
	/**
	 * Compares a radiostation with another object and returns whether they are equal
	 *
	 * @return boolean
	 */
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof RadioStation))
	    	return false;
	    RadioStation otherStation = (RadioStation)other;
	    if (otherStation.getId() == id)
	    	return true;
	    else
	    	return false;
	}
	
	/**
	 * 
	 * @return String
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getGenre()
	{
		return genre;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 *
	 * @return URL
	 */
	public URL getStreamUrl()
	{
		return streamUrl;
	}
}