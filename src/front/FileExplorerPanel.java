package front;

import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class FileExplorerPanel extends JPanel implements ActionListener {
	
	private JList<String> list;
	private DefaultListModel<String> model;
	private JScrollPane scroll;
	private FileRepositoryButton btnAdd, btnEdit, btnRemove;
	private JTextField tfNewElement;
	
	private OnFileExplorerClientListener listener;
	private FileRepositoryFrame frame;

	
	public FileExplorerPanel(OnFileExplorerClientListener listener, FileRepositoryFrame frame) {
		
		setBounds(0, 0, 500, 500);
		setBackground(Color.white);
		
		this.listener = listener;
		this.frame = frame;
		
		// LIST -------------------------------------------------
		list = new JList<>();
		list.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		list.setForeground(Color.BLACK);
		list.setBackground(Color.WHITE);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		list.setSelectionBackground(new Color(0, 169, 255));
		list.setSelectionForeground(Color.WHITE);
		
		model = new DefaultListModel<>();
		setLayout(null);
		
		scroll = new JScrollPane();
		scroll.setBounds(78, 103, 374, 210);
		scroll.setViewportView(list);

		this.add(scroll);
		
		// BUTTONS -------------------------------------------------
		btnEdit = new FileRepositoryButton(
				new Color(0, 169, 255),
				Color.white,
				Color.white,
				"../icons/iddleEdit.png",
				"../icons/hoverEdit.png"
		);
		btnEdit.setBounds(23, 151, 45, 45);
		btnEdit.addActionListener(this);
		this.add(btnEdit);
		
		btnAdd = new FileRepositoryButton(
				new Color(0, 169, 255),
				Color.white,
				Color.white,
				"../icons/iddleAdd.png",
				"../icons/hoverAdd.png"
		);
		btnAdd.addActionListener(this);
		btnAdd.setBounds(23, 103, 45, 45);
		this.add(btnAdd);
		
		btnRemove  = new FileRepositoryButton(
				new Color(255, 63, 63),
				Color.white,
				Color.white,
				"../icons/iddleDelete.png",
				"../icons/hoverDelete.png"
		);
		btnRemove.setBounds(23, 200, 45, 45);
		btnRemove.addActionListener(this);
		this.add(btnRemove);
		
		tfNewElement = new JTextField();
		tfNewElement.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		tfNewElement.setForeground(Color.BLACK);
		tfNewElement.setBackground(Color.WHITE);
		tfNewElement.setBounds(23, 50, 429, 32);
		tfNewElement.setColumns(10);
		this.add(tfNewElement);
	
		
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnAdd) {
			
			String fileName = tfNewElement.getText();
			
			String errorLog = this.validateFileName(fileName);
			
			if(!errorLog.isEmpty()) {
				frame.throwError(errorLog);
			} else {
				listener.notifyNewFile(fileName);	
				tfNewElement.setText("");
			}
			
		} else if(event.getSource() == btnRemove) {
			
			listener.notifyDeletedFile(model.get(list.getSelectedIndex()));
			listener.requestFileList();

		} else if(event.getSource() == btnEdit) {
			
			listener.requestFile(model.get(list.getSelectedIndex()));
		}
	}
	
	private String validateFileName(String fileName) {
		String errorLog = "";
		
		if(fileName.isEmpty()) {
			errorLog += "El fichero tiene que tener nombre \n"; 
		} 
		else if(!fileName.matches("^.*\\.t{1}x{1}t{1}?")) {
			errorLog += "La extensión del fichero debe ser .txt \n"; 
		}
		return errorLog;
	}
	
	public void loadElements(String[] elements) {
		model.clear();
		
		for(String str: elements) {
			model.addElement(str);
		}
		list.setModel(model);
		list.setSelectedIndex(0);
		
	}
	
	public void addElement(String element) {
		model.addElement(element);
		list.setModel(model);
	}
	


}