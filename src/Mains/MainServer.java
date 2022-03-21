package Mains;

import ServerSide.implementations.TextEditorServer;

public class MainServer {


    public static void main(String[] args) {

        TextEditorServer textEditorServer = new TextEditorServer("TextEditorServer", 6969);

        textEditorServer.startListening();

    }
}
