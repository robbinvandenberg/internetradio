package ProductAgent;

import java.util.List;

/**
 * Created by Bart on 10-5-2016.
 */
public class NonCheckableComponent extends Component {

    public NonCheckableComponent(String name, List<Step> replacementSteps, String fileName){
        super(name, replacementSteps, fileName);
    }
}
