package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameter;
import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.histogram.HistogramParameter;

import java.util.Properties;


public final class HistogramParameterProvider implements HistogramParameter
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
    
    
    public HistogramParameterProvider(Properties parameter, ParameterProvider parameterProvider)
    {
        binsPerSecond = Double.parseDouble(parameter.getProperty("binsPerSecond", "5.0"));
        isPublishable = Boolean.parseBoolean(parameter.getProperty("isPublishable", "true"));
    
        numberOfSimulations = parameterProvider.getSimulationParameterProvider()
                                               .getNumberOfSimulations();
    
        MouseParameter mouseParameter = parameterProvider.getMouseParameterProvider();
        mouseTrainingLevel = mouseParameter.getMouseTrainingLevel();
        displayedSearchDurationCap = calculateDisplayedSearchDurationCap(parameter, mouseParameter.getMaximumMouseSwimmingDuration());
    
        imageFileFormat = parameterProvider.getImageFileParameterProvider()
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
