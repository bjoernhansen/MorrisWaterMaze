package morris_water_maze.parameter;

import morris_water_maze.model.mouse.MouseParameterProvider;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.StringJoiner;


public final class SimulationParameterProviderImplementation implements SimulationParameterProvider
{
    private final int
        numberOfSimulations;
        
    private final boolean
        isStartingWithGui;
    
    private final String
        simulationId;
    
    private final double
        mouseTrainingLevel;
        
    
    public SimulationParameterProviderImplementation(Properties parameter, MouseParameterProvider mouseParameterProviderImplementation)
    {
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        simulationId = generateSimulationId();
        mouseTrainingLevel = mouseParameterProviderImplementation.getMouseTrainingLevel();
        
        validate();
    }
    
    private void validate()
    {
        if(numberOfSimulations < 1)
        {
            throw new IllegalArgumentException("Illegal Argument: Number of simulations < 1");
        }
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
              .add(String.valueOf(mouseTrainingLevel));
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
    
   
}
