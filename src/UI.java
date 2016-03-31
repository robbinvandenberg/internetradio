import java.util.ArrayList;

/**
 * UI.java
 * The UI class.
 * 
 */
public class UI {

	ArrayList<UI_Element> list;
	/**
	 * Constructs UI object
	 * 
	 * @param ui
	 */
	public UI(UI ui) {
		list = ui.getElements();
	}
	/**
	 * Constructs UI object
	 * 
	 * @param uilist
	 */
	public UI(ArrayList<UI_Element> uilist) {
		list = uilist;
	}
	/*
	public String getType() {
		return "asdf";	
	}*/
	/**
	 * Returns the list of UI elements
	 * 
	 * @return ArrayList<UI_Element>
	 */
	public ArrayList<UI_Element> getElements() {
		return list;
	}
	
	/**
	 * Returns an element using the id variable as key
	 * 
	 * @param id
	 * @return UI_Element
	 */
	public UI_Element getElementById(String id) {
		
		for(UI_Element ue : list) {
			if (ue.getID().equals(id)) {
				return ue;
			}
		}
		return null;
	}
	
}