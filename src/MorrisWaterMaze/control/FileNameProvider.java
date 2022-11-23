package MorrisWaterMaze.control;

import MorrisWaterMaze.util.NecessaryDirectoriesReporter;
import MorrisWaterMaze.parameter.ParameterAccessor;

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
        
    
    public FileNameProvider(ParameterAccessor parameterAccessor)
    {
        String simulationId = parameterAccessor.getSimulationId();
        subDirectory = LOG_DIRECTORY + simulationId + "/";
        finalReportPath = subDirectory + simulationId + ".txt";
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
        return Arrays.asList(subDirectory, LOG_DIRECTORY);
    }
}