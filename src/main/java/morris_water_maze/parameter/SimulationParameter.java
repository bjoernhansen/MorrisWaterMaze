package morris_water_maze.parameter;

public interface SimulationParameter
{
    int getNumberOfSimulations();
    
    boolean isStartingWithGui();
    
    String getSimulationId();
    
    boolean isReportingSimulationProgress();
    
}
