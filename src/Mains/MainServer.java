package Mains;

import ServerSide.classes.ServerBase;
import ServerSide.implementations.BasicEchoServer;

public class MainServer {



    public static void main(String[] args) {
        BasicEchoServer basicEchoServer = new BasicEchoServer("echo_server", 6969);

        basicEchoServer.startListening();
    }
}
