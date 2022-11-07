package MorrisWaterMaze.control;

import MorrisWaterMaze.control.observer.SimulationCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.WaterMorrisMazeSimulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


// TODO Klasse überarbeiten
public abstract class SimulationController implements SimulationStepObserver, SimulationCompletionObserver
{
    public static final String
        LOG_DIRECTORY_NAME = "logs/";
    
    private final int
        maxNrOfPicInSeries;
    
    public final double
        upperBoundOfPictureTimeFrame;
    
    public final double
        lowerBoundOfPictureTimeFrame;
    
    private final String
        fileName;
    
    private final WaterMorrisMazeSimulation
        simulation;
    
    private final ImagePainter
        imagePainter;
    
    private int
        currentNrOfPicInSeries = 0;
    
    private int
        missingPicturesCount;
    
    
    SimulationController(WaterMorrisMazeSimulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainterInstance)
    {
        simulation = simulationInstance;
        simulation.registerSimulationStepObserver(this);
        simulation.registerSimulationCompletionObserver(this);
        makeLogDirectoryIfNotExistent();
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
    
    private void makeLogDirectoryIfNotExistent()
    {
        File logDirectory = new File(LOG_DIRECTORY_NAME);
        if (!logDirectory.exists())
        {
            logDirectory.mkdir();
        }
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
        currentNrOfPicInSeries++;
        imagePainter.paint(simulation);
        
        if (isPictureSeriesComplete())
        {
            try
            {
                String fileNameTemp = LOG_DIRECTORY_NAME + fileName + "/" + System.currentTimeMillis() + ".png";
                System.out.println("\nSchreibe Datei: " + fileNameTemp);
                ImageIO.write((RenderedImage) imagePainter.getImage(), "png", new File(fileNameTemp));
                missingPicturesCount--;
                System.out.println("Missing Pictures: " + missingPicturesCount + "\n");
            } catch (Exception exception)
            {
                System.out.println(Arrays.toString(exception.getStackTrace()));
            }
            currentNrOfPicInSeries = 0;
            imagePainter.initializeImage();
        }
    }
    
    private boolean isPictureSeriesComplete()
    {
        return currentNrOfPicInSeries == maxNrOfPicInSeries;
    }
  
    protected WaterMorrisMazeSimulation getSimulation()
    {
        return simulation;
    }
    
    public abstract void start();
    
    public boolean isAnotherPictureToBePainted()
    {
        return missingPicturesCount > 0;
    }
    
    public boolean isSearchTimesWithinSpecifiedTimeFrame()
    {
        return simulation.getLastSearchTime() >= lowerBoundOfPictureTimeFrame
            && simulation.getLastSearchTime() <= upperBoundOfPictureTimeFrame;
    }
    
    @Override
    public void beNotifiedAboutEndOfSimulation()
    {
        double sumOfSearchTimes = simulation.calculateSumOfSearchTimes();
        System.out.println("\nDurchschnittliche Suchzeit: " + (sumOfSearchTimes/simulation.getTotalNumberOfSimulations()) + "\n");
        String fileNameTemp = SimulationController.LOG_DIRECTORY_NAME + fileName + "/" + fileName + ".txt";
        System.out.println("Schreibe Datei: " + fileNameTemp);
        BufferedWriter bufferedWriter;
        try
        {
            bufferedWriter = new BufferedWriter(new FileWriter(fileNameTemp));
            for (Double searchTime : simulation.getSearchTimes())
            {
                bufferedWriter.write(searchTime + System.getProperty("line.separator"));
            }
            bufferedWriter.close();
        }
        catch (IOException ioe)
        {
            System.out.println("caught error: " + ioe);
        }
    }
    
    @Override
    public void beNotifiedAboutEndOfLastSimulationStep()
    {
        System.out.println(getSimulationStepCompletionMessage());
        if (isAnotherPictureToBePainted() && isSearchTimesWithinSpecifiedTimeFrame())
        {
            saveImage();
        }
        reset();
    }
    
    private String getSimulationStepCompletionMessage()
    {
        return  "Simulation " + simulation.getNumberOfCompletedSimulations()
                + " of " + simulation.getTotalNumberOfSimulations()
                + ", simulation time: " + simulation.getLastSearchTime();
    }
}