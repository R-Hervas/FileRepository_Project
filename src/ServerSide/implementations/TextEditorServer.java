package ServerSide.implementations;

import ServerSide.Models.FileResource;
import ServerSide.classes.RepoGestor;
import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;
import ServerSide.interfaces.TextFileServerInterface;

import java.io.IOException;

/**
 * Implementation of server. This server has a repository which can be read, edited and created remotely by clients connected
 * to the server.
 */
public class TextEditorServer extends ServerBase implements TextFileServerInterface {

    /**
     * Unique instance of the singleton class
     */
    private final RepoGestor repository = RepoGestor.getRepoGestorInstance();

    /**
     * Constructor of the class
     * @param name
     * @param port - Must be known for the clients to connect to it
     */
    public TextEditorServer(String name, int port) {
        super(name, port);
    }

    /**
     * Sends a message notifying the connection
     * @param client
     */
    @Override
    public void onClientConnected(ClientHandler client) {
        client.sendMessage(CLIENT_NOTIFICATION + "Se ha conectado al repositorio de archivos");
    }

    /**
     * Responds to the message with different action
     *
     * <ul>
     *     <li>
     *         #CLIENT_GET_ -> Connects the client with a file and sends the content as a response
     *     </li>
     *     <li>
     *         #CLIENT_POST_ -> Writes the content
     *     </li>
     *     <li>
     *         #CLIENT_CREATE_FILE_ -> Creates a file i n the repository
     *     </li>
     *     <li>
     *         #CLIENT_GET_FILE_LIST -> Returns the file list in the repository as a formatted String
     *     </li>
     *     <li>
     *         #CLIENT_MESSAGE_NAME -> Changes the client name
     *     </li>
     *     <li>
     *         If message doesn`t match with any condition it will show in the server console
     *     </li>
     * </ul>
     * @param client
     * @param message
     */
    @Override
    public void onClientMessageReceived(ClientHandler client, String message) {
        System.out.println(message);
        if (message.matches("^" + CLIENT_GET_FILE + "[\\w ]+\\.txt?")){
            if (!client.isConnectedToResource()) {
                String requestedFileName = message.replace(CLIENT_GET_FILE, "");
                FileResource requestedFile = repository.findFileByName(requestedFileName);
                if (requestedFile != null) {
                    client.setConnectionWithFile(requestedFile);
                    client.sendMessage(CLIENT_GET_FILE + client.readFromResource());
                } else {
                    client.sendMessage(CLIENT_ERROR + "El recurso no se encuentra disponible");
                }
            } else {
                client.sendMessage(CLIENT_ERROR + "El cliente ya se encuentra usando un recurso");
            }
        } else if (message.matches("^" + CLIENT_POST_FILE + ".*?")){
            if (client.isConnectedToResource()){
                client.writeInResource(message.replace(CLIENT_POST_FILE, ""));
                System.out.println("paso 1" + message);
            } else {
                client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
            }
        } else if (message.matches("^" + CLIENT_CREATE_FILE + "[\\w ]+\\.txt?")){
                String newFileName = message.replace(CLIENT_CREATE_FILE, "");
                try {
                    if (repository.createFile(newFileName) != null)
                        client.sendMessage(TextFileServerInterface.CLIENT_CREATE_FILE + newFileName);
                    else
                        client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "Error creando el archivo " + newFileName + ", Probablemente ya exista");
                } catch (IOException e) {
                    client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El archivo no se puede crear");
                }
        } else if (message.matches(CLIENT_GET_FILE_LIST)){
            client.sendMessage(CLIENT_GET_FILE_LIST + repository.getFileListAsString());
        } else if (message.matches("^" + CLIENT_MESSAGE_NAME + ".*?")) {
            System.out.println(getFormattedMessage(client, message.replace(CLIENT_MESSAGE_NAME, "") + "Se ha conectado al servidor"));
        } else {
            System.out.println("DEFAULT MESSAGE: " + getFormattedMessage(client, message));
        }
    }

    /**
     * Notifies all the clients and disconnects them from the server
     */
    @Override
    public void onListeningStopped() {
        broadcastMessage(null, CLIENT_NOTIFICATION + "El servidor se ha desconectado");
        broadcastMessage(null, SERVER_DISCONNECTED);
    }

}
