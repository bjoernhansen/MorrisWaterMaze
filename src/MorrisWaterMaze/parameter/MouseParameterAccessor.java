package MorrisWaterMaze.parameter;

public interface MouseParameterAccessor extends GeneralParameterAccessor
{
    int getMaximumMouseSwimmingTime();
    double getMouseTrainingLevel();
    double getStepLengthBias();
    boolean isMouseStartingPositionLeft();
    double mouseSpeed();
}
