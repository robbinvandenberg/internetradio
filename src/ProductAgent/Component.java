package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import static jade.tools.sniffer.Message.step;

/**
 * Created by Bart on 10-5-2016.
 */
public class Component {
    private String name;
    private List<Step> replaceingSteps;
    protected String fileName;

    public Component(String name, List<Step> replaceingSteps, String fileName){
        this.name = name;
        this.replaceingSteps = replaceingSteps;
        this.fileName = fileName;
    }

    public List<Step> getReplaceingSteps() {
        return replaceingSteps;
    }

    public String getName() {
        return name;
    }

    protected void log(ComponentStatus componentStatus) throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        file.AddToLog(new Log(new Date(), "The component status has been changed to " + componentStatus.toString()));
    }

    public Vector<Log> getLogs() throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return file.getLogs();
    }
}
