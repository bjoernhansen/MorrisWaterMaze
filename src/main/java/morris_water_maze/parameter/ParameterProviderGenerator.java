package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterProvider;
import morris_water_maze.report.ImageFileParameterProvider;
import morris_water_maze.report.histogram.HistogramParameterProvider;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;


public class ParameterProviderGenerator
{
    private static final String
        PARAMETER_PROPERTIES_FILE_NAME = "/parameter.properties";
    
    
    private final HistogramParameterProvider
        histogramParameterProvider;
    
    private final MouseParameterProvider
        mouseParameterProvider;
    
    private final ImageFileParameterProvider
        imageFileParameterProvider;
    
    private final SimulationParameterProvider
        simulationParameterProvider;
    
    
    public ParameterProviderGenerator()
    {
        Properties parameter = getParameter();
        
        mouseParameterProvider = new MouseParameterProviderImplementation(parameter);
        simulationParameterProvider = new SimulationParameterProviderImplementation(parameter, mouseParameterProvider);
        imageFileParameterProvider = new ImageFileParameterProviderImplementation(parameter);
        
        histogramParameterProvider = new HistogramParameterProviderImplementation(parameter, this);
    }
    
    private Properties getParameter()
    {
        Properties parameter = new Properties();
        try
        {
            URL url = getClass().getResource(PARAMETER_PROPERTIES_FILE_NAME);
            parameter.load(Objects.requireNonNull(url)
                                  .openStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return parameter;
    }
    
    public HistogramParameterProvider getHistogramParameterAccessor()
    {
        return histogramParameterProvider;
    }
    
    public MouseParameterProvider getMouseParameterAccessor()
    {
        return mouseParameterProvider;
    }
    
    public ImageFileParameterProvider getImageFileParameterAccessor()
    {
        return imageFileParameterProvider;
    }
    
    public SimulationParameterProvider getSimulationParameterProvider()
    {
        return simulationParameterProvider;
    }
}
