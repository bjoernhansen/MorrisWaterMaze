package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterAccessor;

public interface SimulationParameterAccessor
{
    int getNumberOfSimulations();
    
    MouseParameterAccessor getMouseParameterAccessor();
}
