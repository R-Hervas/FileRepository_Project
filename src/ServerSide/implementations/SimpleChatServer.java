package ServerSide.implementations;

import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;

public class SimpleChatServer extends ServerBase {

    public SimpleChatServer(String name, int port) {
        super(name, port);
    }

    @Override
    public void onClientMessageRecived(ClientHandler client, String message) {
        String formattedMessage = getFormattedMessage(client, message);
        broadcastMessage(client, formattedMessage);
        System.out.println(formattedMessage);
    }

    @Override
    public void onHandlerMessageRecived(ClientHandler client, String message) {
        super.onHandlerMessageRecived(client, message);
        System.out.println("#" + message);
    }
}
