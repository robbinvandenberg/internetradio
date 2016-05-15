package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * Created by Bart on 10-5-2016.
 */
public class NonCheckableComponent extends Component {

    public NonCheckableComponent(String name, List<Step> replacementSteps, String fileName){
        super(name, replacementSteps, fileName);
    }

    public static NonCheckableComponent fromFile(String fileName) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new NonCheckableComponent(file.getComponentName(), file.getReplacementSteps(), fileName);
    }
}
