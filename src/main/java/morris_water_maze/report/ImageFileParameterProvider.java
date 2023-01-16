package morris_water_maze.report;

import morris_water_maze.graphics.painter.image.ImagePainterType;

public interface ImageFileParameterProvider
{
    int getNumberOfPics();
    
    double getLowerBoundOfPictureTimeFrame();
    
    double getUpperBoundOfPictureTimeFrame();
    
    int getMaximumTrajectoriesPerPicture();
    
    ImagePainterType getImagePainterTypeForPictureExport();
    
    ImageFileFormat getImageFileFormat();
}
