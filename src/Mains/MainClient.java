package Mains;

import ClientSide.implementations.ChatClient;

import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {

        Scanner sn = new Scanner(System.in);

        ChatClient chatClient = new ChatClient("Chat_Client");

        chatClient.connect("localhost", 6969);

        while (true)
            chatClient.sendMessage(sn.nextLine());
    }
}
