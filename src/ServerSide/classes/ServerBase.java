package ServerSide.classes;

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

    public ServerBase() {
        isRunning = true;

        try {
            listenerSocket = new ServerSocket(port);
            this.onListeningStarted(port);
            while (isRunning)
                createHandler(listenerSocket.accept());
        } catch (IOException e) {
            isRunning = false;
            System.out.println("Errores durante el inicio del server\n" + e.toString());
        }

    }

    @Override
    public void onMessageRecived(ClientHandler client, String message, int messageType) {

    }

    @Override
    public void startListening() {

    }

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

    @Override
    public void removeHandler(ClientHandler client) {

    }

    @Override
    public void createHandler(Socket socket) {

    }

    @Override
    public void broadcastMessage(ClientHandler sender, String message) {

    }

    @Override
    public void onHandlerMessageRecived(ClientHandler client, String message) {

    }

    @Override
    public void onClientMessageRecived(ClientHandler client, String message) {

    }

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
}
