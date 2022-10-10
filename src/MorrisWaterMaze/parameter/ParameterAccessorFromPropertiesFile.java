package MorrisWaterMaze.parameter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class ParameterAccessorFromPropertiesFile extends AbstractParameterAccessor
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
    
    private final boolean
        isMouseStartPositionLeft;
    
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
        fileName;
    
    
    public ParameterAccessorFromPropertiesFile()
    {
        Properties parameter = getParameter();
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        maximumMouseSwimmingTime = Integer.parseInt(parameter.getProperty("maximumMouseSwimmingTime", "0"));
        mouseTrainingLevel = Double.parseDouble(parameter.getProperty("mouseTrainingLevel", "0.5"));
        stepLengthBias = Double.parseDouble(parameter.getProperty("stepLengthBias", "5"));
        isMouseStartPositionLeft = Boolean.parseBoolean(parameter.getProperty("isMouseStartPositionLeft", "true"));
        mouseSpeed = Double.parseDouble(parameter.getProperty("mouseSpeed", "5"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        numberOfPics = Integer.parseInt(parameter.getProperty("numberOfPics", "0"));
        lowerBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("lowerBoundOfPictureTimeFrame", "10.74"));
        upperBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("upperBoundOfPictureTimeFrame", "25.76"));
        maximumTrajectoriesPerPicture = Integer.parseInt(parameter.getProperty("maximumTrajectoriesPerPicture", "25"));
        fileName = generateFilename();
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
    public int getMaximumMouseSwimmingTime()
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
    public boolean isMouseStartPositionLeft()
    {
        return isMouseStartPositionLeft;
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
    public String getFilename()
    {
        return fileName;
    }
}
