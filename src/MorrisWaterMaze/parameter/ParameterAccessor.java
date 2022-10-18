package MorrisWaterMaze.parameter;

public interface ParameterAccessor
{
    int getNumberOfSimulations();
    int getMaximumMouseSwimmingTime();
    double getMouseTrainingLevel();
    double getStepLengthBias();
    boolean isMouseStartingPositionLeft();
    double mouseSpeed();
    boolean isStartingWithGui();
    int getNumberOfPics();
    double getLowerBoundOfPictureTimeFrame();
    double getUpperBoundOfPictureTimeFrame();
    int getMaximumTrajectoriesPerPicture();
    String getFilename();
}
