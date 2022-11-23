package morris_water_maze.parameter;

import morris_water_maze.model.StartingSide;

public interface MouseParameterAccessor extends GeneralParameterAccessor
{
    int getMaximumMouseSwimmingDuration();
    double getMouseTrainingLevel();
    double getStepLengthBias();
    StartingSide getStartingSide();
    double mouseSpeed();
}
