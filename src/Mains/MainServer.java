package Mains;

import ServerSide.classes.ServerBase;
import ServerSide.implementations.BasicEchoServer;
import ServerSide.implementations.SimpleChatServer;

public class MainServer {



    public static void main(String[] args) {
//        BasicEchoServer basicEchoServer = new BasicEchoServer("echo_server", 6969);
//
//        basicEchoServer.startListening();

        SimpleChatServer simpleChatServer = new SimpleChatServer("Chat_Server", 6969);

        simpleChatServer.startListening();
    }
}
