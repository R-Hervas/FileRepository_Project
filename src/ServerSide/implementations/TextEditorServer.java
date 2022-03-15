package ServerSide.implementations;

import ServerSide.Models.FileResource;
import ServerSide.classes.RepoGestor;
import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;
import ServerSide.interfaces.TextFileServer;

import java.io.IOException;


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
            if (!client.isConnectedToResource()) {
                String requestedFileName = message.replace(CLIENT_GET_FILE, "");
                FileResource requestedFile = repository.findFileByName(requestedFileName);
                if (requestedFile != null) {
                    client.setConnectionWithFile(requestedFile);
                    client.sendMessage(CLIENT_POST_FILE + client.readFromResource());
                } else {
                    client.sendMessage(CLIENT_NOTIFICATION + "El recurso no se encuentra disponible");
                }
            } else {
                client.sendMessage(CLIENT_NOTIFICATION + "El cliente ya se encuentra usando un recurso");
            }
        } else if (message.matches("^" + CLIENT_POST_FILE)){
            if (client.isConnectedToResource()){
                client.writeInResource(message.replace(CLIENT_POST_FILE, ""));
            } else {
                client.sendMessage(TextFileServer.CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
            }
        } else if (message.matches("^" + CLIENT_CREATE_FILE + "[\\w ]+\\.txt?")){
            if (!client.isConnectedToResource()){
                client.setConnectionWithFile(repository.createFile(message.replace(CLIENT_CREATE_FILE, "")));
            } else {
                client.sendMessage(CLIENT_NOTIFICATION + "El cliente ya se encuentra usando un recurso");
            }
        }

    }
}
