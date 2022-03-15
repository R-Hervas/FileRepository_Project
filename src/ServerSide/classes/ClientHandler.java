package ServerSide.classes;

import ServerSide.Models.FileResource;
import ServerSide.interfaces.ClientListener;
import ServerSide.interfaces.ServerInterface;
import ServerSide.interfaces.TextFileServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientHandler extends Thread {

    private static String default_nickname = "UnamedClient " + Math.random();

    private Socket socket;
    private ClientListener listener;
    private String clientName;
    private boolean isRunning;
    private FileResource fileResource;

    public ClientHandler(Socket socket, ClientListener listener) {

        this.socket = socket;
        this.isRunning = true;
        this.listener = listener;

        clientName = default_nickname;

        sendMessage(ServerInterface.SERVER_CONNECTED);
    }

    @Override
    public void run() {


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (isRunning) {
                String input = in.readLine();
                if (input != null) {
                    handleMessage(input);
                } else {
                    notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);
                }
            }
        } catch (IOException e) {
            listener.onMessageRecived(this, ClientListener.CLIENT_NOTIFY_ERROR, ClientListener.MESSAGE_TYPE_NOTIFY);
        }
    }

     public void sendMessage(String msg) {
        try {
            new PrintWriter(socket.getOutputStream(), true).println(msg);
        } catch (IOException e) {
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);
        }
    }

    private void handleMessage(String msg) {

        listener.onMessageRecived(this, msg, ClientListener.MESSAGE_TYPE_CLIENT);

        if(msg.equals(ClientListener.CLIENT_NOTIFY_DISCONNECT))
            notifyClientDisconnect(ClientListener.CLIENT_NOTIFY_DISCONNECT);

        if(msg.contains(ClientListener.CLIENT_MESSAGE_NAME))
            this.clientName = msg.replace(ClientListener.CLIENT_MESSAGE_NAME, "");

    }


    private void notifyClientDisconnect(String disconnectMsg) {
        listener.onMessageRecived(this, disconnectMsg, ClientListener.MESSAGE_TYPE_NOTIFY);
    }

    public void dispose() {

        try {
            if (socket != null) {
                socket.close();
                isRunning = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getClientName() {
        return (clientName.equals(default_nickname) ? getIpAddress() : clientName);
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIpAddress() {
        return this.socket.getRemoteSocketAddress().toString();
    }

    public void setConnectionWithFile(FileResource fileResource) {
        if (fileResource.connect())
            this.fileResource = fileResource;
        else
            this.sendMessage("Recurso no disponible");
    }

    public void disconnectFromFile(){
        if (this.fileResource != null){
            fileResource.disconnect();
            fileResource = null;
        }
    }

    public void writeInResource(String newFileText){
        if (this.isConnectedToResource()) {
            try {
                this.fileResource.setFileText(newFileText);
            } catch (IOException e) {
                sendMessage(TextFileServer.CLIENT_ERROR + "Error al acceder al archivo");
                this.disconnectFromFile();
            }
        } else {
            sendMessage(TextFileServer.CLIENT_ERROR + "El cliente no esta asociado a ningun archivo");
        }
    }

    public String readFromResource(){
        if (this.isConnectedToResource()) {
            try {
                return this.fileResource.getFileText();
            } catch (IOException e) {
                sendMessage(TextFileServer.CLIENT_ERROR + "Error al acceder al archivo");
                this.disconnectFromFile();
                return "";
            }
        } else
            return "";
    }

    public boolean isConnectedToResource(){return (this.fileResource != null);}
}
