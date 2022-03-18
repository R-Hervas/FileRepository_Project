package ServerSide.implementations;

import ServerSide.Models.FileResource;
import ServerSide.classes.RepoGestor;
import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;
import ServerSide.interfaces.TextFileServerInterface;

import java.io.IOException;


public class TextEditorServer extends ServerBase implements TextFileServerInterface {

    private RepoGestor repository = RepoGestor.getRepoGestorInstance();

    public TextEditorServer(String name, int port) {
        super(name, port);
    }

    @Override
    public void onClientConnected(ClientHandler client) {
        super.onClientConnected(client);
        client.sendMessage(CLIENT_NOTIFICATION + "Bienvenido a repositorio de archivos");
        client.sendMessage(CLIENT_ERROR + "VAMOS A MORIR TODOS!!!!");
    }

    @Override
    public void onClientMessageRecived(ClientHandler client, String message) {
        if (message.matches("^" + CLIENT_GET_FILE + "[\\w ]+\\.txt?")){
            if (!client.isConnectedToResource()) {
                String requestedFileName = message.replace(CLIENT_GET_FILE, "");
                FileResource requestedFile = repository.findFileByName(requestedFileName);
                if (requestedFile != null) {
                    client.setConnectionWithFile(requestedFile);
                    client.sendMessage(CLIENT_GET_FILE + client.readFromResource());
                } else {
                    client.sendMessage(CLIENT_NOTIFICATION + "El recurso no se encuentra disponible");
                }
            } else {
                client.sendMessage(CLIENT_NOTIFICATION + "El cliente ya se encuentra usando un recurso");
            }
        } else if (message.matches("^" + CLIENT_POST_FILE + ".*?")){
            if (client.isConnectedToResource()){
                client.writeInResource(message.replace(CLIENT_POST_FILE, ""));
            } else {
                client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
            }
        } else if (message.matches("^" + CLIENT_CREATE_FILE + "[\\w ]+\\.txt?")){
            if (!client.isConnectedToResource()){
                try {
                    repository.createFile(message.replace(CLIENT_CREATE_FILE, ""));
                } catch (IOException e) {
                    client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El archivo ya existe");
                }
            } else {
                client.sendMessage(CLIENT_NOTIFICATION + "El cliente ya se encuentra usando un recurso");
            }
        } else if (message.matches(CLIENT_GET_FILE_LIST)){
            client.sendMessage(CLIENT_GET_FILE_LIST + repository.getFileList());
        } else {
            System.out.println("client: " + message);
        }

    }
}
