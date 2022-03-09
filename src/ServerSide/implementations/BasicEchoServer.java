package ServerSide.implementations;

import ServerSide.classes.ClientHandler;
import ServerSide.classes.ServerBase;

public class BasicEchoServer extends ServerBase {

    public BasicEchoServer(String name, int port) {
        super(name, port);
    }

    @Override
    public void onClientMessageRecived(ClientHandler client, String message) {
        client.sendMessage(message);
        System.out.println(message);
    }

    @Override
    public void onListeningStarted(int port) {
        System.out.println("EchoServer started, listening on port: " + port);
    }
}
