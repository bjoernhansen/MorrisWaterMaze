package morris_water_maze.report.histogram;

import morris_water_maze.report.ImageFileFormat;


public interface HistogramParameter
{
    ImageFileFormat getImageFileFormat();
    
    double getMouseTrainingLevel();
    
    int getNumberOfSimulations();
    
    boolean isPublishable();
    
    double getBinsPerSecond();
    
    double getMaximumDisplayedSearchTimeDuration();
}
