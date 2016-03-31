import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Special JPanel GUI control to set an ImageIcon
 */
public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Image pic;
	
	/**
	 * Constructor, requires a path to a start image
	 */
	public ImagePanel(String startImage){
		ImageIcon image = new ImageIcon(startImage);
		pic = image.getImage();
		this.setBackground(new Color(224,255,255));		
	}
	
	/**
	 * Overridden paintComponent function from JPanel, redraws image
	 * 
	 * @param g The graphics to draw on
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if (pic != null)
			g.drawImage(pic,0,0,getWidth(),getHeight(),this);
	}
	
	/**
	 * Set a ImageIcon to the ImagePanel, can redraw
	 * 
	 * @param img The image to be drawn
	 */
	public void setPic(ImageIcon img){
		if (img == null)
		{
			pic = null;
		}
		else
		{
			pic = img.getImage();
		}
		repaint();
	}
	
}
