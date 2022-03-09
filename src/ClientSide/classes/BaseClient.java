package ClientSide.classes;

import ClientSide.interfaces.ClientInterface;
import ClientSide.interfaces.ClientMessageListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BaseClient implements ClientInterface {

    private String name;
    private Socket socket = null;
    private ClientReciever clientReciever;
    private boolean isConnected;

    public BaseClient(String name) {
        this.name = name;
    }

    @Override
    public void connect(String ip, int port) {

        try {
            socket = new Socket(ip, port);
            clientReciever = new ClientReciever(this, socket);
            clientReciever.start();
            isConnected = true;
            onConnected();

        } catch (IOException e) {
            clientReciever.dispose();
        }

    }

    @Override
    public void sendMessage(String message) {
        if (socket != null){
            try {
                new PrintWriter(socket.getOutputStream(), true).println(message);
                onMessageSent(message);
            } catch (IOException e) {
                if (clientReciever != null)
                    clientReciever.dispose();
                clientReciever = null;
            }

        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnected() {
        sendMessage("#NICKNAME " + name);
    }

    @Override
    public void onHandlerMessageRecived(String message) {
        if (message.equals(ClientReciever.MESSAGE_DISCONNECTED)){
            if (clientReciever != null)
                this.clientReciever.dispose();
            isConnected = false;
            onDisconnected();
        }
    }

    @Override
    public void onClientMessageRecived(String message) {

    }

    @Override
    public void onMessageSent(String message) {

    }

    @Override
    public void onMessageRecived(String message, int messageType) {
        if (messageType == ClientMessageListener.MESSAGE_TYPE_SERVER)
            onClientMessageRecived(message);
        else if (messageType == ClientMessageListener.MESSAGE_TYPE_HANDLER)
            onHandlerMessageRecived(message);
    }
}
