package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Bart on 12-5-2016.
 *
 * Simulator class to simulate a checkable component.
 */
public class CheckableComponentSimulator extends CheckableComponent {
    public enum Mode {AlwaysOk, DefectiveWithinSpecifiedTime}

    Mode mode;
    int defectTime;
    boolean hasBroken;
    long creationTime;

    /**
     * Constructor of CheckableComponentSimulator
     *
     * @param name the name of the component.
     * @param installDate the date on which the component has been installed in the device.
     * @param replacementSteps a Vector which contains all the steps to replace the component.
     * @param fileName path and filename to the component file (containing all the component information).
     * @param simulationMode the mode the simulator should be set to.
     */
    public CheckableComponentSimulator(String name, Date installDate, Vector<Step> replacementSteps, String fileName, Mode simulationMode){
        super(name, installDate, replacementSteps, fileName);
        this.mode = simulationMode;
        defectTime = 0;
        hasBroken = false;
        creationTime = System.currentTimeMillis();
    }

    /**
     * Constructor of CheckableComponentSimulator, this constructor takes an extra parameter containing the defect time.
     * If mode is set to DefectiveWithinSpecifiedTime, the component will fail somewhere within the given time.
     *
     * @param name the name of the component.
     * @param installDate the date on which the component has been installed in the device.
     * @param replacementSteps a Vector which contains all the steps to replace the component.
     * @param fileName path and filename to the component file (containing all the component information).
     * @param simulationMode the mode the simulator should be set to.
     * @param defectTime the timespan in which the component will fail.
     */
    public CheckableComponentSimulator(String name, Date installDate, Vector<Step> replacementSteps, String fileName, Mode simulationMode, int defectTime){
        super(name, installDate, replacementSteps, fileName);
        this.mode = simulationMode;
        this.defectTime = defectTime;
        hasBroken = false;
        creationTime = System.currentTimeMillis();
    }


    @Override
    protected ComponentStatus getComponentStatus(){
        if(mode == Mode.AlwaysOk){
            return ComponentStatus.Ok;
        }
        if(mode == Mode.DefectiveWithinSpecifiedTime){
            if(hasBroken){
                return ComponentStatus.Broken;
            }
            else{
                float randomValue = (float) Math.random();
                float brokenValue = randomValue * getDefectingChange();
                if(brokenValue > 0.5){
                    hasBroken = true;
                    return ComponentStatus.Broken;
                }
                else{
                    return ComponentStatus.Ok;
                }
            }
        }
        else{
            return ComponentStatus.Unknown;
        }
    }

    /**
     * Gets the change of component braking down. Change increases as time passes.
     *
     * @return value which indicates change of failure, where 0 is lowest and 1 highest.
     */
    private float getDefectingChange(){
        long passedTime = (System.currentTimeMillis() - creationTime) / 1000;
        try {
            float change = (float)passedTime / (float)defectTime;
            return change;
        }
        catch (ArithmeticException e){
            return 0;
        }
    }

    /**
    * Load a Checkcomponent from a given file
     *
     * @param fileName path to componentFile.
     * @param simulationMode simulationmode the simulator should operate in.
    */

    public static CheckableComponent fromFile(String fileName, Mode simulationMode) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new CheckableComponentSimulator(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName, simulationMode);
    }

    /**
     * Load a Checkcomponent from a given file
     *
     * @param fileName path to componentFile.
     * @param simulationMode simulationmode the simulator should operate in.
     * @param defectTime the timespan in which the component will fail.
     */
    public static CheckableComponent fromFile(String fileName, Mode simulationMode, int defectTime) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new CheckableComponentSimulator(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName, simulationMode, defectTime);
    }
}
