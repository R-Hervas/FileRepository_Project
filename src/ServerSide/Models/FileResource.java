package ServerSide.Models;

import ServerSide.classes.ClientHandler;

import java.io.*;
import java.net.URI;
import java.util.concurrent.Semaphore;

public class FileResource extends File {

    private Semaphore available = new Semaphore(1);
    private ClientHandler clientConnected;

    public FileResource(String pathname) {
        super(pathname);
    }

    public FileResource(String parent, String child) {
        super(parent, child);
    }

    public FileResource(File parent, String child) {
        super(parent, child);
    }

    public FileResource(URI uri) {
        super(uri);
    }

    public boolean isAvailable(){
        return available.availablePermits() == 1? true : false;
    }

    public boolean userConnect(ClientHandler client){
        if(available.tryAcquire()){
            clientConnected = client;
            return true;
        } else {
            return false;
        }
    }

    public boolean userDisconnect(){
        if (clientConnected != null && !this.isAvailable()){
            available.release();
            clientConnected.sendMessage("Ya no tiene acceso al archivo del nombre: " + this.getName());
            return true;
        } else {
            return false;
        }
    }

    public void setFileText(String text) throws IOException {
        FileWriter fileWriter = new FileWriter(this, false);
        fileWriter.write(text);
    }

    public String getFileText() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this));
        StringBuilder fileText = new StringBuilder();
        String textLine;
        while ((textLine = bufferedReader.readLine()) != null){
            fileText.append(textLine);
        }
        return fileText.toString();
    }

}
