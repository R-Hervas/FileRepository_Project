package ClientSide.interfaces;

public interface ClientMessageListener {
    public static int MESSAGE_TYPE_SERVER = 1;
    public static int MESSAGE_TYPE_HANDLER = 2;

    public abstract void onMessageRecived(String message, int messageType);
}
