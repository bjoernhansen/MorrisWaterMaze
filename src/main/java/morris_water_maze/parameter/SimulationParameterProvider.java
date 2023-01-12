package morris_water_maze.parameter;

public interface SimulationParameterProvider
{
    int getNumberOfSimulations();
    
    boolean isStartingWithGui();
    
    String getSimulationId();
    
}
