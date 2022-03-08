package ServerSide.interfaces;

import ServerSide.classes.ClientHandler;

public interface ClientListener {

    public static int MESSAGE_TYPE_CLIENT = 1;
    public static int MESSAGE_TYPE_NOTIFY = 2;
    public static int MESSAGE_TYPE_OTHER = 3;

    public static String CLIENT_NOTIFY_ERROR = "#CLIENT_IO_ERROR";
    public static String CLIENT_NOTIFY_DISCONNECT = "#CLIENT_DISCONNECTED";
    public static String CLIENT_MESSAGE_NAME = "#NICKNAME";

    public abstract void onMessageRecived(ClientHandler client, String message, int messageType);
}
