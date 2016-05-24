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
 */
public abstract class CheckableComponent extends Component {

    public CheckableComponent(String name, Date installDate, Vector<Step> replacementSteps, String fileName){
        super(name, installDate, replacementSteps, fileName);
    }

    public ComponentStatus checkStatus() throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        ComponentStatus currentStatus = getComponentStatus();

        ComponentFile file = ComponentFile.Load(fileName);

        if(currentStatus != file.getStatus()){
            file.setComponentStatus(currentStatus);
            log(currentStatus);
        }
        return currentStatus;
    }

    protected abstract ComponentStatus getComponentStatus();


}
