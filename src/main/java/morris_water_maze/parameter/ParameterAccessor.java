package morris_water_maze.parameter;

import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.mouse.MouseParameterAccessor;
import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.histogram.HistogramParameterAccessor;

public interface ParameterAccessor extends SimulationParameterAccessor
{
    boolean isStartingWithGui();
    
    int getNumberOfPics();
    
    double getLowerBoundOfPictureTimeFrame();
    
    double getUpperBoundOfPictureTimeFrame();
    
    int getMaximumTrajectoriesPerPicture();
    
    ImagePainterType getImagePainterTypeForPictureExport();
    
    String getSimulationId();
    
    HistogramParameterAccessor getHistogramParameterAccessor();
    
    double getMouseTrainingLevel();
    
    ImageFileFormat getImageFileFormat();
    
    
}
