package Mains;

import ServerSide.implementations.TextEditorServer;

public class MainServer {



    public static void main(String[] args) {


//        BasicEchoServer basicEchoServer = new BasicEchoServer("echo_server", 6969);
//
//        basicEchoServer.startListening();

//        SimpleChatServer simpleChatServer = new SimpleChatServer("Chat_Server", 6969);
//
//        simpleChatServer.startListening();

        TextEditorServer textEditorServer = new TextEditorServer("TextEditorServer", 6969);

        textEditorServer.startListening();
    }
}
