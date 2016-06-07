package PreferenceAgent;

import RadioPlayer.RadioStation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Patrick on 24-5-2016.
 */
public class XMLManager {

    private static XMLManager instance = null;

    private static DocumentBuilderFactory documentBuilderFactory;
    private static TransformerFactory transformerFactory;
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    private static final String filePrefix = "out/production/internetradio/";
    private static final String filename = "radioStations.xml";
    private static final String DAYS[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private XMLManager() {

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static XMLManager getInstance() {
        if(instance == null) {
            instance = new XMLManager();
        }
        return instance;
    }

    private void writeToXML(Document doc) {
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filePrefix + filename);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public boolean createDefaultStationXML(List<RadioStation> radioStationList) {

        File file = new File(filePrefix + filename);

        if(!file.exists()) {
            try {
                file.createNewFile();
                Document doc = documentBuilder.newDocument();
                Element rootElement = doc.createElement("Stations");
                doc.appendChild(rootElement);


                for(RadioStation radioStation : radioStationList) {

                    Element stationElement = doc.createElement("station");
                    stationElement.setAttribute("id", Integer.toString(radioStation.getId()));
                    stationElement.setAttribute("name", radioStation.getName());

                    rootElement.appendChild(stationElement);

                    for(String day : DAYS) {
                        Element dayElement = doc.createElement("day");
                        dayElement.setAttribute("name", day);
                        dayElement.setAttribute("total", Integer.toString(0));
                        stationElement.appendChild(dayElement);

                        Element timeElement = doc.createElement("time");
                        timeElement.setAttribute("end", Integer.toString(0));
                        timeElement.setAttribute("begin", Integer.toString(0));
                        dayElement.appendChild(timeElement);
                    }
                }

                writeToXML(doc);

            }catch(Exception e) {
                return false;
            }
        }
        return true;
    }

    public boolean updateRadiostation(RadioStation radioStation, final int listeningTime, final int beginTime, final int endTime, final String day) {

        try {
            //Document doc = documentBuilder.parse(filePrefix + filename);
            Document doc = documentBuilder.parse(new InputSource(new FileReader(new File(filePrefix + filename))));

            int currentPosition = 0;

            NodeList nodeList = doc.getElementsByTagName("station");
            for(int i = 0; i < nodeList.getLength(); i++) {
                String id = nodeList.item(i).getAttributes().getNamedItem("id").getTextContent();

                if(id.equals(Integer.toString(radioStation.getId()))) {

                    currentPosition = i;

                    NodeList childNodes = nodeList.item(currentPosition).getChildNodes();
                    for(int j = 0; j < childNodes.getLength(); j++){
                        if (childNodes.item(j) instanceof Element == false)
                            continue;
                        Node name = childNodes.item(j).getAttributes().getNamedItem("name");
                        System.out.println(listeningTime);
                        System.out.println(day);
                        if(name.getTextContent().equals(day)){
                            childNodes.item(j).getAttributes().getNamedItem("total").setTextContent(Integer.toString(listeningTime));
                            break;
                        }
                    }
                }
            }
            writeToXML(doc);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
