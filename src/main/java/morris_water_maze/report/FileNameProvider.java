package morris_water_maze.report;

import morris_water_maze.util.NecessaryDirectoriesReporter;
import morris_water_maze.parameter.ParameterAccessor;

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
