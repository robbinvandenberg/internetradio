package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToReadAttachmentException;
import jade.core.Agent;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 */
public class ProductAgent extends Agent {
    private static final long componentCheckInterval = 10000;
    private ComponentChecker componentChecker;

    private Vector<CheckableComponent> checkableComponents;
    private Vector<NonCheckableComponent> nonCheckableComponents;

    public void setup() {
        checkableComponents = new Vector<CheckableComponent>();
        nonCheckableComponents = new Vector<NonCheckableComponent>();

        addComponentToCheckAbles("components/amplifier/componentInfo.xml");

        componentChecker = new ComponentChecker(this, componentCheckInterval, checkableComponents);
        addBehaviour(componentChecker);
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
