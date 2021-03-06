package ProductAgent;

import PreferenceAgent.Constants;
import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Bart on 12-5-2016.
 *
 * Class for easy reading and writing to component files. Changes are only committed to the componentfile if storwriteToFile() is called.
 */
public class ComponentFile {

    private static final String filePrefix = Constants.FILEPREFIX;
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String fileName;
    private Document document;
    private Element root;

    /**
     * Constructor of ComponentFile.
     *
     * @param fileName path to file.
     * @param document DOM object containing the component elemeonts.
     */
    private ComponentFile(String fileName, Document document){
        this.fileName = fileName;
        this.document = document;
        root = document.getDocumentElement();
    }

    /**
     * Get component name.
     *
     * @return name of component.
     */
    public String getComponentName(){
        return root.getElementsByTagName("name").item(0).getTextContent();
    }

    /**
     * Get component installation date.
     *
     * @return date of installation.
     */
    public Date getInstallDate(){
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date timeStamp = null;
        try {
            timeStamp = formatter.parse(root.getElementsByTagName("installDate").item(0).getTextContent());
        } catch (ParseException e) {
            return null;
        }
        return timeStamp;
    }

    /**
     * writes the current datetime as installation date to file.
     */
    private void setInstallDate() throws UnableToStoreComponentFileException {
        Format formatter = new SimpleDateFormat(dateTimeFormat);

        root.getElementsByTagName("installDate").item(0).setTextContent(formatter.format(new Date()));

        try {
            writeToFile();
        } catch (TransformerException e) {
            throw new UnableToStoreComponentFileException(e);
        }

        addToLog(new Log(new Date(), "Installment date has been stored."));
    }

    /**
     * Get mileage of component.
     *
     * @return mileage of component in seconds.
     */
    public long getMileage(){
        return Long.parseLong(root.getElementsByTagName("mileage").item(0).getTextContent());
    }

    /**
     * Adds the given amount of seconds to the mileage.
     *
     * @param seconds amount of seconds to add to mileage.
     */
    public void addMileage(long seconds) throws UnableToStoreComponentFileException {
        long newMileage = getMileage() + seconds;

        Element mileageElement = (Element) root.getElementsByTagName("mileage").item(0);

        mileageElement.setTextContent(""+newMileage);

        try {
            writeToFile();
        } catch (TransformerException e) {
            throw new UnableToStoreComponentFileException(e);
        }

        addToLog(new Log(new Date(), "" + seconds + " seconds has been added to mileage."));
    }

    /**
     * Get the current componentstatus.
     *
     * @return componentstatus stored in the component file.
     */
    public ComponentStatus getStatus(){
        String status = root.getElementsByTagName("status").item(0).getTextContent();
        if(status.equals(ComponentStatus.Ok.toString())){
            return ComponentStatus.Ok;
        }
        else if(status.equals(ComponentStatus.Broken.toString())){
            return ComponentStatus.Broken;
        }
        else{
            return ComponentStatus.Unknown;
        }
    }

    /**
     * Writes the given componentstatus to the componentfile
     *
     * @param status status which should be written to the componentfile
     */

    public void setComponentStatus(ComponentStatus status) throws UnableToStoreComponentFileException {
        Element statusElement = (Element) root.getElementsByTagName("status").item(0);

        statusElement.setTextContent(status.toString());

        try {
            writeToFile();
        } catch (TransformerException e) {
            throw new UnableToStoreComponentFileException(e);
        }
    }


    /**
     * Load replacement steps from file.
     *
     * @return Vector containing replacementsteps.
     */
    public Vector<Step> getReplacementSteps(){
        Vector<Step> replacementSteps = new Vector<Step>();

        NodeList replacementStepsList = root.getElementsByTagName("replacementSteps").item(0).getChildNodes();
        for (int i = 0; i < replacementStepsList.getLength(); i++) {
            if (replacementStepsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element stepElement = (Element) replacementStepsList.item(i);

                String title = stepElement.getElementsByTagName("title").item(0).getTextContent();
                String description = stepElement.getElementsByTagName("description").item(0).getTextContent();
                Vector<Attachment> attachments = new Vector<Attachment>();

                NodeList attachmentsList = stepElement.getElementsByTagName("attachments").item(0).getChildNodes();
                for (int a = 0; a < attachmentsList.getLength(); a++) {
                    if (attachmentsList.item(a).getNodeType() == Node.ELEMENT_NODE) {
                        Element attachmentElement = (Element) attachmentsList.item(a);

                        String name = attachmentElement.getElementsByTagName("name").item(0).getTextContent();

                        //TODO betere fix voor deze 'hack'?
                        String path = filePrefix + fileName;
                        String[] bits = fileName.split("/");
                        String absoluteFileName = bits[bits.length-1];
                        path = path.substring(0, path.length() - absoluteFileName.length());

                        String fName = path + attachmentElement.getElementsByTagName("fileName").item(0).getTextContent();

                        attachments.add(new Attachment(name, fName));
                    }
                }
                replacementSteps.add(new Step(title, description, attachments));
            }
        }

        return replacementSteps;
    }

    /**
     * Add given log to log section in componentfile.
     *
     * @param log Log which should be stored.
     */
    public void addToLog(Log log) throws UnableToStoreComponentFileException {
        Element logsElement = (Element) root.getElementsByTagName("logs").item(0);

        Format formatter = new SimpleDateFormat(dateTimeFormat);

        Element timestamp = document.createElement("timestamp");
        timestamp.appendChild(document.createTextNode(formatter.format(log.getTimeStamp())));

        Element message = document.createElement("message");
        message.appendChild(document.createTextNode(log.getMessage()));

        Element logElement = document.createElement("log");
        logElement.appendChild(timestamp);
        logElement.appendChild(message);

        logsElement.appendChild(document.createTextNode("    "));
        logsElement.appendChild(logElement);
        logsElement.appendChild(document.createTextNode("\n    "));


        try {
            writeToFile();
        } catch (TransformerException e) {
            throw new UnableToStoreComponentFileException(e);
        }
    }

    /**
     * Get component logs.
     *
     * @return logs from componentfile.
     */
    public Vector<Log> getLogs() throws UnableToParseComponentFileException {
        try {
            Vector<Log> logs = new Vector<Log>();

            NodeList logsList = root.getElementsByTagName("logs").item(0).getChildNodes();
            for (int i = 0; i < logsList.getLength(); i++) {
                if (logsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element logElement = (Element) logsList.item(i);

                    SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
                    Date timeStamp = formatter.parse(logElement.getElementsByTagName("timestamp").item(0).getTextContent());

                    String message = logElement.getElementsByTagName("message").item(0).getTextContent();

                    logs.add(new Log(timeStamp, message));
                }
            }

            Collections.reverse(logs);
            return logs;

        } catch (ParseException e) {
            throw new UnableToParseComponentFileException(e);
        }
    }

    /**
     * Writes the DOM document to the component file.
     */
    private void writeToFile() throws TransformerException {
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        StreamResult result = new StreamResult(filePrefix + fileName);
        transformer.transform(source, result);
    }

    /**
     * Static method for creating a ComponentFile object from a given filepath.
     *
     * @param fileName path to componentfile.
     */
    public static ComponentFile Load(String fileName) throws UnableToParseComponentFileException {
        try {
            File componentFile = new File(filePrefix + fileName);
            DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuildFactory.newDocumentBuilder();
            Document componentDocument = documentBuilder.parse(componentFile);

            if(!componentDocument.getDocumentElement().getTagName().equals("component")){
                throw new IllegalArgumentException("root element 'component' not found in file.");
            }

            ComponentFile file = new ComponentFile(fileName, componentDocument);

            if(file.getInstallDate() == null){
                try {
                    file.setInstallDate();
                } catch (UnableToStoreComponentFileException e) {
                }
            }

            return file;

        } catch (SAXException e) {
            throw new UnableToParseComponentFileException(e);
        } catch (ParserConfigurationException e) {
            throw new UnableToParseComponentFileException(e);
        } catch (IOException e) {
            throw new UnableToParseComponentFileException(e);
        }
    }
}
