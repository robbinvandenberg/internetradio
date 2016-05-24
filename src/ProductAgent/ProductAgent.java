package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import ProductAgent.Web.DownloadAttachmentPage;
import ProductAgent.Web.HomePage;
import ProductAgent.Web.LogPage;
import ProductAgent.Web.ReplaceInstructionsPage;
import com.sun.net.httpserver.HttpServer;
import jade.core.Agent;

import java.io.IOException;
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

        addComponentToCheckAbles("components/amplifier/componentInfo.xml");
        addComponentToCheckAbles("components/mainBoard/componentInfo.xml");
        addComponentToCheckAbles("components/touchScreen/componentInfo.xml");
        addComponentToCheckAbles("components/powerSupply/componentInfo.xml");
        addComponentToCheckAbles("components/wifiModule/componentInfo.xml");

        addComponentToNonCheckAbles("components/speaker/componentInfo.xml");
        addComponentToNonCheckAbles("components/frontPanel/componentInfo.xml");
        addComponentToNonCheckAbles("components/bottomPanel/componentInfo.xml");
        addComponentToNonCheckAbles("components/rearPanel/componentInfo.xml");
        addComponentToNonCheckAbles("components/topPanel/componentInfo.xml");
        addComponentToNonCheckAbles("components/leftSidePanel/componentInfo.xml");
        addComponentToNonCheckAbles("components/rightSidePanel/componentInfo.xml");

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
            webServer.createContext("/replaceInstructions", new ReplaceInstructionsPage(allComponents));
            webServer.setExecutor(null); // creates a default executor
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeDown(){
        for (CheckableComponent component: checkableComponents){
            try {
                component.stopMileageCount();
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
            } catch (UnableToStoreComponentFileException e) {
                e.printStackTrace();
            }
        }

        for (NonCheckableComponent component: nonCheckableComponents){
            try {
                component.stopMileageCount();
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
            } catch (UnableToStoreComponentFileException e) {
                e.printStackTrace();
            }
        }
    }

    private void addComponentToNonCheckAbles(String file){
        try {
            NonCheckableComponent component = NonCheckableComponent.fromFile(file);
            nonCheckableComponents.add(component);
            System.out.println("component " + component.getName() + " added");
        } catch (UnableToParseComponentFileException e) {
            System.err.println("Unable initialize component " + file);
            e.printStackTrace();
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
}
