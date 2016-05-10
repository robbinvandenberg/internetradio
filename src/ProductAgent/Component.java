package ProductAgent;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by Bart on 10-5-2016.
 */
public class Component {
    private String name;
    private List<Step> replaceingSteps;
    private String fileName;

    public Component(String name, List<Step> replaceingSteps, String fileName){
        this.name = name;
        this.replaceingSteps = replaceingSteps;
        this.fileName = fileName;
    }

    public List<Step> getReplaceingSteps() {
        return replaceingSteps;
    }

    public String getName() {
        return name;
    }

    protected void log(ComponentStatus componentStatus){

        //TODO schrijf nieuwe status naar log in de fileName
        throw new NotImplementedException();
    }

    public static Component fromFile(String fileName){
        //TODO parse from XML
        throw new NotImplementedException();

    }
}
