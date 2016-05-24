package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import ProductAgent.Web.DownloadAttachmentPage;
import ProductAgent.Web.HomePage;
import ProductAgent.Web.LogPage;
import com.sun.net.httpserver.HttpServer;
import jade.core.Agent;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.net.InetSocketAddress;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 */
public class ProductAgent extends Agent {
    private static final long componentCheckInterval = 10000;
    private ComponentChecker componentChecker;

    private Vector<CheckableComponent> checkableComponents;
    private Vector<NonCheckableComponent> nonCheckableComponents;
    private Vector<Component> allComponents;

    private HttpServer webServer;

    public void setup() {
        checkableComponents = new Vector<CheckableComponent>();
        nonCheckableComponents = new Vector<NonCheckableComponent>();
        System.err.println("PRODUCT AGENT SETUP");

        autoDetectComponents();
        //addComponentToCheckAbles("components/checkable/amplifier/componentInfo.xml");

        allComponents = new Vector<Component>();

        for (CheckableComponent component: checkableComponents){
            allComponents.add(component);
        }
        for (NonCheckableComponent component: nonCheckableComponents){
            allComponents.add(component);
        }

        componentChecker = new ComponentChecker(this, componentCheckInterval, checkableComponents);
        addBehaviour(componentChecker);

        for (Component component: allComponents){
            component.startMileageCount();
        }

        try {

            webServer = HttpServer.create(new InetSocketAddress(8000), 0);
            webServer.createContext("/", new HomePage(checkableComponents, nonCheckableComponents));
            webServer.createContext("/downloadAttachment", new DownloadAttachmentPage(allComponents));
            webServer.createContext("/logs", new LogPage(allComponents));
            webServer.setExecutor(null); // creates a default executor
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeDown(){
        for (CheckableComponent component: checkableComponents){
            try {
                component.stopMilageCount();
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
            } catch (UnableToStoreComponentFileException e) {
                e.printStackTrace();
            }
        }

        for (NonCheckableComponent component: nonCheckableComponents){
            try {
                component.stopMilageCount();
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
            } catch (UnableToStoreComponentFileException e) {
                e.printStackTrace();
            }
        }
    }

    private void addComponentToCheckAbles(String file){
        addComponentToCheckAbles(file, CheckableComponentSimulator.Mode.AlwaysOk);
    }

    private void addComponentToCheckAbles(String file, CheckableComponentSimulator.Mode simulationMode){
        try {
            CheckableComponent component = CheckableComponentSimulator.fromFile(file, CheckableComponentSimulator.Mode.AlwaysOk);
            checkableComponents.add(component);
            System.out.println("component " + component.getName() + " added");
        } catch (UnableToParseComponentFileException e) {
            System.err.println("Unable initialize component " + file);
            e.printStackTrace();
        }
    }

    /*
    Dynamically read out the checkable components folder and add them.
     */
    private void autoDetectComponents() {
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path file) throws IOException {
                return (Files.isDirectory(file));
            }
        };

        // Set default test path
        Path dir = FileSystems.getDefault().getPath("");
        // Try get the path to project folder
        try {
            dir = FileSystems.getDefault().getPath(new File(".").getCanonicalPath() + "\\src\\components\\checkable");
        } catch(IOException e) {
            e.printStackTrace();
        }

        // Process all the components inside checkable components folder
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
            for (Path path : stream) {
                // Iterate over the paths in the directory and add found components
                addComponentToCheckAbles("components/checkable/" + path.getFileName() + "/componentInfo.xml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}