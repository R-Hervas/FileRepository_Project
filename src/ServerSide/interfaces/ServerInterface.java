package ServerSide.interfaces;

import ServerSide.classes.ClientHandler;

import java.io.IOException;
import java.net.Socket;

public interface ServerInterface extends ClientListener {

    public static String SERVER_DISCONNECTED = "#SERVER_DISCONNECTED";
    public static String SERVER_CONNECTED = "#SERVER_CONNECTED";

    public void startListening();

    public abstract void stopListening() throws IOException;

    public abstract void removeHandler(ClientHandler client); // !

    public abstract void createHandler(Socket socket); // !

    public abstract void broadcastMessage(ClientHandler sender, String message); // ! - Pass null as ClientHandler to broadcast the message to everyone

    public abstract void onHandlerMessageRecived(ClientHandler client, String message); // !

    public abstract void onClientMessageRecived(ClientHandler client, String message);

    public abstract void onClientConnected(ClientHandler client);

    public abstract void onClientDisconnected(ClientHandler client);

    public abstract void onListeningStarted(int port);

    public abstract void onListeningStopped();
}
