package MorrisWaterMaze.parameter;

import MorrisWaterMaze.model.StartingSide;

public interface MouseParameterAccessor extends GeneralParameterAccessor
{
    int getMaximumMouseSwimmingTime();
    double getMouseTrainingLevel();
    double getStepLengthBias();
    StartingSide getStartingSide();
    double mouseSpeed();
}
