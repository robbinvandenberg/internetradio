package RadioPlayer;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * RadioPlayer.UI_Handler.java
 * The RadioPlayer.UI_Handler class.
 * 
 */
public class UI_Handler {
	
	/**
	 * @param final String menuID
	 * @return RadioPlayer.UI
	 * 
	 * This function reads out the layout of the given menu (indicated by the menuID parameter) from the layout.xml file
	 * present on the Beaglebone using the SAX parser.
	 * 
	 * RadioPlayer.UI_Element objects are constructed from the data in the XML file and added to an arraylist,
	 * which is used to construct the RadioPlayer.UI object.
	 * 
	 */	
	public static UI readLayout(final String menuID) {
		final ArrayList<UI_Element> UIlist = new ArrayList<UI_Element>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean correctMenu = false;
				String fontStyle;
				String fontWeight;
				String borderStyle;
				String label;
				String id;
				int xLength;
				int xPos;
				int yLength;
				int yPos;
				int fSize;


				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if(qName.equalsIgnoreCase(menuID)) {
						correctMenu = true;
					}
					if(correctMenu){
						if (qName.equalsIgnoreCase("ELEMENT")) {
							label = attributes.getValue("label");
							id = attributes.getValue("id");
						}
	
						if (qName.equalsIgnoreCase("X")) {
							xPos = Integer.parseInt(attributes.getValue("pos"));
							xLength = Integer.parseInt(attributes.getValue("length"));
						}
						if (qName.equalsIgnoreCase("Y")) {
							yPos = Integer.parseInt(attributes.getValue("pos"));
							yLength = Integer.parseInt(attributes.getValue("length"));
						}
						if (qName.equalsIgnoreCase("FONT")) {
							fontWeight = attributes.getValue("weight");
							fSize = Integer.parseInt(attributes.getValue("size"));
							fontStyle = attributes.getValue("style");
						}
						if (qName.equalsIgnoreCase("BORDER")) {
							borderStyle = attributes.getValue("style");
						}
					}
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("ELEMENT") && correctMenu == true) {
						UIlist.add(new UI_Element(xLength, xPos, yLength, yPos, fSize, fontStyle, 
											  fontWeight, label, "button", borderStyle, id));
					}
					if(qName.equalsIgnoreCase(menuID)) {
						correctMenu = false;
					}
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
				
				}

			};
			File xml = new File(DeviceHandler.getInstance().ExecutionPath, "layout.xml");
			saxParser.parse(xml, handler);

		} catch (Exception e) {
			System.out.println("UI_HANDLER EXCEPTION: " + e.toString());
			e.printStackTrace();
		}
		return new UI(UIlist);
	}
}