package ServerSide.classes;

import ServerSide.Models.FileResource;
import ServerSide.helpers.TxtFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RepoGestor {

    private final ArrayList<FileResource> fileResources = new ArrayList<>();
    private static RepoGestor repoGestor;

    private RepoGestor() {
        this.loadFilesInRepository();
    }
    
    public static RepoGestor getRepoGestorInstance() {
        if (repoGestor == null )
            repoGestor = new RepoGestor();
        return repoGestor;
    }

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

    public FileResource createFile(String fileName) throws IOException {
        FileResource newFile = null;
        if (this.findFileByName(fileName) == null && !fileName.equals("")) {
            newFile = new FileResource("Files/Server_Files/" + fileName); //TODO Avisar de como ha ido la creacion
            if (newFile.createNewFile())
                fileResources.add(newFile);
            else
                newFile = null;
        }
        return newFile;
    }

    public FileResource findFileByName(String fileName){
        for (FileResource fileResource : fileResources) {
            if (fileResource.getName().equals(fileName))
                return fileResource;
        }
        return null;
    }


}
