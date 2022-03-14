package ServerSide.classes;

import ServerSide.Models.FileResource;
import ServerSide.helpers.TxtFileFilter;

import java.io.File;
import java.util.ArrayList;

public class RepoGestor {

    private ArrayList<FileResource> fileResources = new ArrayList<>();
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

        if (fileResources != null)
            fileResources.clear();

        for (File txtFile :
                filesInRepository) {
            fileResources.add(new FileResource(txtFile.getPath()));
        }

        System.out.println(getFileList());
    }

    public String getFileList() {
        StringBuilder fileList = new StringBuilder();
        if (fileResources.size() != 0) {
            for (File file : fileResources){
                fileList.append(file.getName()).append("\n");
            }
            return fileList.toString();
        }
        return "There's no files in the repository.";
    }

    public FileResource createFile(String fileName) {
        FileResource newFile = null;
        if (this.findFileName(fileName) == null)
            fileResources.add(newFile = new FileResource(fileName));
        return newFile;
    }

    public FileResource findFileName(String fileName){
        for (FileResource fileResource : fileResources) {
            if (fileResource.getName().equals(fileName))
                return fileResource;
        }
        return null;
    }
}
