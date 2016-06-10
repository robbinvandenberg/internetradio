package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseVolumeFileException;
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
public class VolumeFile {

    public enum DayPart {
        NIGHT,
        MORNING,
        AFTERNOON,
        EVENING,
    }

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

    private VolumeFile(String filename, Document document) {
        this.filename = filename;
        this.document = document;
        root = document.getDocumentElement();
    }

    public static VolumeFile load(final String filename) throws UnableToParseVolumeFileException {

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

            if(!doc.getDocumentElement().getTagName().equals("Volume")){
                throw new IllegalArgumentException("root element 'Volume' not found in file.");
            }

            VolumeFile favoritesFile = new VolumeFile(filename, doc);

            return favoritesFile;

        } catch (SAXException e) {
            throw new UnableToParseVolumeFileException(e);
        }  catch (IOException e) {
            throw new UnableToParseVolumeFileException(e);
        } catch (ParserConfigurationException e) {
            throw new UnableToParseVolumeFileException(e);
        } catch (TransformerConfigurationException e) {
            throw new UnableToParseVolumeFileException(e);
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

    private void insertBlankVolumeContent() {
        for(int i = 0; i < Day.values().length; i++) {
            Element dayElement = document.createElement(Day.values()[i].toString());

            for(int j = 0; j < DayPart.values().length; j++){
                Element currentDayPart = document.createElement(DayPart.values()[j].toString());
                currentDayPart.appendChild(document.createTextNode("50"));
                dayElement.appendChild(currentDayPart);
            }

            root.appendChild(dayElement);
        }

        try {
            writeToFile();
        } catch(TransformerException e) {
            e.printStackTrace();
        }
    }

    private boolean dayExists(Day day) {

        NodeList nodeList = root.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); ++i) {
            final String fileDay = nodeList.item(i).getNodeName();
            if(day.toString().equals(fileDay)) {
                return true;
            }
        }
        return false;
    }

    public int getVolume(Day day, DayPart dayPart) {

        if(!dayExists(day)) {
            insertBlankVolumeContent();
        }

        NodeList nodeList = root.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);

            final String fileDay = currentNode.getNodeName();

            if(day.toString().equals(fileDay)) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element currentElement = (Element) nodeList.item(i);

                    return Integer.parseInt(currentElement.getElementsByTagName(dayPart.toString()).item(0).getTextContent());
                }
            }
        }
        return 0;
    }

    public void setVolume(Day day, DayPart dayPart, final int volume) throws TransformerException {

        if(!dayExists(day)) {
            insertBlankVolumeContent();
        }

        NodeList nodeList = root.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);

            final String fileDay = currentNode.getNodeName();

            if(day.toString().equals(fileDay)) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    Element currentElement = (Element) nodeList.item(i);

                    currentElement.getElementsByTagName(dayPart.toString()).item(0).setTextContent(Integer.toString(volume));
                }
            }
        }
        writeToFile();
    }
}
