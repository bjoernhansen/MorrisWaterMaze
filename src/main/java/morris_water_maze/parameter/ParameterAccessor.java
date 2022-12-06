package morris_water_maze.parameter;

import morris_water_maze.graphics.painter.image.ImagePainterType;

public interface ParameterAccessor extends SimulationParameterAccessor
{
    boolean isStartingWithGui();
    
    int getNumberOfPics();
    
    double getLowerBoundOfPictureTimeFrame();
    
    double getUpperBoundOfPictureTimeFrame();
    
    int getMaximumTrajectoriesPerPicture();
    
    ImagePainterType getImagePainterTypeForPictureExport();
    
    String getSimulationId();
    
    
    default int determineMaximumSwimmingTime(String swimmingTimeAsString)
    {
        int swimmingTime = Integer.parseInt(swimmingTimeAsString);
        return swimmingTime > 0
            ? swimmingTime
            : Integer.MAX_VALUE;
    }
}
