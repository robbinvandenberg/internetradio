import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * PreferenceAgentHandler.java
 * The PreferenceAgentHandler class
 * 
 * The function of this class is to read and write the preferenceXML. This xml contains data how long a radiostation
 * has been played.
 */
public class PreferenceAgentHandler {
	private File agentFavoriteXML;
	
	/**
	 * The PrefenceAgentHandler Constructor.
	 * 
	 * This constructor makes a instance of the file that can be read or written.
	 */
	public PreferenceAgentHandler()
	{
		agentFavoriteXML = new File(DeviceHandler.getInstance().ExecutionPath, "agentFavorites.xml");
	}
	
	/**
	 * The writeFavoriteToXml function.
	 * 
	 * This function is to write the data to the preferencedXML.
	 * 
	 * @param stations The data is given in a Hashmap with the types <String, Integer>. 
	 */
	public void writeFavoritesToXml(HashMap<String, Integer> stations) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Stations");
			doc.appendChild(root);
			for(Map.Entry<String, Integer> station : stations.entrySet()){
				Element el = doc.createElement("Station");
				el.setAttribute("id", station.getKey());
				el.setAttribute("time", station.getValue().toString());
				root.appendChild(el);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(agentFavoriteXML);
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * The readFavoriteToXML function.
	 * 
	 * This function is to read the data from the preferencedXML.
	 * 
	 * @return a Hashmap with the types <String, Integer>. Were the String is the name of the RadioStation
	 * and the Integer is the played time of this RadioStation.
	 */
	public HashMap<String, Integer> readFavoritesXML() {
		final HashMap<String, Integer> stations = new HashMap<String, Integer>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException {
			 
					if (qName.equalsIgnoreCase("STATION")) {					
						try {
							String id = attributes.getValue("id");
							String timeStr = attributes.getValue("time");
							int time = Integer.parseInt(timeStr);
							stations.put(id,  time);
						} catch (NumberFormatException e) {
						}						
					}
				}
				
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				 				 
				}
				
			};
			saxParser.parse(agentFavoriteXML, handler);
			

		} catch (Exception e) {
		}
		return stations;
	}
}
