package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterProvider;
import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.histogram.HistogramParameterProvider;

import java.util.Properties;


public class HistogramParameterProviderImplementation implements HistogramParameterProvider
{
    private final double
        displayedSearchDurationCap;
    
    private final double
        binsPerSecond;
    
    private final boolean
        isPublishable;
    
    private final double
        mouseTrainingLevel;
    
    private final int
        numberOfSimulations;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    public HistogramParameterProviderImplementation(Properties parameter, ParameterProviderGenerator parameterProviderGenerator)
    {
        binsPerSecond = Double.parseDouble(parameter.getProperty("binsPerSecond", "5.0"));
        isPublishable = Boolean.parseBoolean(parameter.getProperty("isPublishable", "true"));
    
        numberOfSimulations = parameterProviderGenerator.getSimulationParameterProvider()
                                                        .getNumberOfSimulations();
    
        MouseParameterProvider mouseParameterAccessor = parameterProviderGenerator.getMouseParameterAccessor();
        mouseTrainingLevel = mouseParameterAccessor.getMouseTrainingLevel();
        displayedSearchDurationCap = calculateDisplayedSearchDurationCap(parameter, mouseParameterAccessor.getMaximumMouseSwimmingDuration());
    
        imageFileFormat = parameterProviderGenerator.getImageFileParameterAccessor()
                                                    .getImageFileFormat();
        validate();
    }
    
    private void validate()
    {
        if(displayedSearchDurationCap <= 0)
        {
            throw new IllegalArgumentException("Illegal argument: displayed search duration cap does not exceed 0.0");
        }
        if(binsPerSecond <= 0)
        {
            throw new IllegalArgumentException("Illegal argument: bins per second value does not exceed 0.0");
        }
    }
    
    private double calculateDisplayedSearchDurationCap(Properties parameter, double maximumMouseSwimmingTime)
    {
        double parsedValue = Double.parseDouble(parameter.getProperty("preferredDisplayedSearchDurationCap", "0"));
        double preferredDisplayedSearchDurationCap = parsedValue == 0.0 ? Double.MAX_VALUE : parsedValue;
        return Math.min(maximumMouseSwimmingTime - 1, preferredDisplayedSearchDurationCap);
    }
    
    @Override
    public boolean isPublishable()
    {
        return isPublishable;
    }
    
    @Override
    public double getBinsPerSecond()
    {
        return binsPerSecond;
    }
    
    @Override
    public double getMaximumDisplayedSearchTimeDuration()
    {
        return displayedSearchDurationCap;
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public ImageFileFormat getImageFileFormat()
    {
        return imageFileFormat;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return mouseTrainingLevel;
    }
}
