package ServerSide.classes;

import ServerSide.Models.FileResource;
import ServerSide.helpers.TxtFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the basic methods to manipulate the files in server repository. As there`s going to be only one repository,
 * the class has been constructed with a Singleton Patern
 */
public class RepoGestor {

    /**
     * List of FileResources in the repository folder
     */
    private final ArrayList<FileResource> fileResources = new ArrayList<>();
    /**
     * Unique instance of RepoGestor (Singleton Patern)
     */
    private static RepoGestor repoGestor;

    /**
     * As the unique instance is created the repository loads all files in its list
     */
    private RepoGestor() {
        this.loadFilesInRepository();
    }

    /**
     * If there`s not an instance creates one
     * @return return the created instance or null if there`s already one
     */
    public static RepoGestor getRepoGestorInstance() {
        if (repoGestor == null )
            repoGestor = new RepoGestor();
        return repoGestor;
    }

    /**
     * Loads all the files in the folder {@code Files/Server_Files} into the list {@code filesInRepository}
     */
    public void loadFilesInRepository() {
        File repositoryFolder = new File("Files/Server_Files");
        File[] filesInRepository = repositoryFolder.listFiles(new TxtFileFilter());

        fileResources.clear();

        if (filesInRepository != null) {
            for (File txtFile :
                    filesInRepository) {
                fileResources.add(new FileResource(txtFile.getPath()));
            }

            System.out.println(getFileListAsString());
        } else {
            System.out.println("Repositorio vacio");
        }
    }

    /**
     * @return Returns the file list as a string with the format:
     *
     * {@code file_name.txt|file_name2.txt|}
     */
    public String getFileListAsString() {
        StringBuilder fileList = new StringBuilder();
        if (fileResources.size() != 0) {
            for (File file : fileResources){
                fileList.append(file.getName()).append("|");
            }
            return fileList.toString();
        }
        return "There's no files in the repository.";
    }

    /**
     * Creates a file in repository folder
     * @param fileName
     * @return The created file as a FileResource
     * @throws IOException - If there was an error creating the file
     */
    public FileResource createFile(String fileName) throws IOException {
        FileResource newFile = null;
        if (this.findFileByName(fileName) == null && !fileName.equals("")) {
            newFile = new FileResource("Files/Server_Files/" + fileName);
            if (newFile.createNewFile())
                fileResources.add(newFile);
            else
                newFile = null;
        }
        return newFile;
    }

    /**
     * Finds a file by its name
     * @param fileName
     * @return If it finds the file a FileResource, null if it`s not
     */
    public FileResource findFileByName(String fileName){
        for (FileResource fileResource : fileResources) {
            if (fileResource.getName().equals(fileName))
                return fileResource;
        }
        return null;
    }


}
