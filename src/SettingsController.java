import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SpinnerModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/** 
 * SettingsController.java
 * The SettingsController class.
 * 
 * In this class the settings menu GUI is represented to the user
 */
public class SettingsController extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private DeviceHandler handler;
	private JLabel lblNetwork;
	private JSlider slBrightness;
	private JLabel lblBrightness;
	private JSpinner spGain;
	private JSpinner spBass;
	private JSpinner spTreble;
	private JSpinner spBalance;
	private JLabel lblGain;
	private JLabel lblBass;
	private JLabel lblTreble;
	private JLabel lblBalance;
	private JButton btnShutdown;
	private JButton btnHome;
	private JButton btnSave;
	private JButton btnNetwork;
	private JComboBox<String> comboBox;
	private File settingsXML;
	private ArrayList<Audio_Setting> al;
	private int currentALIndex;
	private MusicController myMusicController;

	/**
	 * The constructor of SettingsController.
	 *
	 * 
	 * @param musicController The instance to the MusicController, required for controlling the music stream
	 */
	public SettingsController(MusicController musicController) {
		settingsXML = new File(DeviceHandler.getInstance().ExecutionPath, "AudioSettings.xml");
		myMusicController = musicController;
		handler = DeviceHandler.getInstance();
	}
	
	/**
	 * The StartGUI function.
	 * 
	 * This function makes the settings menu GUI frame. 
	 */
	public void StartGUI()
	{
		UI ui = UI_Handler.readLayout("SettingsController");
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		getContentPane().setCursor(blankCursor);
		
		getContentPane().setBackground(new Color(192, 192, 192));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize( 480, 272);
		getContentPane().setLayout(null);
		
		UI_Element combobox = ui.getElementById("combobox");
		comboBox = new JComboBox<String>();
		comboBox.setFont(combobox.getFont());
		comboBox.setBounds(combobox.getxPos(), combobox.getyPos(), combobox.getxLength(), combobox.getyLength());
		//comboBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		//comboBox.setBounds(10, 10, 173, 32);
		comboBox.addActionListener(this);
		//ComboBoxDemo(option);
		getContentPane().add(comboBox); 
		
		UI_Element brightness = ui.getElementById("slBrightness");
		slBrightness = new JSlider();
		slBrightness.setValue(100);//handler.getBrightness()); Cannot be used with HDMI
		slBrightness.setMinimum(10);
		slBrightness.setPaintTicks(true);
		slBrightness.setPaintLabels(true);
		slBrightness.setMinorTickSpacing(1);
		slBrightness.setMinimumSize(new Dimension(36, 43));
		slBrightness.setMaximumSize(new Dimension(32767, 43));
		slBrightness.setMajorTickSpacing(10);
		slBrightness.setForeground(Color.BLACK);
		slBrightness.setFont(brightness.getFont());
		//slBrightness.setFont(new Font("Tahoma", Font.BOLD, 11));
		slBrightness.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), null, new Color(0, 0, 0)));
		slBrightness.setBackground(new Color(255, 250, 205));
		slBrightness.setBounds(brightness.getxPos(), brightness.getyPos(), brightness.getxLength(), brightness.getyLength());
		//slBrightness.setBounds(10, 112, 460, 66);
		getContentPane().add(slBrightness);
		slBrightness.addChangeListener(this);
		slBrightness.setUI(new MetalSliderUI() 
		{
			protected TrackListener createTrackListener(JSlider slider) 
			{
			    return new TrackListener() 
			    {
			      @Override public void mousePressed(MouseEvent e) 
			      {
			        JSlider slider = (JSlider)e.getSource();
			        switch (slider.getOrientation()) {
			          case JSlider.VERTICAL:
			            slider.setValue(valueForYPosition(e.getY()));
			            break;
			          case JSlider.HORIZONTAL:
			            slider.setValue(valueForXPosition(e.getX()));
			            break;
			        }
			        super.mousePressed(e); //isDragging = true;
			        super.mouseDragged(e);
			      }
			      @Override public boolean shouldScroll(int direction) 
			      {
			          return false;
			      }
			    };
			}
		});
		
		UI_Element lblbrightness = ui.getElementById("Brightness");
		lblBrightness = new JLabel("Brightness");
		lblBrightness.setFont(lblbrightness.getFont());
		//lblBrightness.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblBrightness.setLabelFor(slBrightness);
		lblBrightness.setBounds(lblbrightness.getxPos(), lblbrightness.getyPos(), lblbrightness.getxLength(), lblbrightness.getyLength());
		//lblBrightness.setBounds(10, 98, 460, 14);
		getContentPane().add(lblBrightness);
		
		UI_Element spgain = ui.getElementById("spGain");
		spGain = new JSpinner();
		spGain.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		spGain.setFont(spgain.getFont());
		//spGain.setFont(new Font("Tahoma", Font.BOLD, 11));
		spGain.setValue(myMusicController.getGain());
		spGain.setBounds(spgain.getxPos(), spgain.getyPos(), spgain.getxLength(), spgain.getyLength());
		//spGain.setBounds(20, 61, 69, 33);
		spGain.addChangeListener(this);
		getContentPane().add(spGain);
		
		UI_Element spbass = ui.getElementById("spBass");
		spBass = new JSpinner();
		spBass.setModel(new SpinnerNumberModel(0, -10, 10, 1));
		spBass.setFont(spbass.getFont());
		//spBass.setFont(new Font("Tahoma", Font.BOLD, 11));
		spBass.setValue(handler.getBass());
		spBass.setBounds(spbass.getxPos(), spbass.getyPos(), spbass.getxLength(), spbass.getyLength());
		//spBass.setBounds(268, 61, 69, 33);
		spBass.addChangeListener(this);
		getContentPane().add(spBass);
		
		UI_Element sptreble = ui.getElementById("spTreble");
		spTreble = new JSpinner();
		spTreble.setModel(new SpinnerNumberModel(0, -10, 10, 1));
		spTreble.setFont(sptreble.getFont());
		//spTreble.setFont(new Font("Tahoma", Font.BOLD, 11));
		spTreble.setValue(handler.getTreble());
		spTreble.setBounds(sptreble.getxPos(), sptreble.getyPos(), sptreble.getxLength(), sptreble.getyLength());
		//spTreble.setBounds(391, 61, 69, 33);
		spTreble.addChangeListener(this);
		getContentPane().add(spTreble);
		
		UI_Element spbalance = ui.getElementById("spBalance");
		spBalance = new JSpinner();
		spBalance.setModel(new SpinnerNumberModel(0, -10, 10, 1));
		spBalance.setFont(spbalance.getFont());
		//spBalance.setFont(new Font("Tahoma", Font.BOLD, 11));
		spBalance.setValue(myMusicController.getBalance());
		spBalance.setBounds(spbalance.getxPos(), spbalance.getyPos(), spbalance.getxLength(), spbalance.getyLength());
		//spBalance.setBounds(143, 61, 69, 33);
		spBalance.addChangeListener(this);
		getContentPane().add(spBalance);
		
		UI_Element lblgain = ui.getElementById("lblGain");
		lblGain = new JLabel("Gain");
		lblGain.setFont(lblgain.getFont());
		//lblGain.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblGain.setLabelFor(spGain);
		lblGain.setHorizontalAlignment(SwingConstants.CENTER);
		lblGain.setBounds(lblgain.getxPos(), lblgain.getyPos(), lblgain.getxLength(), lblgain.getyLength());
		//lblGain.setBounds(20, 43, 69, 18);
		getContentPane().add(lblGain);
		
		UI_Element lblbalance = ui.getElementById("lblBalance");
		lblBalance = new JLabel("Balance");
		lblBalance.setFont(lblbalance.getFont());
		//lblBalance.setLabelFor(spBalance);
		lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalance.setBounds(lblbalance.getxPos(), lblbalance.getyPos(), lblbalance.getxLength(), lblbalance.getyLength());
		//lblBalance.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblBalance.setBounds(143, 43, 69, 18);
		getContentPane().add(lblBalance);
		
		UI_Element lblbass = ui.getElementById("lblBass");
		lblBass = new JLabel("Bass");
		lblBass.setFont(lblbass.getFont());
		//lblBass.setLabelFor(spBass);
		lblBass.setHorizontalAlignment(SwingConstants.CENTER);
		lblBass.setBounds(lblbass.getxPos(), lblbass.getyPos(), lblbass.getxLength(), lblbass.getyLength());
		//lblBass.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblBass.setBounds(268, 40, 69, 21);
		getContentPane().add(lblBass);
		
		UI_Element lbltreble = ui.getElementById("lblTreble");
		lblTreble = new JLabel("Treble");
		lblTreble.setFont(lbltreble.getFont());
		//lblTreble.setLabelFor(spTreble);
		lblTreble.setHorizontalAlignment(SwingConstants.CENTER);
		lblTreble.setBounds(lbltreble.getxPos(), lbltreble.getyPos(), lbltreble.getxLength(), lbltreble.getyLength());
		//lblTreble.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblTreble.setBounds(391, 40, 69, 21);
		getContentPane().add(lblTreble);
		
		UI_Element home = ui.getElementById("btnHome");
		btnHome = new JButton("Home");
		btnHome.setFont(home.getFont());
		btnHome.setBounds(home.getxPos(), home.getyPos(), home.getxLength(), home.getyLength());
		//btnHome.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnHome.setBounds(390, 10, 80, 32);
		btnHome.addActionListener(this);
		getContentPane().add(btnHome);
		
		UI_Element save = ui.getElementById("btnSave");
		btnSave = new JButton("Save");
		btnSave.setFont(save.getFont());
		btnSave.setBounds(save.getxPos(), save.getyPos(), save.getxLength(), save.getyLength());
		//btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnSave.setBounds(301, 10, 80, 32);
		btnSave.addActionListener(this);
		getContentPane().add(btnSave);
		
		UI_Element shutdown = ui.getElementById("btnShutdown");
		btnShutdown = new JButton("Shutdown");
		btnShutdown.setFont(shutdown.getFont());
		btnShutdown.setBounds(shutdown.getxPos(), shutdown.getyPos(), shutdown.getxLength(), shutdown.getyLength());
		//btnShutdown.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnShutdown.setBounds(222, 10, 80, 32);
		btnShutdown.addActionListener(this);
		getContentPane().add(btnShutdown);
		
		UI_Element btnnetwerk = ui.getElementById("btnNetwork");
		btnNetwork = new JButton("Network centrum");
		btnNetwork.setFont(btnnetwerk.getFont());
		btnNetwork.setBounds(btnnetwerk.getxPos(), btnnetwerk.getyPos(), btnnetwerk.getxLength(), btnnetwerk.getyLength());
		//btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnNetwork.setFont(new Font("Tahoma", Font.BOLD, 11));
		//btnNetwork.setBounds(10, 196, 460, 65);
		btnNetwork.addActionListener(this);
		getContentPane().add(btnNetwork);
		
		UI_Element netwerk = ui.getElementById("lblNetwork");
		lblNetwork = new JLabel("Network");
		lblNetwork.setFont(netwerk.getFont());
		//lbNetwerk.setFont(new Font("Tahoma", Font.BOLD, 11));
		//lblNetwork.setLabelFor(btnNetwork);
		lblNetwork.setBounds(netwerk.getxPos(), netwerk.getyPos(), netwerk.getxLength(), netwerk.getyLength());
		//lbNetwerk.setBounds(10, 180, 460, 14);
		getContentPane().add(lblNetwork);
		setUndecorated(true);
		fillOptions();
	}

	/**
	 * Gets called by various GUI controls on action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnHome)
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if(e.getSource() == btnSave)
		{
			saveCurrentScheme();
		}
		else if(e.getSource() == btnShutdown)
		{
			String message = "Are you sure you want to shutdown?";
			String title = "Shutdown?";
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				handler.command("shutdown -h now", ".", false);
			}
		}
		else if(e.getSource() == btnNetwork)
		{
			String command = "wicd-client -n";
			String command1 = "florence --use-config /root/florence.conf";
			ArrayList<String> output = handler.command(command, ".", false);
			ArrayList<String> output1 = handler.command(command1, ".", false);
			if (output == null || output1 == null)
			{
		        System.out.println("\n\n\t\tCOMMAND FAILED: " + command);
		        System.out.println("\n\n\t\tCOMMAND FAILED: " + command1);
			}
			/*else
			{
				for (String line : output)
					System.out.println(line);
				for (String line : output1)
					System.out.println(line);
			}*/
		}
		// to open touch keyboard, echo >> "matchbox-keyboard"
		
		else if(e.getSource() == comboBox){
			currentALIndex = comboBox.getSelectedIndex();
			spGain.setValue(al.get(currentALIndex).getGain());
			spBass.setValue(al.get(currentALIndex).getBass());
			spBalance.setValue(al.get(currentALIndex).getPan());
			spTreble.setValue(al.get(currentALIndex).getTreble());
			//System.out.println(al.get(currentALIndex).getGain() + " - " + al.get(currentALIndex).getBass() + " - " + al.get(currentALIndex).getPan() + " - " + al.get(currentALIndex).getTreble());
		}
	}
	
	/**
	 * The fillOptions function.
	 * 
	 * This function fills the settings with the values that are readed from the AudioSettings.xml.
	 */
	public void fillOptions(){
		al = readSettings();
		//System.out.println("" + al.size());
		for(int i = 0; i < al.size(); i++){
			comboBox.addItem(al.get(i).getId());
		}
	}
	
	/**
	 * The saveCurrentScheme function.
	 * 
	 * This fuction is to save the changed settings from a profile.
	 */
	public void saveCurrentScheme(){
		if(currentALIndex > 0){
			String message = "Are you sure?";
			String title = "Overwrite scheme: " + al.get(currentALIndex).getId();
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, al.get(currentALIndex).getId() + " overwritten.");
				Audio_Setting as = new Audio_Setting(al.get(currentALIndex).getId(), Integer.parseInt(spBass.getValue().toString()), Integer.parseInt(spTreble.getValue().toString()), Integer.parseInt(spGain.getValue().toString()), Integer.parseInt(spBalance.getValue().toString()));
				//System.out.println("Overwritten: " + as.toString());
				al.set(currentALIndex, as);
				//System.out.println("new: " + al.get(currentALIndex).toString());
				writeSettingsToXml(al);	
	        }
	        else {
	        	JOptionPane.showMessageDialog(null, "Nothing overwritten.");
	        }
		}
		else{
			String message = "You can't overwrite the default scheme.";
			String title = "";
			JOptionPane.showMessageDialog(null, message);
		}		
	}
	
	/**
	 * The parseDefaultValues function.
	 * 
	 * This function initialize the values from the AudioSettings.xml.
	 * This is the only function that can be called without startGUI
	 */
	public void parseDefaultValues(){
		//System.out.println("Parse default values");
		al = readSettings();
		if(al != null) {
			if (spGain == null) {
				myMusicController.setGain(al.get(0).getGain());
				handler.setBass(al.get(0).getBass());
				handler.setTreble(al.get(0).getTreble());
				myMusicController.setBalance(al.get(0).getPan());
			} else {
				spGain.setValue(al.get(0).getGain());
				spBass.setValue(al.get(0).getBass());
				spBalance.setValue(al.get(0).getPan());
				spTreble.setValue(al.get(0).getTreble());
			}
		}
	}

	/**
	 * Gets called by various GUI controls on change
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		//System.out.println("CLASSES:" + e.getSource().getClass() + "," + slBrightness.getClass());
		if(e.getSource().getClass().equals(slBrightness.getClass())){
			//System.out.println("Brightness changed");
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				//handler.setBrightness(slBrightness.getValue()); Cannot be used with HDMI
			}	
		}		
		else if(e.getSource().getClass().equals(spBalance.getClass())){
			if(e.getSource() == spGain){
				//System.out.println("Gain changed");
				myMusicController.setGain((int)spGain.getValue());
			}
			else if(e.getSource() == spBass){
				//System.out.println("Bass changed");
				handler.setBass((int)spBass.getValue());
			}
			else if(e.getSource() == spTreble){
				//System.out.println("Treble changed");  
				handler.setTreble((int)spTreble.getValue());
			}
			else if(e.getSource() == spBalance){
				//System.out.println("Balance changed");
				myMusicController.setBalance((int)spBalance.getValue());
			}
		}
	}
	
	/**
	 * The writeSettingsToXml function.
	 * 
	 * This function write the values to the AudioSettings.xml.
	 * 
	 * @param settings This arraylist contains all the values of the settings.
	 * @see saveCurrentScheme()
	 */
	private void writeSettingsToXml(ArrayList<Audio_Setting> settings) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Sound_Preferences");
			doc.appendChild(root);
			for (Iterator<Audio_Setting> i = settings.iterator(); i.hasNext();) {
				Audio_Setting st = i.next();
				Element preference = doc.createElement("Preference");
				preference.setAttribute("id", st.getId());
				Element bass = doc.createElement("Bass");
				bass.setAttribute("value", Integer.toString(st.getBass()));
				preference.appendChild(bass);
				Element treble = doc.createElement("Treble");
				treble.setAttribute("value", Integer.toString(st.getTreble()));
				preference.appendChild(treble);
				Element gain = doc.createElement("Gain");
				gain.setAttribute("value", Integer.toString(st.getGain()));
				preference.appendChild(gain);
				Element pan = doc.createElement("Pan");
				pan.setAttribute("value", Integer.toString(st.getPan()));
				preference.appendChild(pan);
				root.appendChild(preference);
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(settingsXML);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			//System.out.println("File saved!");

		} catch (Exception e) {

		}
	}
	
	/**
	 * The readSettings function.
	 * 
	 * This function read the values of the settings from the AudioSettings.xml file with the choosen profile.
	 * 
	 * @return settings This arraylist contains all the values of the settings.
	 * @see fillOptions()
	 * @see parseDefaultValues()
	 */
	private ArrayList<Audio_Setting> readSettings() {
		final ArrayList<Audio_Setting> settings = new ArrayList<Audio_Setting>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				int pan;
				int gain;
				int bass;
				int treble;
				String id;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {

					if (qName.equalsIgnoreCase("PREFERENCE")) {
						id = attributes.getValue("id");
					}

					if (qName.equalsIgnoreCase("BASS")) {
						bass = Integer.parseInt(attributes.getValue("value"));
					}
					if (qName.equalsIgnoreCase("TREBLE")) {
						treble = Integer.parseInt(attributes.getValue("value"));
					}
					if (qName.equalsIgnoreCase("GAIN")) {
						gain = Integer.parseInt(attributes.getValue("value"));
					}
					if (qName.equalsIgnoreCase("PAN")) {
						pan = Integer.parseInt(attributes.getValue("value"));
					}
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("PREFERENCE")) {
						settings.add(new Audio_Setting(id, bass, treble, gain, pan));
					}
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
				
				}

			};
			saxParser.parse(settingsXML, handler);
			//System.out.println("settings size: " + settings.size());

		} catch (Exception e) {
		}
		return settings;
	}
}
