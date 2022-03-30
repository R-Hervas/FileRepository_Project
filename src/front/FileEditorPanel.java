package front;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class FileEditorPanel extends JPanel implements ActionListener {

	
	private OnFileExplorerClientListener listener;
	private FileRepositoryFrame frame;
	
	private FileRepositoryButton btnExit, btnSave;
	private JTextArea taEditor;
	
	private String currentText, updatedText;
	
	public FileEditorPanel(OnFileExplorerClientListener listener, FileRepositoryFrame frame, String text) {
		this.currentText = text;
		this.listener = listener; 
		this.frame = frame;
		
		setBounds(0, 0, 500, 488);
		setBackground(new Color(45,45,45));
		
		btnSave = new FileRepositoryButton(
				new Color(0, 169, 255),
				Color.white,
				new Color(45,45,45),
				"../icons/iddleSave.png",
				"../icons/hoverSave.png"
		);
		btnSave.setBounds(366, 408, 45, 45);
		btnSave.addActionListener(this);
		setLayout(null);
		this.add(btnSave);
		
		btnExit = new FileRepositoryButton(
				new Color(255, 63, 63),
				Color.white,
				new Color(45,45,45),
				"../icons/iddleOut.png",
				"../icons/hoverOut.png"
		);
		btnExit.setBounds(421, 408, 45, 45);
		btnExit.addActionListener(this);
		this.add(btnExit);
		
		taEditor = new JTextArea();
		taEditor.setBackground(Color.DARK_GRAY);
		taEditor.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		taEditor.setForeground(Color.WHITE);
		taEditor.setLineWrap(true);
		taEditor.setBounds(26, 23, 440, 374);
		taEditor.setText(text);
		this.add(taEditor);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnSave) {
			
			updatedText = taEditor.getText();
			String errorLog = this.checkTextVersion();
			
			if(!errorLog.isEmpty()) {
				frame.throwError(errorLog);
			}else {
				frame.setContentPanel(new FileExplorerPanel(listener,frame));
				listener.requestFileList();
				listener.notifyUpdatedFile(updatedText);
			}
			
		} else if(event.getSource() == btnExit) {
			
			frame.setContentPanel(new FileExplorerPanel(listener,frame));
			listener.requestFileList();
			listener.notifyReleasedFile();
		}
		
	}
	
	private String checkTextVersion() {
		String errorLog = "";
		
		if(currentText.equals(updatedText)) {
			errorLog += "El fichero no ha recibido modificaciones"; 
		} 

		return errorLog;
	}

}
