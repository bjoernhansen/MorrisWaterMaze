package morris_water_maze.report;

import morris_water_maze.util.NecessaryDirectoriesReporter;

import java.util.Arrays;
import java.util.List;


public final class FileNameProvider implements NecessaryDirectoriesReporter
{
    private static final String
        LOG_DIRECTORY = "logs/";
    
    private final String
        subDirectory;
    
    private final String
        finalReportPath;
    
    
    public FileNameProvider(String simulationId)
    {
        subDirectory = LOG_DIRECTORY + simulationId + "/";
        finalReportPath = subDirectory + "search_times.txt";
    }
    
    public String getSubDirectory()
    {
        return subDirectory;
    }
    
    public String getFinalReportPath()
    {
        return finalReportPath;
    }
    
    @Override
    public List<String> getNecessaryDirectories()
    {
        return Arrays.asList(LOG_DIRECTORY, subDirectory);
    }
}