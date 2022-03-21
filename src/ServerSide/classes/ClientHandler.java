package ServerSide.classes;

import ServerSide.Models.FileResource;
import ServerSide.interfaces.ClientListener;
import ServerSide.interfaces.ServerInterface;
import ServerSide.interfaces.TextFileServerInterface;

import java.io.*;
import java.net.Socket;

import static ServerSide.interfaces.TextFileServerInterface.*;

/**
 * Handles the client interactions with the server. Extends from {@code Thread} as its always listening while the client
 * is connected. Every {@code ClientHandler} manages a client and creates a Thread to listen to it.
 * <br/> <br/>
 * Note version 1.0.0: This class is now prepared to handle a client who asks for txt file manipulation (In future
 * version it will be separated in {@code ClientHandler} and {@code ClientHandlerTxtEditor extends ClientHandler}
 */
public class ClientHandler extends Thread {

    /**
     * Default random for unidentified clients
     */
    private static final String default_nickname = "UnamedClient " + Math.random();

    /**
     * Client socket associated to client
     */
    private final Socket socket;

    /**
     * The server which is listening
     */
    private final ClientListener listener;

    /**
     * ClientName (If not defined it will be {@code default_nickname}
     */
    private String clientName;

    /**
     * True if the listener is correctly running, if false stops the Thread
     */
    private boolean isRunning;

    /**
     * File being edited by client, null if client is not editing any file
     *
     * (ClientHandlerTxtEditor in version 1.0.1)
     */
    private FileResource fileResource;

    /**
     * Constructor of the class
     * @param socket - client socket
     * @param listener - server listening
     */
    public ClientHandler(Socket socket, ClientListener listener) {

        this.socket = socket;
        this.isRunning = true;
        this.listener = listener;

        clientName = default_nickname;

        sendMessage(ServerInterface.SERVER_CONNECTED);
    }

    /**
     * When ClientHandler is Running, listens all messages the associated client send and manages them depending on the
     * information it carries. As the ClientHandler receives a message it reads line by line and handles the message. If
     * the client`s message it`s null, the server understands the client has disconnected.
     */
    @Override
    public void run() {


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (isRunning) {
                String input = in.readLine();
                if (input != null) {
                    handleMessage(input);
                } else {
                    notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);
                }
            }
        } catch (IOException e) {
            listener.onMessageReceived(this, ClientListener.CLIENT_NOTIFY_ERROR, ClientListener.MESSAGE_TYPE_NOTIFY);
        }
    }

    /**
     * Sends a normal message to the client if it`s not possible sends an error
     * @param msg - messsage sent to the client
     */
    public void sendMessage(String msg) {
        try {
            new PrintWriter(socket.getOutputStream(), true).println(msg);
        } catch (IOException e) {
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);
        }
    }

    /**
     * Handles a message from the client. First applies the basic behaviour as a response for a message and then applies
     * two possible special responses:
     *
     * <ul>
     *     <li>
     *         #CLIENT_DISCONNECTED -> Disconnects the client from the server
     *     </li>
     *     <li>
     *         #NICKNAME -> Changes the client name
     *     </li>
     * </ul>
     * @param msg - messsage sent to the client
     */
    private void handleMessage(String msg) {

        listener.onMessageReceived(this, msg, ClientListener.MESSAGE_TYPE_CLIENT);

        if(msg.equals(ClientListener.CLIENT_NOTIFY_DISCONNECT))
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);

        if(msg.contains(ClientListener.CLIENT_MESSAGE_NAME))
            this.clientName = msg.replace(ClientListener.CLIENT_MESSAGE_NAME, "");

    }

    /**
     * Notifies the client it`s disconnection to the server
     * @param disconnectMsg
     */
    private void notifyClientDisconnect(@SuppressWarnings("SameParameterValue") String disconnectMsg) {
        listener.onMessageReceived(this, disconnectMsg, ClientListener.MESSAGE_TYPE_NOTIFY);
    }

    /**
     * Defines the method to follow when the client handler is safetly disposed
     */
    public void dispose() {

        try {
            if (socket != null) {
                socket.close();
                isRunning = false;
                disconnectFromFile();
            }
        } catch (IOException e) {
            System.err.println("ERROR DISPOSING CLIENT " + this.getClientName());
            System.err.println(e.getMessage());
        }
    }

    /**
     * @return the client name
     */
    public String getClientName() {
        return (clientName.equals(default_nickname) ? getIpAddress() : clientName);
    }

    /**
     * Sets the client name
     * @param clientName
     */
    @SuppressWarnings("unused")
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return IP Address
     */
    public String getIpAddress() {
        return this.socket.getRemoteSocketAddress().toString();
    }

    /**
     * Assigns a file to the client, while a file is connected to the client, it can`t be accessed by other client and
     * this client can`t connect to a different file.
     * @param fileResource
     */
    public void setConnectionWithFile(FileResource fileResource) {
        if (fileResource.connect())
            this.fileResource = fileResource;
        else
            this.sendMessage(CLIENT_ERROR + "Recurso no disponible");
    }

    /**
     * Disconnects the client from the file it is was connected to one.
     */
    public void disconnectFromFile(){
        if (this.fileResource != null){
            fileResource.disconnect();
            fileResource = null;
            sendMessage("Liberando archivo");
        }
    }

    /**
     * Updates the file text content with a new one.
     *
     * <ul>
     *     <li>
     *         CLIENT_NOTIFICATION_archivo actualizado -> Operation went correctly
     *     </li>
     *     <li>
     *         CLIENT_ERROR_Error al acceder al archivo -> Error accessing the file
     *     </li>
     *     <li>
     *         CLIENT_ERROR_Error al acceder al archivo -> The client is not connected to any file
     *     </li>
     * </ul>
     * @param newFileText
     */
    public void writeInResource(String newFileText){
        if (this.isConnectedToResource()) {
            try {
                this.fileResource.setFileText(newFileText);
                sendMessage(CLIENT_NOTIFICATION + "Archivo actualizado");
                this.disconnectFromFile();
            } catch (IOException e) {
                sendMessage(CLIENT_ERROR + "Error al acceder al archivo");
                this.disconnectFromFile();
            }
        } else {
            sendMessage(CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
        }
    }

    /**
     * Gets the text content from the associated file.
     * @return Text file content as a String, {@code  null} if error at connecting to file or the client is not associated
     * to a file
     *
     * <ul>
     *     <li>
     *         CLIENT_ERROR_Error al acceder al archivo -> Error accessing the file
     *     </li>
     *     <li>
     *         CLIENT_ERROR_El cliente no esta asociado a ningun archivo -> The client is not connected to any file
     *     </li>
     * </ul>
     */
    public String readFromResource(){
        if (this.isConnectedToResource()) {
            try {
                return this.fileResource.getFileText();
            } catch (IOException e) {
                sendMessage(CLIENT_ERROR + "Error al acceder al archivo");
                this.disconnectFromFile();
                return null;
            }
        } else {
            sendMessage(CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
            return null;
        }
    }

    /**
     * @return true if client is connected to a file, false otherwise
     */
    public boolean isConnectedToResource(){return (this.fileResource != null);}
}
