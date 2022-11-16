package MorrisWaterMaze.parameter;

import MorrisWaterMaze.model.StartingSide;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


final class ParameterAccessorFromPropertiesFile extends AbstractParameterAccessor
{
    private static final String
        PARAMETER_PROPERTIES_FILE_NAME = "parameter.properties";
 
    private final int
        numberOfSimulations;
    
    private final int
        maximumMouseSwimmingTime;
    
    private final double
        mouseTrainingLevel;
    
    private final double
        stepLengthBias;
    
    private final StartingSide
        startingSide;
    
    private final double
        mouseSpeed;
    
    private final boolean
        isStartingWithGui;
    
    private final int
        numberOfPics;
    
    private final double
        lowerBoundOfPictureTimeFrame;
    
    private final double
        upperBoundOfPictureTimeFrame;
    
    private final int
        maximumTrajectoriesPerPicture;
    
    private final String
        simulationId;
    
    
    public ParameterAccessorFromPropertiesFile(List<String> inputParameters)
    {
        Properties parameter = getParameter();
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        maximumMouseSwimmingTime = Integer.parseInt(parameter.getProperty("maximumMouseSwimmingTime", "0"));
        mouseTrainingLevel = Double.parseDouble(parameter.getProperty("mouseTrainingLevel", "0.5"));
        stepLengthBias = Double.parseDouble(parameter.getProperty("stepLengthBias", "5"));
        startingSide = Boolean.parseBoolean(parameter.getProperty("isMouseStartPositionLeft", "true"))
                            ? StartingSide.LEFT
                            : StartingSide.RIGHT;
        mouseSpeed = Double.parseDouble(parameter.getProperty("mouseSpeed", "5"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        numberOfPics = Integer.parseInt(parameter.getProperty("numberOfPics", "0"));
        lowerBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("lowerBoundOfPictureTimeFrame", "10.74"));
        upperBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("upperBoundOfPictureTimeFrame", "25.76"));
        maximumTrajectoriesPerPicture = Integer.parseInt(parameter.getProperty("maximumTrajectoriesPerPicture", "25"));
        simulationId = generateSimulationId();
    }
    
    private Properties getParameter()
    {
        Properties parameter = new Properties();
        try
        {
            URL url = ParameterAccessorFromPropertiesFile.class.getResource(PARAMETER_PROPERTIES_FILE_NAME);
            parameter.load(Objects.requireNonNull(url)
                                  .openStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return parameter;
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public int getMaximumMouseSwimmingDuration()
    {
        return maximumMouseSwimmingTime;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return mouseTrainingLevel;
    }
    
    @Override
    public double getStepLengthBias()
    {
        return stepLengthBias;
    }
    
    @Override
    public StartingSide getStartingSide()
    {
        return startingSide;
    }
    
    @Override
    public double mouseSpeed()
    {
        return mouseSpeed;
    }
    
    @Override
    public boolean isStartingWithGui()
    {
        return isStartingWithGui;
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
    public String getSimulationId()
    {
        return simulationId;
    }
}
