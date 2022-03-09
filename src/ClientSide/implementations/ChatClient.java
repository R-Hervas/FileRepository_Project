package ClientSide.implementations;

import ClientSide.classes.BaseClient;

public class ChatClient extends BaseClient {

    public ChatClient(String name) {
        super(name);
    }

    @Override
    public void onConnected() {
        super.onConnected();
        System.out.println("#Connection to server");
    }

    @Override
    public void onDisconnected() {
        System.out.println("#Disconnection to server");
    }

    @Override
    public void onClientMessageRecived(String message) {
        System.out.println("Server: " + message);
    }

    @Override
    public void onMessageSent(String message) {
        System.out.println("Tu: " + message);
    }
}
