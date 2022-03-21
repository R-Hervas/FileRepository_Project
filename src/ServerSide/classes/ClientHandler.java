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
 * Note {@version 1.0.0}: This class is now prepared to handle a client who asks for txt file manipulation (In future
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

     public void sendMessage(String msg) {
        try {
            new PrintWriter(socket.getOutputStream(), true).println(msg);
        } catch (IOException e) {
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);
        }
    }

    private void handleMessage(String msg) {

        listener.onMessageReceived(this, msg, ClientListener.MESSAGE_TYPE_CLIENT);

        if(msg.equals(ClientListener.CLIENT_NOTIFY_DISCONNECT))
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);

        if(msg.contains(ClientListener.CLIENT_MESSAGE_NAME))
            this.clientName = msg.replace(ClientListener.CLIENT_MESSAGE_NAME, "");

    }


    private void notifyClientDisconnect(@SuppressWarnings("SameParameterValue") String disconnectMsg) {
        listener.onMessageReceived(this, disconnectMsg, ClientListener.MESSAGE_TYPE_NOTIFY);
    }

    public void dispose() {

        try {
            if (socket != null) {
                socket.close();
                isRunning = false;
                disconnectFromFile();
            }
        } catch (IOException e) {
            e.printStackTrace(); //TODO Cambiar esta linea por notificacion o algo asi
        }
    }

    public String getClientName() {
        return (clientName.equals(default_nickname) ? getIpAddress() : clientName);
    }

    @SuppressWarnings("unused")
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIpAddress() {
        return this.socket.getRemoteSocketAddress().toString();
    }

    public void setConnectionWithFile(FileResource fileResource) {
        if (fileResource.connect())
            this.fileResource = fileResource;
        else
            this.sendMessage(CLIENT_ERROR + "Recurso no disponible");
    }

    public void disconnectFromFile(){
        if (this.fileResource != null){
            fileResource.disconnect();
            fileResource = null;
            sendMessage(CLIENT_NOTIFICATION + "Liberando archivo");
        }
    }

    public void writeInResource(String newFileText){
        if (this.isConnectedToResource()) {
            try {
                this.fileResource.setFileText(newFileText);
                System.out.println("paso 2" + newFileText);
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

    public String readFromResource(){
        if (this.isConnectedToResource()) {
            try {
                return this.fileResource.getFileText();
            } catch (IOException e) {
                sendMessage(CLIENT_ERROR + "Error al acceder al archivo");
                this.disconnectFromFile();
                return "";
            }
        } else
            return "";
    }

    public boolean isConnectedToResource(){return (this.fileResource != null);}
}
