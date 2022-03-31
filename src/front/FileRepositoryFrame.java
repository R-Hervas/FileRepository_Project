package front;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Image;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FileRepositoryFrame extends JFrame {
	
	private OnFileExplorerClientListener listener;
	private JPanel contentPane;
	
	public FileRepositoryFrame(OnFileExplorerClientListener listener) throws IOException {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 500, 500);
		setBackground(Color.white);
		
		Image icon = new ImageIcon(getClass().getResource("../icons/icon.png")).getImage();
        setIconImage(icon);
        setResizable(false);
        setTitle("Textmark");
		setContentPanel(new FileExplorerPanel(listener,this));
	}
	
	public void throwNotification(String message) {
		JOptionPane.showMessageDialog(this, message,"Information", JOptionPane.INFORMATION_MESSAGE);	
	}
	
	public void throwError(String message) {
		JOptionPane.showMessageDialog(this, message,"Error", JOptionPane.ERROR_MESSAGE);	
	}

	public JPanel getContentPanel() {
		return contentPane;
	}

	public void setContentPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		setContentPane(contentPane);
		this.repaint();
	}

}
