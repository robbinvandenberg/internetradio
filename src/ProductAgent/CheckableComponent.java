package ProductAgent;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by Bart on 10-5-2016.
 */
public abstract class CheckableComponent extends Component {
    private int checkInterval; //in seconds

    public CheckableComponent(String name, List<Step> replacementSteps, int checkInterval){
        super(name, replacementSteps);
        this.checkInterval = checkInterval;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public ComponentStatus checkStatus(){
        ComponentStatus currentStatus = blabla();

        if(currentStatus != getPreviousStatus()){

            //TODO log verandering naar logfile
        }
        return currentStatus;
    }

    private ComponentStatus getPreviousStatus(){
        //TODO haal laatste status op uit log
        throw new NotImplementedException();
    }

    protected abstract ComponentStatus blabla();
}
