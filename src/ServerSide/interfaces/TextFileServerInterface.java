package ServerSide.interfaces;

/**
 * Defines the communication protocol with the client
 */
public interface TextFileServerInterface {

    /**
     * Communication protocol to connect to a txt file and receive its information
     * (If the client is already connected to a file the command won`t work)
     * <ol>
     *     <li>
     * Sent by client: {@code #CLIENT_GET_archivo.txt}
     * </li>
     * <li>
     * Server Response: {@code #CLIENT_GET_En un lugar de la mancha}
     * </li>
     * </ol>
     * See Also: #CLIENT_NOTIFICATION, #CLIENT_ERROR
     */
    String CLIENT_GET_FILE = "#CLIENT_GET_";

    /**
     * Communication protocol for the client to overwrite the file they have
     * connected to and disconnect (If the client is not connected to any file the
     * command won´t work)
     * <ul>
     * <li>
     * Sent by client: {@code #CLIENT_POST_En un lugar de la mancha}
     * </li>
     * <li>
     * Response from the server: {@code #CLIENT_NOTIFICATION_ or #CLIENT_ERROR_}
     * </li>
     * </ul>
     *
     * See Also: #CLIENT_NOTIFICATION, #CLIENT_ERROR
     */
    String CLIENT_POST_FILE = "#CLIENT_POST_";

    /**
     * Communication protocol for the client to create a file in the repository with
     * a given name
     * <ul>
     * <li>
     * Sent by client: {@code #CLIENT_CREATE_FILE_otro_archivo.txt}
     * </li>
     * <li>
     * Response from the server: {@code #CLIENT_NOTIFICATION_ or #CLIENT_ERROR_}
     * </li>
     * </ul>
     *
     * See Also: #CLIENT_NOTIFICATION, #CLIENT_ERROR
     */
    String CLIENT_CREATE_FILE = "#CLIENT_CREATE_";

    /**
     * Communication protocol for the client to ask for the list of files in the
     * file repository
     * <ul>
     * <li>
     * Sent by client: {@code #CLIENT_LIST_}
     * </li>
     * <li>
     * Response from the server: {@code #CLIENT_LIST_archivo.txt|otro_archivo.txt|}
     * </li>
     * </ul>
     *
     * See Also: #CLIENT_NOTIFICATION, #CLIENT_ERROR
     */
    String CLIENT_GET_FILE_LIST = "#CLIENT_LIST_";


    String CLIENT_RELEASE_FILE = "#CLIENT_RELEASE_FILE_";


    /**
     * Protocol for notification communication between server and client
     */
    String CLIENT_NOTIFICATION = "#CLIENT_NOTIFICATION_";
    /**
     * Protocol for error communication between server and client
     */
    String CLIENT_ERROR = "#CLIENT_ERROR_";

}
