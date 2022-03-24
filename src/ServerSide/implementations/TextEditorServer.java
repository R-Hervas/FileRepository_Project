package ServerSide.implementations;

import ServerSide.Models.FileResource;
import ServerSide.classes.RepoGestor;
import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;
import ServerSide.interfaces.TextFileServerInterface;

import java.io.File;
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

            getFile(client, message);

        } else if (message.matches("^" + CLIENT_POST_FILE + ".*?")){

            postFile(client, message);

        } else if (message.matches("^" + CLIENT_CREATE_FILE + "[\\w ]+\\.txt?")){

            createFile(client, message);

        } else if (message.matches(CLIENT_RELEASE_FILE)) {

            releaseFile(client, message);

        } else if (message.matches("^" + CLIENT_DELETE_FILE + "[\\w ]+\\.txt?")){

            deleteFile(client, message);

        } else if (message.matches(CLIENT_GET_FILE_LIST)){

            client.sendMessage(CLIENT_GET_FILE_LIST + repository.getFileListAsString());

        } else if (message.matches("^" + CLIENT_MESSAGE_NAME + ".*?")) {

            System.out.println(getFormattedMessage(client, message.replace(CLIENT_MESSAGE_NAME, "") + " Se ha conectado al servidor"));

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

    /**
     * Creates a file in the repository
     * @param client
     * @param message - Message sent by the client for request
     */
    @Override
    public void createFile(ClientHandler client, String message) {
        String newFileName = message.replace(CLIENT_CREATE_FILE, "");
        try {
            if (repository.createFile(newFileName) != null) {
                client.sendMessage(TextFileServerInterface.CLIENT_CREATE_FILE + newFileName);
                broadcastMessage(null, CLIENT_GET_FILE_LIST + repository.getFileListAsString());
            } else
                client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "Error creando el archivo " + newFileName + ", Probablemente ya exista");
        } catch (IOException e) {
            client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El archivo no se puede crear");
        }
    }

    /**
     * Connects and sends the content of a file to a client
     * @param client
     * @param message - Message sent by the client for request
     */
    @Override
    public void getFile(ClientHandler client, String message) {
        if (!client.isConnectedToResource()) {
            String requestedFileName = message.replace(CLIENT_GET_FILE, "");
            FileResource requestedFile = repository.findFileByName(requestedFileName);
            if (requestedFile != null) {
                if (client.setConnectionWithFile(requestedFile))
                    client.sendMessage(CLIENT_GET_FILE + client.readFromResource());
                else
                    client.sendMessage(CLIENT_ERROR + "Recurso no disponible");
            } else {
                client.sendMessage(CLIENT_ERROR + "El recurso no se encuentra disponible");
            }
        } else {
            client.sendMessage(CLIENT_ERROR + "El cliente ya se encuentra usando un recurso");
        }
    }

    /**
     * Overwrites a file already assigned to the client with a new text
     * @param client
     * @param message - Message sent by the client for request
     */
    @Override
    public void postFile(ClientHandler client, String message) {
        if (client.isConnectedToResource()){
            client.writeInResource(message.replace(CLIENT_POST_FILE, ""));
        } else {
            client.sendMessage(TextFileServerInterface.CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
        }
    }

    /**
     * Disconnects a client from the file it was connected to
     * @param client
     * @param message - Message sent by the client for request
     */
    @Override
    public void releaseFile(ClientHandler client, String message) {
        if (client.isConnectedToResource()){
            client.disconnectFromFile();
            client.sendMessage(CLIENT_NOTIFICATION + "Liberando fichero");
        } else {
            client.sendMessage(CLIENT_ERROR + "El cliente no tiene asociado ningun fichero");
        }
    }

    /**
     * Deletes a file from the repository
     * @param client
     * @param message - Message sent by the client for request
     */
    @Override
    public void deleteFile(ClientHandler client, String message) {
        String fileName = message.replace(CLIENT_DELETE_FILE, "");
        FileResource fileResource = repository.findFileByName(fileName);
        if (fileResource != null && fileResource.isAvaliable()) {
            if (fileResource.delete()){
                client.sendMessage(CLIENT_NOTIFICATION + "Fichero borrado");
                repository.loadFilesInRepository();
                broadcastMessage(null, CLIENT_GET_FILE_LIST + repository.getFileListAsString());
            } else {
                client.sendMessage(CLIENT_ERROR + "Error al borrar el fichero");
            }
        } else {
            client.sendMessage(CLIENT_ERROR + "El fichero no esta disponible");
        }
    }
}
