package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterProvider;
import morris_water_maze.report.ImageFileParameterProvider;
import morris_water_maze.report.histogram.HistogramParameterProvider;

public interface SimulationParameterProvider
{
    int getNumberOfSimulations();
    
    boolean isStartingWithGui();
    
    String getSimulationId();
    
}
