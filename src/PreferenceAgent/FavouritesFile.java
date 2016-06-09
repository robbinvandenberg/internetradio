package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseFavoritesFileException;
import RadioPlayer.RadioStation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

    public enum Day {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
    }

    private static DocumentBuilderFactory documentBuilderFactory;
    private static TransformerFactory transformerFactory;
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    private static final String filePrefix = "out/production/internetradio/";

    private String filename;
    private Document document = null;
    private Element root = null;

    private FavouritesFile(String filename, Document document) {
        this.filename = filename;
        this.document = document;
        root = document.getDocumentElement();
    }

    public static FavouritesFile load(final String filename) throws UnableToParseFavoritesFileException {

        try {

            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            transformerFactory = TransformerFactory.newInstance();

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();

            File file = new File(filePrefix + filename);

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

    private void writeToFile() throws TransformerException {
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StreamResult result = new StreamResult(filePrefix + filename);
        transformer.transform(source, result);
    }

    private void insertRadioStation(RadioStation radioStation) {

        Element stationElement = document.createElement("Station");
        stationElement.setAttribute("id", Integer.toString(radioStation.getId()));
        stationElement.setAttribute("name", radioStation.getName());

        for(int i =0;i < Day.values().length; ++i) {
            Element day = document.createElement(Day.values()[i].toString());
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

    public long getTime(RadioStation radioStation, Day day) {

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

    public void setTime(RadioStation radioStation, Day day, final long time) throws TransformerException {

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

    public long getTotalTime(RadioStation radioStation) {
        long totalTime = 0;
        for(Day day : Day.values()) {
            totalTime += getTime(radioStation, day);
        }
        return totalTime;
    }
}
