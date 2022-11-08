package MorrisWaterMaze.parameter;

public interface ParameterAccessor extends SimulationParameterAccessor
{
    int getNumberOfPics();
    double getLowerBoundOfPictureTimeFrame();
    double getUpperBoundOfPictureTimeFrame();
    int getMaximumTrajectoriesPerPicture();
    String getSimulationId();
}
