package MorrisWaterMaze.util;

import java.io.File;


public final class DirectoryCreator
{
    private final NecessaryDirectoriesReporter
        necessaryDirectoriesReporter;
        
    public DirectoryCreator(NecessaryDirectoriesReporter necessaryDirectoriesReporter)
    {
        this.necessaryDirectoriesReporter = necessaryDirectoriesReporter;
    }
    
    public void makeDirectories()
    {
        necessaryDirectoriesReporter.getNecessaryDirectories()
                                    .forEach(this::makeDirectory);
    }
    
    private void makeDirectory(String directoryName)
    {
        File pictureDirectory = new File(directoryName);
        pictureDirectory.mkdir();
    }
}
