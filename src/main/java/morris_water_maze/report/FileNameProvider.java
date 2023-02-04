package morris_water_maze.report;

import morris_water_maze.util.NecessaryDirectoriesReporter;

import java.util.Arrays;
import java.util.List;


public final class FileNameProvider implements NecessaryDirectoriesReporter
{
    private static final String
        LOG_DIRECTORY = "logs/";
    
    private final static String
        USED_PARAMETERS_FILE_PATH = "src/main/resources/parameter.properties";
    
    private final String
        subDirectory;
    
    private final String
        finalReportPath;
    
    private final String
        usedParametersCopyPath;
 
        
    
    public FileNameProvider(String simulationId)
    {
        subDirectory = LOG_DIRECTORY + simulationId + "/";
        finalReportPath = subDirectory + "search_times.txt";
        usedParametersCopyPath = subDirectory + "usedParameters.txt";
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
    
    public String getUsedParametersFilePath()
    {
        return USED_PARAMETERS_FILE_PATH;
    }
    
    public String getUsedParametersCopyPath()
    {
        return usedParametersCopyPath;
    }
}
