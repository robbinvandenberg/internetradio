package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 */
public class Component {
    private String name;
    private List<Step> replaceingSteps;
    private long mileageStartTime;
    protected String fileName;

    public Component(String name, List<Step> replaceingSteps, String fileName){
        this.name = name;
        this.replaceingSteps = replaceingSteps;
        mileageStartTime = -1;
        this.fileName = fileName;
    }

    public List<Step> getReplaceingSteps() {
        return replaceingSteps;
    }

    public String getName() {
        return name;
    }

    public void startMileageCount(){
        if(mileageStartTime == -1) {
            mileageStartTime = System.currentTimeMillis();
        }
    }

    public void stopMilageCount() throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        if(mileageStartTime != -1) {
            long mileage = (System.currentTimeMillis() - mileageStartTime) / 1000;

            ComponentFile file = ComponentFile.Load(fileName);
            file.addMileage(mileage);

            mileageStartTime = -1;
        }
    }

    public long getMileage() throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return file.getMileage();
    }

    protected void log(ComponentStatus componentStatus) throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        file.addToLog(new Log(new Date(), "The component status has been changed to " + componentStatus.toString()));
    }

    public Vector<Log> getLogs() throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return file.getLogs();
    }
}
