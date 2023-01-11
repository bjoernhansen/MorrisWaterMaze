package morris_water_maze.parameter;

import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.histogram.HistogramParameterProvider;

import java.util.Properties;


public class HistogramParameterProviderImplementation implements HistogramParameterProvider
{
    private final ParameterProvider
        parameterProvider;
    
    private final double
        displayedSearchDurationCap;
    
    private final double
        binsPerSecond;
    
    private final boolean
        isPublishable;

    
    public HistogramParameterProviderImplementation(ParameterProvider parameterProvider, Properties parameter)
    {
        this.parameterProvider = parameterProvider;
        double maximumMouseSwimmingDuration = getMaximumMouseSwimmingDurationFrom(parameterProvider);
        displayedSearchDurationCap = calculateDisplayedSearchDurationCap(parameter, maximumMouseSwimmingDuration);
        binsPerSecond = Double.parseDouble(parameter.getProperty("binsPerSecond", "5.0"));
        isPublishable = Boolean.parseBoolean(parameter.getProperty("isPublishable", "true"));
    }
    
    private double getMaximumMouseSwimmingDurationFrom(ParameterProvider parameterProvider)
    {
        return parameterProvider.getMouseParameterAccessor()
                                .getMaximumMouseSwimmingDuration();
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
        return parameterProvider.getNumberOfSimulations();
    }
    
    @Override
    public ImageFileFormat getImageFileFormat()
    {
        return parameterProvider.getImageFileParameterAccessor()
                                .getImageFileFormat();
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return parameterProvider.getMouseParameterAccessor()
                                .getMouseTrainingLevel();
    }
}
