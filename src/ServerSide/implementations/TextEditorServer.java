package ServerSide.implementations;

import ServerSide.Models.FileResource;
import ServerSide.classes.RepoGestor;
import ServerSide.helpers.TxtFileFilter;
import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;
import ServerSide.interfaces.TextFileServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TextEditorServer extends ServerBase implements TextFileServer {

    private RepoGestor repository = RepoGestor.getRepoGestorInstance();

    public TextEditorServer(String name, int port) {
        super(name, port);
    }

    @Override
    public void onClientConnected(ClientHandler client) {
        super.onClientConnected(client);
        client.sendMessage(repository.getFileList());
    }

    @Override
    public void onClientMessageRecived(ClientHandler client, String message) {
        if (message.matches("^" + CLIENT_GET_FILE + "[\\w ]+\\.txt?")){
            String requestedFileName = message.substring(CLIENT_GET_FILE.length(),(message.length()));
            FileResource requestedFile = repository.findFileName(requestedFileName);
            if (requestedFile != null) {
                try {
                    String requestedFileContent = requestedFile.getFileText();
                    client.sendMessage(requestedFileContent);
                } catch (IOException e) {
                    client.sendMessage("File Loading Failed");
                }
            } else {
                client.sendMessage("There's no file such as" + requestedFileName);
            }
        } else if (message.matches("^" + CLIENT_POST_FILE + "[\\w _]+\\.txt?")){

        }

    }
}
