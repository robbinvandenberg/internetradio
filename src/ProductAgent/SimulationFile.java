package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToParseSimulationFileException;
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
public class SimulationFile {

    private static final String filePrefix = "out/production/internetradio/";
    private String fileName;
    private Document document;
    private Element root;

    /**
     * Constructor of ComponentFile.
     *
     * @param fileName path to file.
     * @param document DOM object containing the component elemeonts.
     */
    private SimulationFile(String fileName, Document document){
        this.fileName = fileName;
        this.document = document;
        root = document.getDocumentElement();
    }

    /**
     * Get component name.
     *
     * @return name of component.
     */
    public CheckableComponentSimulator.Mode getSimulationMode(){
        if(root.getElementsByTagName("mode").item(0).getTextContent().equals(CheckableComponentSimulator.Mode.AlwaysOk.toString())){
            return CheckableComponentSimulator.Mode.AlwaysOk;
        }
        else{
            return CheckableComponentSimulator.Mode.DefectiveWithinSpecifiedTime;
        }
    }

    public int getDefectTime(){
        return Integer.parseInt(root.getElementsByTagName("defectTime").item(0).getTextContent());
    }


    /**
     * Static method for creating a ComponentFile object from a given filepath.
     *
     * @param fileName path to simulationfile.
     */
    public static SimulationFile Load(String fileName) throws UnableToParseSimulationFileException {
        try {
            File componentFile = new File(filePrefix + fileName);
            DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuildFactory.newDocumentBuilder();
            Document componentDocument = documentBuilder.parse(componentFile);

            if(!componentDocument.getDocumentElement().getTagName().equals("simulation")){
                throw new IllegalArgumentException("root element 'simulation' not found in file.");
            }

            SimulationFile file = new SimulationFile(fileName, componentDocument);

            return file;

        } catch (SAXException e) {
            throw new UnableToParseSimulationFileException(e);
        } catch (ParserConfigurationException e) {
            throw new UnableToParseSimulationFileException(e);
        } catch (IOException e) {
            throw new UnableToParseSimulationFileException(e);
        }
    }
}
