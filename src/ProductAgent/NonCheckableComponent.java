package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 *
 * Class for components that cannot be checked digitally.
 */
public class NonCheckableComponent extends Component {

    /**
     * Constructor of NonCheckableComponent
     *
     * @param name the thame of the component.
     * @param installDate the date on whch the component has been installed in the device.
     * @param replacementSteps a Vector which contains all the steps to replace the component.
     * @param fileName path and filename to the component file (containing all the component information).
     */
    public NonCheckableComponent(String name, Date installDate, Vector<Step> replacementSteps, String fileName){
        super(name, installDate, replacementSteps, fileName);
    }

    /**
     * Load a NonCheckablecomponent from a given file
     *
     * @param fileName path to componentFile.
     */
    public static NonCheckableComponent fromFile(String fileName) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new NonCheckableComponent(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName);
    }
}
