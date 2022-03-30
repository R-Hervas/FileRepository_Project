package front;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class FileRepositoryButton extends JButton {

	private Color primaryColor, secundaryColor,backgroundColor;
	private ImageIcon iddleIcon, hoverIcon;
	private final FileRepositoryButton self = this;
	
	public FileRepositoryButton(Color primaryColor, Color secundaryColor, Color backgroundColor, String urlIddleIcon, String urlHoverIcon) {
		this.primaryColor = primaryColor;
		this.secundaryColor = secundaryColor;
		this.backgroundColor = backgroundColor;
		this.iddleIcon = new ImageIcon(FileExplorerPanel.class.getResource(urlIddleIcon));
		this.hoverIcon = new ImageIcon(FileExplorerPanel.class.getResource(urlHoverIcon));
		
		initAttributes();
	}

	private void initAttributes() {
		this.setIcon(iddleIcon);
		this.setBackground(backgroundColor);
		this.setBorder(new LineBorder(backgroundColor));
		this.setFocusable(false);

		this.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	self.setBackground(primaryColor);
		    	self.setIcon(hoverIcon);
		    	self.setBorder(new LineBorder(primaryColor));
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	self.setBackground(backgroundColor);
		    	self.setIcon(iddleIcon);
		    	self.setBorder(new LineBorder(backgroundColor));
		    	
		    }
		});
	}


}
