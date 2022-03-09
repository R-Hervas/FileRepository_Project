package ClientSide.interfaces;

import java.io.IOException;

public interface ClientInterface extends ClientMessageListener{

    public abstract void connect(String ip, int port) throws IOException;

    public abstract void sendMessage(String message) throws IOException;

    public abstract void onDisconnected();

    public abstract void onConnected();

    public abstract void onHandlerMessageRecived(String message); // !

    public abstract void onClientMessageRecived(String message);

    public abstract void onMessageSent(String message);

}
