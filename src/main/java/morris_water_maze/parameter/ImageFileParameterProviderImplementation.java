package morris_water_maze.parameter;

import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.ImageFileParameterProvider;

import java.util.Properties;


public class ImageFileParameterProviderImplementation implements ImageFileParameterProvider
{
    private final int
        numberOfPics;
    
    private final double
        lowerBoundOfPictureTimeFrame;
    
    private final double
        upperBoundOfPictureTimeFrame;
    
    private final int
        maximumTrajectoriesPerPicture;
    
    private final ImagePainterType
        imagePainterTypeForPictureExport;
    
    
    public ImageFileParameterProviderImplementation(Properties parameter)
    {
        numberOfPics = Integer.parseInt(parameter.getProperty("numberOfPics", "0"));
        lowerBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("lowerBoundOfPictureTimeFrame", "10.74"));
        upperBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("upperBoundOfPictureTimeFrame", "25.76"));
        maximumTrajectoriesPerPicture = Integer.parseInt(parameter.getProperty("maximumTrajectoriesPerPicture", "25"));
        imagePainterTypeForPictureExport = Boolean.parseBoolean(parameter.getProperty("isUsingSvgAsImageFileFormat", "false"))
            ? ImagePainterType.SVG
            : ImagePainterType.DEFAULT;
    }
    
    @Override
    public int getNumberOfPics()
    {
        return numberOfPics;
    }
    
    @Override
    public double getLowerBoundOfPictureTimeFrame()
    {
        return lowerBoundOfPictureTimeFrame;
    }
    
    @Override
    public double getUpperBoundOfPictureTimeFrame()
    {
        return upperBoundOfPictureTimeFrame;
    }
    
    @Override
    public int getMaximumTrajectoriesPerPicture()
    {
        return maximumTrajectoriesPerPicture;
    }
    
    @Override
    public ImagePainterType getImagePainterTypeForPictureExport()
    {
        return imagePainterTypeForPictureExport;
    }
    
    @Override
    public ImageFileFormat getImageFileFormat()
    {
        return imagePainterTypeForPictureExport.getImageFileFormat();
    }
}
