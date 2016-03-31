import java.awt.Font;


/**
 * UI_Element.java
 * The UI_Element class.
 * 
 */
public class UI_Element {

	private int xLength;
	private int xPos;
	private int yLength;
	private int yPos;
	private int fontSize;
	private String id;
	private String type;
	private String label;
	private String fontStyle;
	private String fontWeight;
	private String borderStyle;	
	private Font font;
	
	
	/**
	 * Constructs the UI_Element object. 
	 * Font parameters are used to create a font object.
	 * 
	 * @param xL
	 * @param xP
	 * @param yL
	 * @param yP
	 * @param fSize
	 * @param fStyle
	 * @param fWeight
	 * @param uiLabel
	 * @param uiType
	 * @param border
	 * @param ui_id
	 */
	public UI_Element(int xL, int xP, int yL, int yP, int fSize, 
					  String fStyle, String fWeight, String uiLabel, 
					  String uiType, String border, String ui_id) {
		xLength = xL;
		xPos = xP;
		yLength = yL;
		yPos = yP;
		fontSize = fSize;
		type = uiType;
		borderStyle = border;
		fontWeight = fWeight;
		label = uiLabel;
		id = ui_id;
		if(fWeight.equals("Bold")){
			font = new Font(fStyle, Font.BOLD, fSize);
		} else {
			font = new Font(fStyle, Font.PLAIN, fSize);
		}
		
	}
	
	/**
	 * Returns the Font variable
	 * 
	 * @return Font
	 */
	public Font getFont() {
		return font;
	}
	/**
	 * Returns the xLength variable
	 * 
	 * @return int
	 */
	public int getxLength() {
		return xLength;
	}
	
	/**
	 * Returns the ID variable
	 * 
	 * @return String
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Returns the xPos variable
	 * 
	 * @return int
	 */
	public int getxPos() {
		return xPos;
	}
	
	/**
	 * Returns the yLength variable
	 * 
	 * @return int
	 */
	public int getyLength() {
		return yLength;
	}
	/**
	 * 
	 * @return int
	 */
	public int getyPos() {
		return yPos;
	}
	
	/**
	 * Returns the fSize variable
	 * 
	 * @return int
	 */
	public int getfSize() {
		return fontSize;
	}
	
	/**
	 * Returns the type variable
	 *  
	 * @return int
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the borderStyle variable
	 * 
	 * @return String
	 */
	public String getBorder() {
		return borderStyle;
	}
	
	/**
	 * returns the fontStyle variable
	 * 
	 * @return String
	 */
	public String getfStyle() {
		return fontStyle;
	}
	
	/**
	 * Returns the fontWeight variable
	 * 
	 * @return String
	 */
	public String getWeight() {
		return fontWeight;
	}
	
	/**
	 * Constructs a string using all variables
	 * 
	 * @return String
	 */
	public String toString() {
		String s = "";
		s += xLength + ", ";
		s += xPos + ", ";
		s += yLength + ", ";
		s += xPos + ", ";
		s += fontSize + ", ";
		s += type + ", ";
		s += borderStyle + ", ";
		s += fontWeight + ", ";
		s += label + ", ";
		s += id;
		return s;
	}
}
