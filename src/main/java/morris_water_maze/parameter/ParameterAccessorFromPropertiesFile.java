package morris_water_maze.parameter;

import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.StartingSide;
import morris_water_maze.model.mouse.MouseParameterAccessor;
import morris_water_maze.report.ImageFileFormat;
import morris_water_maze.report.ImageFileParameterAccessor;
import morris_water_maze.report.histogram.HistogramParameterAccessor;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.StringJoiner;


public final class ParameterAccessorFromPropertiesFile implements ParameterAccessor
// TODO Klasse aufspalten
{
    private static final String
        PARAMETER_PROPERTIES_FILE_NAME = "/parameter.properties";
 
    private final int
        numberOfSimulations;
        
    private final boolean
        isStartingWithGui;
    
    private final String
        simulationId;

    private final HistogramParameterAccessor
        histogramParameterAccessor;
    
    private final MouseParameterAccessor
        mouseParameterAccessor;
    
    private final ImageFileParameterAccessor
        imageFileParameterAccessor;
        
    
    
    public ParameterAccessorFromPropertiesFile()
    {
        Properties parameter = getParameter();
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        
        mouseParameterAccessor = new MouseParameterAccessorImplementation(parameter);
        histogramParameterAccessor = new HistogramParameterAccessorImplementation(this, parameter);
        imageFileParameterAccessor = new ImageFileParameterAccessorImplementation(parameter);
        
        simulationId = generateSimulationId();
        
        validate();
    }
    
    private void validate()
    {
        // TODO implementieren: zulässigleit aller eingabewerte Prüfen
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
        joiner.add(String.valueOf(getNumberOfSimulations()))
              .add(String.valueOf(mouseParameterAccessor.getMouseTrainingLevel()));
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
    public HistogramParameterAccessor getHistogramParameterAccessor()
    {
        return histogramParameterAccessor;
    }
    
    @Override
    public MouseParameterAccessor getMouseParameterAccessor()
    {
        return mouseParameterAccessor;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return mouseParameterAccessor.getMouseTrainingLevel();
    }
    
    @Override
    public ImageFileParameterAccessor getImageFileParameterAccessor()
    {
        return imageFileParameterAccessor;
    }
}
