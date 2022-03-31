package Mains;

import ClientSide.implementations.FileRepositoryClient;

public class MainClient {

    public static void main(String[] args) {

    	FileRepositoryClient client = new FileRepositoryClient("Chris");
    	client.connect("localhost", 6969);
    }
}
