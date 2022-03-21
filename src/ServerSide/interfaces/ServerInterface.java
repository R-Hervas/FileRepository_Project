package ServerSide.interfaces;

import ServerSide.classes.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

/**
 * Stablish the main communication protocol for server and it's basic methods
 */
public interface ServerInterface extends ClientListener{

    /**
     * Communication protocol for the server to communicate its disconnection to a client and prevent it from
     * crashing
     */
    String SERVER_DISCONNECTED = "#SERVER_DISCONNECTED";

    /**
     * Communication protocol for the server to communicate it is connected to a client
     */
    String SERVER_CONNECTED = "#SERVER_CONNECTED";


    /**
     * Defines the behaviour of the server when it starts listening
     */
    void startListening();

    /**
     * Defines the behaviour of the server when it stops listening
     * @throws IOException
     */
    @SuppressWarnings("unused")
    void stopListening() throws IOException;

    /**
     * Removes a client listener from the listening list
     * @param client - client to remove from the listening list
     */
    void removeHandler(ClientHandler client); // !

    /**
     * Creates a listener for a client
     * @param socket - Socket for communication
     */
    void createHandler(Socket socket);

    /**
     * Sends a message for all clients connected from one client
     * @param sender - client that sent the message
     * @param message
     */
    void broadcastMessage(ClientHandler sender, String message); // ! - Pass null as ClientHandler to broadcast the message to everyone

    /**
     * Manages a status message from the client
     * @param client
     * @param message
     */
    void onHandlerMessageReceived(ClientHandler client, String message); // !

    /**
     * Manages a text message from the client
     * @param client
     * @param message
     */
    void onClientMessageReceived(ClientHandler client, String message);

    /**
     * Defines the behaviour when a clietn is connected
     * @param client
     */
    void onClientConnected(ClientHandler client);

    /**
     * Defines the behaviour when a clietn is disconnected
     * @param client
     */
    void onClientDisconnected(ClientHandler client);

    /**
     * Defines behaviour when the server starts listening
     * @param port
     */
    void onListeningStarted(int port);

    /**
     * Defines bahaviour when the server stops listening
     */
    void onListeningStopped();
}
