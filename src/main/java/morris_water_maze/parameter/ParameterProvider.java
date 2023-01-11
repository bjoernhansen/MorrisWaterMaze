package morris_water_maze.parameter;

import morris_water_maze.report.ImageFileParameterProvider;
import morris_water_maze.report.histogram.HistogramParameterProvider;

public interface ParameterProvider extends SimulationParameterProvider
{
    boolean isStartingWithGui();
    
    String getSimulationId();
    
    ImageFileParameterProvider getImageFileParameterAccessor();
}
