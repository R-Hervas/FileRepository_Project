package models;

import javax.print.attribute.standard.PrinterURI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    private final int PUERTO = 6969;
    private final String HOST = "localhost";

    protected final int SERVER = 100;
    protected final int CLIENT = 200;

    protected String name;

    protected Socket socket;
    protected ServerSocket serverSocket;

    protected DataOutputStream dataOutputStream;
    protected DataInputStream dataInputStream;

    public Connection(String Name, int type) throws IOException {
        this.name = name;

        switch (type){
            case CLIENT:

                socket = new Socket(HOST, PUERTO); // Socket para el cliente que conecta con el servidor en el host y puerto especificados

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                break;

            case SERVER:

                serverSocket = new ServerSocket(PUERTO); // Se crea el socket para el servidor en le puerto especificado
                socket = new Socket(); // Se crea el socket para el cliente

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                break;
        }
    }
}
