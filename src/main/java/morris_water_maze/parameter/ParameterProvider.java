package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameter;
import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileParameter;
import morris_water_maze.report.histogram.HistogramParameter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;


public final class ParameterProvider
{
    private static final String
        PARAMETER_PROPERTIES_FILE_NAME = "/parameter.properties";
    
    
    private final HistogramParameter
        histogramParameter;
    
    private final MouseParameter
        mouseParameter;
    
    private final ImageFileParameter
        imageFileParameter;
    
    private final SimulationParameter
        simulationParameter;
    
    private final FileNameProvider
        fileNameProvider;
    
    
    public ParameterProvider()
    {
        Properties parameter = getParameter();
        
        mouseParameter = new MouseParameterProvider(parameter);
        simulationParameter = new SimulationParameterProvider(parameter, mouseParameter);
        imageFileParameter = new ImageFileParameterProvider(parameter);
        
        histogramParameter = new HistogramParameterProvider(parameter, this);
        
        String simulationId = simulationParameter.getSimulationId();
        fileNameProvider = new FileNameProvider(simulationId);
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
    
    public HistogramParameter getHistogramParameterProvider()
    {
        return histogramParameter;
    }
    
    public MouseParameter getMouseParameterProvider()
    {
        return mouseParameter;
    }
    
    public ImageFileParameter getImageFileParameterProvider()
    {
        return imageFileParameter;
    }
    
    public SimulationParameter getSimulationParameterProvider()
    {
        return simulationParameter;
    }
    
    public FileNameProvider getFileNameProvider()
    {
        return fileNameProvider;
    }
}
