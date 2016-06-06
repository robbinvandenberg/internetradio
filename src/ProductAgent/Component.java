package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bart on 10-5-2016.
 *
 * Class containing basic functionality for a component.
 */
public class Component {
    private String name;
    private Date installDate;
    private Vector<Step> replaceingSteps;
    private long mileageStartTime;
    protected String fileName;

    /**
     * Constructor of Component
     *
     * @param name name of component
     * @param installDate installationdate of component.
     * @param replaceingSteps Vector containing the steps to replace the component.
     * @param fileName path to the component file.
     */
    public Component(String name, Date installDate, Vector<Step> replaceingSteps, String fileName){
        this.name = name;
        this.installDate = installDate;
        this.replaceingSteps = replaceingSteps;
        mileageStartTime = -1;
        this.fileName = fileName;
    }

    /**
     * method for getting the replacement steps of the component
     *
     * @return Vector containing replacement steps.
     */
    public Vector<Step> getReplaceingSteps() {
        return replaceingSteps;
    }

    /**
     * Get name of component.
     *
     * @return name of the component.
     */
    public String getName() {
        return name;
    }

    /**
     * Get installationdate of component.
     *
     * @return installationdate of component.
     */
    public Date getInstallDate(){
        return installDate;
    }

    /**
     * Start mileage count.
     */
    public void startMileageCount(){
        if(mileageStartTime == -1) {
            mileageStartTime = System.currentTimeMillis();
        }
    }


    /**
     * Stop mileage count and write mileage to the componentfile.
     */
    public void stopMileageCount() throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        if(mileageStartTime != -1) {
            long mileage = (System.currentTimeMillis() - mileageStartTime) / 1000;

            ComponentFile file = ComponentFile.Load(fileName);
            file.addMileage(mileage);

            mileageStartTime = -1;
        }
    }

    /**
     * Get milage of the component
     *
     * @return mileage of component in seconds
     */
    public long getMileage() throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return file.getMileage();
    }

    /**
     * Method used for logging component status change to file.
     *
     * @param componentStatus the status the component has changed to.
     */
    protected void log(ComponentStatus componentStatus) throws UnableToParseComponentFileException, UnableToStoreComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        file.addToLog(new Log(new Date(), "The component status has been changed to " + componentStatus.toString()));
    }

    /**
     * Get all component logs.
     *
     * @return Vector containing Log objects.
     */
    public Vector<Log> getLogs() throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return file.getLogs();
    }
}
