package ServerSide.classes;

import ServerSide.interfaces.ClientListener;
import ServerSide.interfaces.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerBase implements ServerInterface {

    protected String name;
    protected int port;
    protected ArrayList<ClientHandler> clientHandlers;
    protected ServerSocket listenerSocket = null;
    protected boolean isRunning; // TODO use isInterrupted() instead

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
            System.out.println("Errores durante el inicio del server\n" + e.toString());
        }
    }

    /**
     * When overriding stopListening() keep in mind to call onListeningStopped()
     * At the end of your implementation
     * @throws IOException
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
     * @param client
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
     * @param listenerSocket
     */
    @Override
    public void createHandler(Socket listenerSocket) {
        ClientHandler handler = new ClientHandler(listenerSocket, this);
        this.clientHandlers.add(handler);
        handler.start();
        this.onClientConnected(handler);
    }

    @Override
    public void broadcastMessage(ClientHandler sender, String message) {
        if (message != null)
            for (ClientHandler handler : clientHandlers)
                if (handler != sender)
                    handler.sendMessage(message);
    }


    @Override
    public void onHandlerMessageRecived(ClientHandler client, String message) {
        if (message.equals(ClientListener.CLIENT_NOTIFY_DISCONNECT) || message.equals(ClientListener.CLIENT_NOTIFY_ERROR))
            this.removeHandler(client);
    }

    /**
     * Should never override this method
     * @param client
     * @param message
     * @param messageType
     */
    @Override
    public void onMessageRecived(ClientHandler client, String message, int messageType) {
        synchronized (this) {
            if (messageType == ServerInterface.MESSAGE_TYPE_CLIENT)
                this.onClientMessageRecived(client, message);
            else if (messageType == ServerInterface.MESSAGE_TYPE_NOTIFY);
                this.onHandlerMessageRecived(client, message);
        }
    }

    protected String getFormattedMessage(ClientHandler handler, String message){
        String formattedMessage = handler.getClientName() + ": " + message;
        return formattedMessage;
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }


    /* Must be overriden */
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
    public void onClientMessageRecived(ClientHandler client, String message) {

    }

}
