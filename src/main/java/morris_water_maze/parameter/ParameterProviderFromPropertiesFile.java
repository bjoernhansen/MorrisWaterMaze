package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterProvider;
import morris_water_maze.report.ImageFileParameterProvider;
import morris_water_maze.report.histogram.HistogramParameterProvider;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.StringJoiner;


public final class ParameterProviderFromPropertiesFile implements ParameterProvider
{
    private static final String
        PARAMETER_PROPERTIES_FILE_NAME = "/parameter.properties";
 
    private final int
        numberOfSimulations;
        
    private final boolean
        isStartingWithGui;
    
    private final String
        simulationId;

    private final HistogramParameterProvider
        histogramParameterProvider;
    
    private final MouseParameterProvider
        mouseParameterProvider;
    
    private final ImageFileParameterProvider
        imageFileParameterProvider;
    
    
    public ParameterProviderFromPropertiesFile()
    {
        Properties parameter = getParameter();
        
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        
        mouseParameterProvider = new MouseParameterProviderImplementation(parameter);
        histogramParameterProvider = new HistogramParameterProviderImplementation(this, parameter);
        imageFileParameterProvider = new ImageFileParameterProviderImplementation(parameter);
        
        simulationId = generateSimulationId();
        
        validate();
    }
    
    private void validate()
    {
        if(numberOfSimulations < 1)
        {
            throw new IllegalArgumentException("Illegal Argument: Number of simulations < 1");
        }
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
    
    private String generateSimulationId()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );
        return dateFormat.format(date) + "_parameter_" + getParameterString();
    }
    
    private String getParameterString()
    {
        StringJoiner joiner = new StringJoiner("_");
        joiner.add(String.valueOf(numberOfSimulations))
              .add(String.valueOf(mouseParameterProvider.getMouseTrainingLevel()));
        return joiner.toString();
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public boolean isStartingWithGui()
    {
        return isStartingWithGui;
    }
    
    
    @Override
    public String getSimulationId()
    {
        return simulationId;
    }
    
    @Override
    public HistogramParameterProvider getHistogramParameterAccessor()
    {
        return histogramParameterProvider;
    }
    
    @Override
    public MouseParameterProvider getMouseParameterAccessor()
    {
        return mouseParameterProvider;
    }
    
    @Override
    public ImageFileParameterProvider getImageFileParameterAccessor()
    {
        return imageFileParameterProvider;
    }
}
