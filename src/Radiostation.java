import java.net.URL;


/**
 * @file Radiostation.java
 *
 * @brief      Radiostation data object
 * @details    The Radiostation class contains all the variables needed to connect to a given radiostation
 */

public class Radiostation {
	private URL rsStreamAddress;
	private String rsId;
	private String rsName;
	private String rsGenre;
	private String rsCountry;
	private String imageFileName = "";


	/**
	 * Constructor
	 * 
	 * @param StreamAddress
	 * @param id
	 * @param name
	 * @param genre
	 * @param country
	 * @param image
	 */
	public Radiostation(URL StreamAddress, String id, String name, String genre, String country, String image) {
		rsStreamAddress = StreamAddress;
		rsId = id;
		rsName = name;
		rsGenre = genre;
		rsCountry = country;
		imageFileName = image;
		//System.out.println("Selected radio station " + rsId +  " with: " + rsName + " from " + rsStreamAddress + " with genre " + rsGenre + ".");
	}
	
	/**
	 * Compares a radiostation with another object and returns whether they are equal
	 * 
	 * @param Object
	 * @return boolean
	 */
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Radiostation))
	    	return false;
	    Radiostation otherStation = (Radiostation)other;
	    if (otherStation.getId().equals(this.getId())) 
	    	return true;
	    else
	    	return false;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getId()
	{
		return rsId;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getName()
	{
		return rsName;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getGenre()
	{
		return rsGenre;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getCountry()
	{
		return rsCountry;
	}
	
	/**
	 * 
	 * @return URL
	 */
	public URL getUrl()
	{
		return rsStreamAddress;
	}
	
	/**
	 * Sets the ID
	 * 
	 * @param id
	 */
	public void setId(String id)
	{
		this.rsId = id;
	}
	
	/**
	 * Sets the name
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.rsName = name;
	}
	
	/**
	 * Sets the genre
	 * 
	 * @param genre
	 */
	public void setGenre(String genre)
	{
		this.rsGenre = genre;
	}
	
	/**
	 * Sets the country
	 * 
	 * @param country
	 */
	public void setCountry(String country)
	{
		this.rsCountry = country;
	}
	
	/**
	 * Sets the stream url
	 * 
	 * @param url
	 */
	public void setURL(URL url)
	{
		this.rsStreamAddress = url;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getImage() {
		return imageFileName;
	}
	/**
	 * Sets image filename
	 * 
	 * @param im
	 */
	public void setImage(String im) {
		imageFileName = im;
	}
}