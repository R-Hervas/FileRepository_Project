package ClientSide.implementations;

import java.io.IOException;

import ClientSide.classes.BaseClient;
import front.FileEditorFrame;
import front.FileEditorPanel;
import front.FileExplorerFrame;
import front.FileExplorerPanel;
import front.FileRepositoryFrame;
import front.OnFileExplorerClientListener;

public class FileRepositoryClient extends BaseClient implements OnFileExplorerClientListener {

		private FileRepositoryFrame repositoryFrame;
	
	 	public FileRepositoryClient(String name) {
	 		super(name);
	 		try {
	 			repositoryFrame = new FileRepositoryFrame(this);
	 		}catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    @Override
	    public void onConnected() {

	        super.onConnected();
			this.requestFileList();	
			repositoryFrame.setVisible(true);
	    }

	    @Override
	    public void onDisconnected() {   
	    	System.exit(0);
	    }
	    
	    @Override
	    public void onClientMessageRecived(String message) {
	    	System.out.println(message);
	    	
	    	if (message.matches("^" + CLIENT_NOTIFICATION  + ".*?")){
	    		
	    		message = message.replace(CLIENT_NOTIFICATION, "");
	    		repositoryFrame.throwNotification(message);
	    		
	        } else if (message.matches("^" + CLIENT_ERROR  + ".*?")){
	    		
	    		message = message.replace(CLIENT_ERROR, "");
	    		repositoryFrame.throwError(message);

	        } else if (message.matches("^" + CLIENT_LIST  + ".*?")){
	    		
	    		message = message.replace(CLIENT_LIST, "");
	    		
	    		String[] files = message.split("\\|");
	    		FileExplorerPanel explorerPanel = (FileExplorerPanel) repositoryFrame.getContentPane();
	    		explorerPanel.loadElements(files);
	            
	        } else if (message.matches("^" + CLIENT_CREATE_FILE  + ".*?")){
	    		
	    		message = message.replace(CLIENT_CREATE_FILE, "");
	    		repositoryFrame.throwNotification("El fichero " + message + " ha sido creado");
	    		FileExplorerPanel explorerPanel = (FileExplorerPanel) repositoryFrame.getContentPane();
	    		explorerPanel.addElement(message);
	    		
	        } else if (message.matches("^" + CLIENT_GET_FILE  + ".*?")) {
	        	
	        	message = message.replace(CLIENT_GET_FILE, "");
	        	repositoryFrame.setContentPanel(new FileEditorPanel(this, repositoryFrame, message));
	        }
	    }

	    @Override
	    public void onMessageSent(String message) {
	        
	    }

		@Override
		public void notifyNewFile(String file) {
			this.sendMessage(CLIENT_CREATE_FILE + file);
		}

		@Override
		public void notifyUpdatedFile(String text) {
			this.sendMessage(CLIENT_POST_FILE + text);
		}

		@Override
		public void notifyDeletedFile(String file) {
			this.sendMessage(CLIENT_DELETE + file);
		}
		
		@Override
		public void notifyReleasedFile() {
			this.sendMessage(CLIENT_RELEASE_FILE);
		}

		@Override
		public void requestFile(String file) {
			this.sendMessage(CLIENT_GET_FILE + file);
		}
		
		@Override
		public void requestFileList() {
			this.sendMessage(CLIENT_LIST);
		}
}
