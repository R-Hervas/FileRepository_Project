package ServerSide.classes;

import ServerSide.interfaces.ClientListener;
import ServerSide.interfaces.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Basic class to define server. This class must be inherited by any implementation of a server.
 */
public class ServerBase implements ServerInterface {

    /**
     * Name of the server
     */
    protected String name;
    /**
     * Listening port for the server
     */
    protected int port;
    /**
     * List of handlers listening clients in the server
     */
    protected ArrayList<ClientHandler> clientHandlers;
    /**
     * Server Socket
     */
    protected ServerSocket listenerSocket = null;
    /**
     * True if the server is listening, false otherwise
     */
    protected boolean isRunning;

    /**
     * Constructor of the class
     * @param name
     * @param port - Must be known by the client in order to stablish connection
     */
    public ServerBase(String name, int port) {
        this.name = name;
        this.port = port;
        this.isRunning = false;
        this.clientHandlers = new ArrayList<>();
    }


    /**
     * When overriding startListening() keep in mind to call onListeningStarted(int)
     * as your socket start the listening on the selected port
     */
    @Override
    public void startListening() {
        isRunning = true;

        try {
            listenerSocket = new ServerSocket(port);
            this.onListeningStarted(port);
            while(isRunning)
                createHandler(listenerSocket.accept());
        } catch (IOException e) {
            isRunning = false;
            System.out.println("Errores durante el inicio del server\n" + e.getMessage());
        }
    }

    /**
     * When overriding stopListening() keep in mind to call onListeningStopped()
     * At the end of your implementation
     */
    @Override
    public void stopListening() throws IOException {
        this.isRunning = false;
        broadcastMessage(null, SERVER_DISCONNECTED);
        for (ClientHandler handler : clientHandlers){
            handler.dispose();
        }
        if(listenerSocket != null)
            listenerSocket.close();
        this.onListeningStopped();
    }

    /**
     * Call onClientDisconnected(ClientHandler) when overriding this method
     */
    @Override
    public void removeHandler(ClientHandler client) {
        client.dispose();
        this.clientHandlers.remove(client);
        this.onClientDisconnected(client);
    }

    /**
     * When you override createHandler(ClientHandler) Method keep in mind to call onClientConnected(ClientHandler)
     * as your handler get linked to your server.
     */
    @Override
    public void createHandler(Socket listenerSocket) {
        ClientHandler handler = new ClientHandler(listenerSocket, this);
        this.clientHandlers.add(handler);
        handler.start();
        this.onClientConnected(handler);
    }

    /**
     * Sends a message to all clients connected except of the sender
     * @param sender - client that sent the message
     * @param message
     */
    @Override
    public void broadcastMessage(ClientHandler sender, String message) {
        if (message != null)
            for (ClientHandler handler : clientHandlers)
                if (handler != sender)
                    handler.sendMessage(message);
    }

    /**
     * If there`s an error removes the client handler from the list
     * @param client
     * @param message
     */
    @Override
    public void onHandlerMessageReceived(ClientHandler client, String message) {
        if (message.equals(ClientListener.CLIENT_NOTIFY_DISCONNECT) || message.equals(ClientListener.CLIENT_NOTIFY_ERROR))
            this.removeHandler(client);
    }

    /**
     * Classifies and assigns a response for every type of message recieved from the client
     * <br>Should never override this method<br/>
     */
    @Override
    public void onMessageReceived(ClientHandler client, String message, int messageType) {
        synchronized (this) {
            if (messageType == ServerInterface.MESSAGE_TYPE_CLIENT)
                this.onClientMessageReceived(client, message);
            else if (messageType == ServerInterface.MESSAGE_TYPE_NOTIFY)
                this.onHandlerMessageReceived(client, message);
        }
    }

    /**
     * Formats a message
     * @param handler
     * @param message
     * @return
     */
    protected String getFormattedMessage(ClientHandler handler, String message){
        return handler.getClientName() + ": " + message;  //TODO No se cambia el nombre en el mensaje del cliente
    }

    /**
     * Returns the clientHandler list
     * @return
     */
    @SuppressWarnings("unused")
    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }


    /* Must be overridden */
    @Override
    public void onClientConnected(ClientHandler client) {
    }

    @Override
    public void onClientDisconnected(ClientHandler client) {
    }

    @Override
    public void onListeningStarted(int port) {

    }

    @Override
    public void onListeningStopped() {

    }
    @Override
    public void onClientMessageReceived(ClientHandler client, String message) {

    }

}
