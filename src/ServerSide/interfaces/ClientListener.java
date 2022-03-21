package ServerSide.interfaces;

import ServerSide.classes.ClientHandler;

/**
 * Interface for every client manager. Defines the communication protocol for client
 * communication
 */
public interface ClientListener {

    /**
     * Regular message.
     */
    int MESSAGE_TYPE_CLIENT = 1;
    /**
     * Notification
     */
    int MESSAGE_TYPE_NOTIFY = 2;
    /**
     * Other
     */
    @SuppressWarnings("unused")
    int MESSAGE_TYPE_OTHER = 3;

    /**
     * Protocol for the client to notify an error
     */
    String CLIENT_NOTIFY_ERROR = "#CLIENT_IO_ERROR";
    /**
     * Protocol for the client to notify its disconnection to the server
     */
    String CLIENT_NOTIFY_DISCONNECT = "#CLIENT_DISCONNECTED";
    /**
     * Protocol to define the nickname of the client
     */
    String CLIENT_MESSAGE_NAME = "#NICKNAME";

    /**
     * Manages a message sent by the client to the server
     * @param client - who sent the message
     * @param messageType - defines the behaviour of the server
     * @param message
     */
    void onMessageReceived(ClientHandler client, String message, int messageType);
}
