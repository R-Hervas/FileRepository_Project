package front;

public interface OnFileExplorerClientListener {
	
	public static String CLIENT_GET_FILE = "#CLIENT_GET_";
    public static String CLIENT_POST_FILE = "#CLIENT_POST_";
    public static String CLIENT_CREATE_FILE = "#CLIENT_CREATE_";
    public static String CLIENT_LIST = "#CLIENT_LIST_";
    public static String CLIENT_RELEASE_FILE = "#CLIENT_RELEASE_FILE_";
    public static String CLIENT_DELETE = "#CLIENT_DELETE_";

    public static String CLIENT_NOTIFICATION = "#CLIENT_NOTIFICATION_";
    public static String CLIENT_ERROR = "#CLIENT_ERROR_";
   
	public void notifyNewFile(String file);
	public void notifyUpdatedFile(String file);
	public void notifyDeletedFile(String file);
	public void notifyReleasedFile();
	
	public void requestFile(String file);
	public void requestFileList();
	
	
}
