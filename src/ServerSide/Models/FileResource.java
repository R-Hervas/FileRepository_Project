package ServerSide.Models;

import java.io.*;
import java.util.concurrent.Semaphore;

/**
 * Extends a file adding it a semaphore preventing collisions at accessing it and
 * the methods needed to manage that semaphore. The main reason for a semaphore for
 * every file is to avoid update overwriting.
 */
public class FileResource extends File {

    /**
     * Establish if the file is being used or not
     */
    private final Semaphore available = new Semaphore(1);

    /**
     * Constructor of the superclass
     * @param pathname - A pathname string
     */
    public FileResource(String pathname) {
        super(pathname);
    }

    /**
     * Acquires the semaphore of the FileResource if it's possible
     * @return true if the resource is available, false otherwise
     */
    public boolean connect() {
        return available.tryAcquire();
    }

    /**
     * Release the semaphore of the FileResource
     */
    public void disconnect() {
        available.release();
    }

    /**
     * Overwrites a String in the txt file associated
     * @param text - Text to overwrite in the file
     * @throws IOException - If file can't be accessed
     */
    public void setFileText(String text) throws IOException {
        PrintWriter writer = new PrintWriter(this); //TODO comprobar que esto sobreescribe
        writer.print(text);
        writer.close();
    }

    /**
     * Gets the text of the txt file associated to the FileResource
     * @return String - The file's text
     * @throws IOException - If file can't be accessed
     */
    public String getFileText() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this));
        StringBuilder fileText = new StringBuilder();
        String textLine;
        while ((textLine = bufferedReader.readLine()) != null){
            fileText.append(textLine);
        }
        bufferedReader.close();
        return fileText.toString();
    }

    public boolean isAvaliable(){return available.availablePermits() >= 1; }

}
