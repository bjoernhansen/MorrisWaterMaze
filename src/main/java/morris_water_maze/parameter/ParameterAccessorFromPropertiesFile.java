package morris_water_maze.parameter;

import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.StartingSide;
import morris_water_maze.report.ImageFileFormat;

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
    
    private final double
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
    
    private final ImagePainterType
        imagePainterTypeForPictureExport;
    
    private final String
        simulationId;
    
    // Histogram-Parameters
    private final double
        displayedSearchDurationCap;
    
    private final double
        binsPerSecond;
    
    private final boolean
        isPublishable;
    
    
    
    public ParameterAccessorFromPropertiesFile()
    {
        Properties parameter = getParameter();
        numberOfSimulations = Integer.parseInt(parameter.getProperty("numberOfSimulations", "10"));
        maximumMouseSwimmingTime = determineMaximumSwimmingTime(parameter.getProperty("maximumMouseSwimmingTime", "0.0"));
        mouseTrainingLevel = Double.parseDouble(parameter.getProperty("mouseTrainingLevel", "0.5"));
        stepLengthBias = Double.parseDouble(parameter.getProperty("stepLengthBias", "5.0"));
        startingSide = Boolean.parseBoolean(parameter.getProperty("isMouseStartPositionLeft", "true"))
                            ? StartingSide.LEFT
                            : StartingSide.RIGHT;
        mouseSpeed = Double.parseDouble(parameter.getProperty("mouseSpeed", "5"));
        isStartingWithGui = Boolean.parseBoolean(parameter.getProperty("isStartingWithGui", "true"));
        numberOfPics = Integer.parseInt(parameter.getProperty("numberOfPics", "0"));
        lowerBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("lowerBoundOfPictureTimeFrame", "10.74"));
        upperBoundOfPictureTimeFrame = Double.parseDouble(parameter.getProperty("upperBoundOfPictureTimeFrame", "25.76"));
        maximumTrajectoriesPerPicture = Integer.parseInt(parameter.getProperty("maximumTrajectoriesPerPicture", "25"));
        imagePainterTypeForPictureExport = Boolean.parseBoolean(parameter.getProperty("isUsingSvgAsImageFileFormat", "false"))
                                            ? ImagePainterType.SVG
                                            : ImagePainterType.DEFAULT;
        
        simulationId = generateSimulationId();
        
        // Histogram
        binsPerSecond = Double.parseDouble(parameter.getProperty("binsPerSecond", "5.0"));
        isPublishable = Boolean.parseBoolean(parameter.getProperty("isPublishable", "true"));
        displayedSearchDurationCap = calculateDisplayedSearchDurationCap(parameter, maximumMouseSwimmingTime);
        
        validate();
    }
    
    private void validate()
    {
        // TODO implementieren: zulässigleit aller eingabewerte Prüfen
    }
    
    private double determineMaximumSwimmingTime(String swimmingTimeAsString)
    {
        double swimmingTime = Double.parseDouble(swimmingTimeAsString);
        return swimmingTime > 0.0
                ? swimmingTime
                : Double.MAX_VALUE;
    }
    
    private double calculateDisplayedSearchDurationCap(Properties parameter, double maximumMouseSwimmingTime)
    {
        double parsedValue = Double.parseDouble(parameter.getProperty("preferredDisplayedSearchDurationCap", "0"));
        double preferredDisplayedSearchDurationCap = parsedValue == 0.0 ? Double.MAX_VALUE : parsedValue;
        return Math.min(maximumMouseSwimmingTime - 1, preferredDisplayedSearchDurationCap);
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
              .add(String.valueOf(getMaximumMouseSwimmingDuration()))
              .add(String.valueOf(getMouseTrainingLevel()))
              .add(String.valueOf(getStepLengthBias()))
              .add(String.valueOf(getStartingSide()))
              .add(String.valueOf(mouseSpeed()))
              .add(String.valueOf(isStartingWithGui()))
              .add(String.valueOf(getNumberOfPics()))
              .add(String.valueOf(getLowerBoundOfPictureTimeFrame()))
              .add(String.valueOf(getUpperBoundOfPictureTimeFrame()))
              .add(String.valueOf(getMaximumTrajectoriesPerPicture()))
              .add(String.valueOf(getImagePainterTypeForPictureExport()));
        return joiner.toString();
    }
    
    @Override
    public ImageFileFormat getImageFileFormat()
    {
        return getImagePainterTypeForPictureExport().getImageFileFormat();
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public double getMaximumMouseSwimmingDuration()
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
    public ImagePainterType getImagePainterTypeForPictureExport()
    {
        return imagePainterTypeForPictureExport;
    }
    
    @Override
    public String getSimulationId()
    {
        return simulationId;
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
}
