package models;

import java.io.IOException;

public class Server extends Connection implements Runnable{


    public Server(String Name, int type) throws IOException {
        super(Name, type);
    }

    public void startServer() throws IOException {

        socket = serverSocket.accept();

        this.run();

        startServer();

    }


    @Override
    public void run() {



    }
}
