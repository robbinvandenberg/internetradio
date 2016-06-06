package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 */
public class NonCheckableComponent extends Component {

    public NonCheckableComponent(String name, Date installDate, Vector<Step> replacementSteps, String fileName){
        super(name, installDate, replacementSteps, fileName);
    }

    public static NonCheckableComponent fromFile(String fileName) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new NonCheckableComponent(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName);
    }
}
