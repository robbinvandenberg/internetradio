package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import PreferenceAgent.Utils.DateUtils;
import RadioPlayer.RadioStation;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by Patrick on 24-5-2016.
 */
public class FavouritesFile {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static TransformerFactory transformerFactory;
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    private String filename;
    private Document document = null;
    private Element root = null;


    /**
     * Private constructor of Favorites file
     * @param filename The filename of the favorites file
     * @param document The document of the favorites file
     */
    private FavouritesFile(String filename, Document document) {
        this.filename = filename;
        this.document = document;
        root = document.getDocumentElement();
    }

    /**
     * Used to parse and load the Favorites file xml
     * @param filename The filename of the favorites file you want to load
     * @return Gives the loaded favorites file
     * @throws UnableToParseFavoritesFileException throws the favorites file exception
     */
    public static FavouritesFile load(final String filename) throws UnableToParseFavoritesFileException {

        try {

            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            transformerFactory = TransformerFactory.newInstance();

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();

            File file = new File(Constants.FILEPREFIX + Constants.PREFERENCE_DIR + filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            Document doc = documentBuilder.parse(file);

            if(!doc.getDocumentElement().getTagName().equals("Stations")){
                throw new IllegalArgumentException("root element 'Stations' not found in file.");
            }

            FavouritesFile favoritesFile = new FavouritesFile(filename, doc);

            return favoritesFile;

        } catch (SAXException e) {
            throw new UnableToParseFavoritesFileException(e);
        }  catch (IOException e) {
            throw new UnableToParseFavoritesFileException(e);
        } catch (ParserConfigurationException e) {
            throw new UnableToParseFavoritesFileException(e);
        } catch (TransformerConfigurationException e) {
            throw new UnableToParseFavoritesFileException(e);
        }
    }

    /**
     * Writes changes to the loaded xml file
     * @throws TransformerException
     */
    private void writeToFile() throws TransformerException {
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StreamResult result = new StreamResult(Constants.FILEPREFIX + filename);
        transformer.transform(source, result);
    }

    /**
     * Insert radiostation to DOM. Doesn't write to the xml file by itself
     * @param radioStation The radiostation you want to insert
     */
    private void insertRadioStation(RadioStation radioStation) {

        Element stationElement = document.createElement("Station");
        stationElement.setAttribute("id", Integer.toString(radioStation.getId()));
        stationElement.setAttribute("name", radioStation.getName());

        for(int i = 0; i < DateUtils.Day.values().length; ++i) {
            Element day = document.createElement(DateUtils.Day.values()[i].toString());
            day.appendChild(document.createTextNode("0"));
            stationElement.appendChild(day);
        }
       root.appendChild(stationElement);

        try {
            writeToFile();
        } catch(TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if radiostation exists
     * @param radioStation The radiostation you want to check
     * @return true if station is loaded from xml file
     */
    private boolean radioStationExists(RadioStation radioStation) {

        NodeList nodeList = root.getElementsByTagName("Station");

        for(int i = 0; i < nodeList.getLength(); ++i) {
            final int id = Integer.parseInt(nodeList.item(i).getAttributes().getNamedItem("id").getTextContent());

            if(radioStation.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the time a Radiostation has been listened to in minutes from a given day
     * @param radioStation The radiostation you want peek the time of
     * @param day The day of the radiostation you want to peek
     * @return the time listened in Long
     */
    public long getTime(RadioStation radioStation, DateUtils.Day day) {

        if(!radioStationExists(radioStation)) {
            insertRadioStation(radioStation);
        }

        NodeList nodeList = root.getElementsByTagName("Station");

        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);
            final int id = Integer.parseInt(currentNode.getAttributes().getNamedItem("id").getTextContent());

            if(radioStation.getId() == id) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element currentElement = (Element) nodeList.item(i);

                    return Long.parseLong(currentElement.getElementsByTagName(day.toString()).item(0).getTextContent());
                }
            }
        }
        return 0;
    }

    /**
     * Appends time to the given Radiostation on a given day
     * @param radioStation The target radiostation
     * @param day The target day
     * @param time The time you want to append
     * @throws TransformerException throws the tranformer exception
     */
    public void appendTime(RadioStation radioStation, DateUtils.Day day, final long time) throws TransformerException {

        if(!radioStationExists(radioStation)) {
            insertRadioStation(radioStation);
        }

        NodeList nodeList = root.getElementsByTagName("Station");

        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);
            final int id = Integer.parseInt(currentNode.getAttributes().getNamedItem("id").getTextContent());

            if(radioStation.getId() == id) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    final long currentTime = getTime(radioStation, day);
                    final long totalTime = currentTime + time;

                    Element currentElement = (Element) nodeList.item(i);

                    currentElement.getElementsByTagName(day.toString()).item(0).setTextContent(Long.toString(totalTime));
                }
            }
        }
        writeToFile();
    }

    /**
     * Gets total listen time of all stations
     * @param radioStation The target radio station you want the total time of
     * @return time in Long
     */
    public long getTotalTime(RadioStation radioStation) {
        long totalTime = 0;
        for(DateUtils.Day day : DateUtils.Day.values()) {
            totalTime += getTime(radioStation, day);
        }
        return totalTime;
    }
}
