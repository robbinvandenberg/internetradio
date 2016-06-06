package PreferenceAgent;

import RadioPlayer.RadioStation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by Patrick on 24-5-2016.
 */
public class XMLManager {

    private static XMLManager instance = null;

    private static DocumentBuilderFactory documentBuilderFactory;
    private static TransformerFactory transformerFactory;
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

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

    public boolean writeStations() {

        File file = new File("radioStations.xml");

        if(!file.exists()) {
            try {
                file.createNewFile();
                Document doc = documentBuilder.newDocument();
                Element rootElement = doc.createElement("Stations");

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {

                doc.appendChild(rootElement);

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }



        return true;
    }
}
