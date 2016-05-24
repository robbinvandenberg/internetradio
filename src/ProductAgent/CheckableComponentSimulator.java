package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Bart on 12-5-2016.
 */
public class CheckableComponentSimulator extends CheckableComponent {
    public enum Mode {AlwaysOk, DefectiveWithinSpecifiedTime}

    Mode mode;
    int defectTime;
    boolean hasBroken;
    long creationTime;

    public CheckableComponentSimulator(String name, Date installDate, List<Step> replacementSteps, String fileName, Mode simulationMode){
        super(name, installDate, replacementSteps, fileName);
        this.mode = simulationMode;
        defectTime = 0;
        hasBroken = false;
        creationTime = System.currentTimeMillis();
    }

    public CheckableComponentSimulator(String name, Date installDate, List<Step> replacementSteps, String fileName, Mode simulationMode, int defectTime){
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

    public static CheckableComponent fromFile(String fileName, Mode simulationMode) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new CheckableComponentSimulator(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName, simulationMode);
    }

    public static CheckableComponent fromFile(String fileName, Mode simulationMode, int defectTime) throws UnableToParseComponentFileException {
        ComponentFile file = ComponentFile.Load(fileName);
        return new CheckableComponentSimulator(file.getComponentName(), file.getInstallDate(), file.getReplacementSteps(), fileName, simulationMode, defectTime);
    }
}
