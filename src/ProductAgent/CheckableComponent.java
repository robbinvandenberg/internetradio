package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 *
 * The CheckableComponent class can be used for components that can be checked by the system.
 */
public abstract class CheckableComponent extends Component {

    /**
     * Constructor for CheckableComponent.
     *
     * @param name the thame of the component.
     * @param installDate the date on whch the component has been installed in the device.
     * @param replacementSteps a Vector which contains all the steps to replace the component.
     * @param fileName path and filename to the component file (containing all the component information).
     */
    public CheckableComponent(String name, Date installDate, Vector<Step> replacementSteps, String fileName){
        super(name, installDate, replacementSteps, fileName);
    }

    /**
     * check the status of the component. If state changed, this method will log the change to the component file.
     *
     * @return Current status of the component.
     */
    public ComponentStatus checkStatus() throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        ComponentStatus currentStatus = getComponentStatus();

        ComponentFile file = ComponentFile.Load(fileName);

        if(currentStatus != file.getStatus()){
            file.setComponentStatus(currentStatus);
            log(currentStatus);
        }
        return currentStatus;
    }

    /**
     * Gets the component status. This method must be implemented by the derived class because
     * every component has a differnet way of checking it's status (for example: one component can be checked with a
     * serial command, another by reading a (GPIO) pin, etc)
     *
     * @return  status of the component.
     */
    protected abstract ComponentStatus getComponentStatus();
}
