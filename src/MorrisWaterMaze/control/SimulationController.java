package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;


// TODO Klasse überarbeiten
public abstract class SimulationController
{
    public static final double
        ZOOM_FACTOR = 4;
    
    public static final int // TODO ggf. umziehen nach Main
        IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
    
    public static final String
        LOG_DIRECTORY_NAME = "logs/";
    
    private final boolean
        isStartingWithGui;
    
    private final int
        maxNrOfPicInSeries;
        
    public final double
        upperBoundOfPictureTimeFrame;
    
    public final double
        lowerBoundOfPictureTimeFrame;
    
    private final String
        fileName;
    
    private final Simulation
        simulation;
    
    private final ImagePainter
        imagePainter;
    
    private int
        currentNrOfPicInSeries = 0;
    
    private int
        missingPicturesCount;
    
    
    SimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainterInstance)
    {
        simulation = simulationInstance;
        isStartingWithGui = parameterAccessor.isStartingWithGui();
        fileName = parameterAccessor.getFilename();
        String directoryName = LOG_DIRECTORY_NAME + fileName;
        makeDirectory(directoryName);
        imagePainter = imagePainterInstance;
        missingPicturesCount = parameterAccessor.getNumberOfPics();
        lowerBoundOfPictureTimeFrame = parameterAccessor.getLowerBoundOfPictureTimeFrame();
        upperBoundOfPictureTimeFrame = parameterAccessor.getUpperBoundOfPictureTimeFrame();
        maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
        
        reset();
    }
    
    
    public void makeDirectory(String directoryName)
    {
        File pictureDirectory = new File(directoryName);
        pictureDirectory.mkdir();
    }
    
    public void reset()
    {
        simulation.reset();
    }
    
    public void saveImage()
    {
        // TODO vor dem Refactoring sicher gehen, dass es aktuell richtig funktioniert: Werden mehrere Trajektorien im Bild gespeichert?
        if (!isStartingWithGui)
        {
            currentNrOfPicInSeries++;
        }
        if (isStartingWithGui || currentNrOfPicInSeries == maxNrOfPicInSeries)
        {
            try
            {
                String fileNameTemp = LOG_DIRECTORY_NAME + fileName + "/" + System.currentTimeMillis() + ".png";
                System.out.println();
                System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
                Image image = imagePainter.paintImageOf(simulation);
                ImageIO.write((RenderedImage) image, "png", new File(fileNameTemp));
                missingPicturesCount--;
                System.out.println("Missing Pictures: " + missingPicturesCount);
                System.out.println();
            }
            catch (Exception exception)
            {
                System.out.println(Arrays.toString(exception.getStackTrace()));
            }
        }
        if (!isStartingWithGui && currentNrOfPicInSeries == maxNrOfPicInSeries)
        {
            currentNrOfPicInSeries = 0;
        }
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    
    protected Simulation getSimulation()
    {
        return simulation;
    }
    
    public abstract void start();
    
    public boolean isAnotherPictureToBePainted()
    {
        return missingPicturesCount > 0;
    }
    
    public boolean isSearchTimesWithinSpecifiedTimeFrame(double lastSearchTime)
    {
        return lastSearchTime >= lowerBoundOfPictureTimeFrame && lastSearchTime <= upperBoundOfPictureTimeFrame;
    }
}