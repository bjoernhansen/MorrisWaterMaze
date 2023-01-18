package morris_water_maze.model.mouse;

import morris_water_maze.model.StartingSide;


public interface MouseParameter
{
    double getMaximumMouseSwimmingDuration();
    
    double getMouseTrainingLevel();
    
    double getStepLengthBias();
    
    StartingSide getStartingSide();
    
    double mouseSpeed();
}
