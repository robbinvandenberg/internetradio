package PreferenceAgent;

import PreferenceAgent.Exceptions.UnableToParseVolumeFileException;
import PreferenceAgent.Utils.DateUtils;
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
public class VolumeFile {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static TransformerFactory transformerFactory;
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    private String filename;
    private Document document = null;
    private Element root = null;

    /**
     * private constructor of VolumeFile
     * @param filename The filename of the volume file
     * @param document The document of the colume file
     */
    private VolumeFile(String filename, Document document) {
        this.filename = filename;
        this.document = document;
        root = document.getDocumentElement();
    }

    /**
     * Used to parse and load the Volume file xml
     * @param filename The filename of the volume file you want to load
     * @return gives the  loaded volume file
     * @throws UnableToParseVolumeFileException throws the unable to parse volume file exception
     */
    public static VolumeFile load(final String filename) throws UnableToParseVolumeFileException {

        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            transformerFactory = TransformerFactory.newInstance();

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();
            File file = new File(Constants.FILEPREFIX + filename);

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
     * Inserts standard data to the xml file
     */
    private void insertBlankVolumeContent() {
        for(int i = 0; i < DateUtils.Day.values().length; i++) {
            Element dayElement = document.createElement(DateUtils.Day.values()[i].toString());

            for(int j = 0; j < DateUtils.DayPart.values().length; j++){
                Element currentDayPart = document.createElement(DateUtils.DayPart.values()[j].toString());
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

    /**
     * Checks if a the given day exists in the loaded file
     * @param day The target day you want to check
     * @return true if given day exists
     */
    private boolean dayExists(DateUtils.Day day) {

        NodeList nodeList = root.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); ++i) {
            final String fileDay = nodeList.item(i).getNodeName();
            if(day.toString().equals(fileDay)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the volume that has been listened to at a given dayPart
     * @param day The target day
     * @param dayPart the target daypart you want the current volume of
     * @return gives the current volume of the selected daypart
     */
    public int getVolume(DateUtils.Day day, DateUtils.DayPart dayPart) {

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

    /**
     * Sets the volume at a given day and dayPart
     * @param day Target day
     * @param dayPart Target daypart you want to set the volume to
     * @param volume targtet volume you want to set
     * @throws TransformerException Throws the tranformer exception
     */
    public void setVolume(DateUtils.Day day, DateUtils.DayPart dayPart, final int volume) throws TransformerException {

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
