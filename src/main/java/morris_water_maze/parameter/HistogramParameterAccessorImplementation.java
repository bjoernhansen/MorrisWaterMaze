package morris_water_maze.parameter;

import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.histogram.HistogramParameterAccessor;

import java.util.Properties;


public class HistogramParameterAccessorImplementation implements HistogramParameterAccessor
{
    private final ParameterAccessor
        parameterAccessor;
    
    private final double
        displayedSearchDurationCap;
    
    private final double
        binsPerSecond;
    
    private final boolean
        isPublishable;

    
    public HistogramParameterAccessorImplementation(ParameterAccessor parameterAccessor, Properties parameter)
    {
        this.parameterAccessor = parameterAccessor;
        double maximumMouseSwimmingDuration = getMaximumMouseSwimmingDurationFrom(parameterAccessor);
        displayedSearchDurationCap = calculateDisplayedSearchDurationCap(parameter, maximumMouseSwimmingDuration);
        binsPerSecond = Double.parseDouble(parameter.getProperty("binsPerSecond", "5.0"));
        isPublishable = Boolean.parseBoolean(parameter.getProperty("isPublishable", "true"));
    }
    
    private double getMaximumMouseSwimmingDurationFrom(ParameterAccessor parameterAccessor)
    {
        return parameterAccessor.getMouseParameterAccessor()
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
        return parameterAccessor.getNumberOfSimulations();
    }
    
    @Override
    public ImageFileFormat getImageFileFormat()
    {
        return parameterAccessor.getImageFileParameterAccessor()
                                .getImageFileFormat();
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return parameterAccessor.getMouseTrainingLevel();
    }
}
