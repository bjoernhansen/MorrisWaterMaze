package MorrisWaterMaze.parameter;

public interface ParameterAccessor extends SimulationParameterAccessor
{
    int getNumberOfPics();
    double getLowerBoundOfPictureTimeFrame();
    double getUpperBoundOfPictureTimeFrame();
    int getMaximumTrajectoriesPerPicture();
    String getSimulationId();
    
    default int determineMaximumSwimmingTime(String swimmingTimeAsString)
    {
        int swimmingTime = Integer.parseInt(swimmingTimeAsString);
        return swimmingTime > 0
            ? swimmingTime
            : Integer.MAX_VALUE;
    }
}
