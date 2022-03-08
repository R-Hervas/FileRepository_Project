package ServerSide.classes;

import ServerSide.interfaces.ClientListener;
import ServerSide.interfaces.ServerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientHandler extends Thread {

    private static String default_nickname = "UnamedClient " + Math.random();

    private Socket socket;
    private ClientListener listener;
    private String clientName;
    private boolean isRunning;

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
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg) {
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
            this.clientName = msg.replace(ClientListener.CLIENT_MESSAGE_NAME, ""); // WTF THIS LINE??

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
}
