package ProductAgent;

import ProductAgent.Exceptions.UnableToParseComponentFileException;
import ProductAgent.Exceptions.UnableToStoreComponentFileException;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.Vector;

/**
 * Created by Bart on 14-5-2016.
 */
public class ComponentChecker extends TickerBehaviour {

    private Vector<CheckableComponent> components;

    public ComponentChecker(Agent agent, long checkInterval, Vector<CheckableComponent> components){
        super(agent, checkInterval);
        this.components = components;
    }

    @Override
    protected void onTick() {
        System.out.println("Checking components.");
        for (CheckableComponent component: components){
            try {
                component.checkStatus();
            } catch (UnableToParseComponentFileException e) {
                e.printStackTrace();
                System.err.println("Unable to parse component file "+ component.getName());
            } catch (UnableToStoreComponentFileException e) {
                e.printStackTrace();
                System.err.println("Unable to write component log "+ component.getName());
            }
        }
    }
}
