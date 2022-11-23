package morris_water_maze.control;

import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.graphics.painter.ImagePainter;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;


public final class ImageFileCreator implements SimulationRunCompletionObserver
{
    private final int
        maxNrOfPicInSeries;
    
    private final ImagePainter
        imagePainter;
    
    public final double
        upperBoundOfPictureTimeFrame;
    
    public final double
        lowerBoundOfPictureTimeFrame;
    
    private int
        currentNrOfPicInSeries = 0;
    
    private int
        missingPicturesCount;
    
    private Simulation
        simulation;
    
    private final FileNameProvider fileNameProvider;
    
    
    public ImageFileCreator(ImagePainter imagePainterInstance, ParameterAccessor parameterAccessor, FileNameProvider fileNameProviderInstance)
    {
        imagePainter = imagePainterInstance;
        fileNameProvider = fileNameProviderInstance;
        missingPicturesCount = parameterAccessor.getNumberOfPics();
        lowerBoundOfPictureTimeFrame = parameterAccessor.getLowerBoundOfPictureTimeFrame();
        upperBoundOfPictureTimeFrame = parameterAccessor.getUpperBoundOfPictureTimeFrame();
        maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
    }
    
    @Override
    public void beNotifiedAboutCompletionOfCurrentSimulationRun()
    {
        System.out.println(getSimulationRunCompletionMessage());
        if (isAnotherPictureToBePainted() && isSearchTimesWithinSpecifiedTimeFrame())
        {
            saveImage();
        }
        simulation.reset();
    }
    
    private String getSimulationRunCompletionMessage()
    {
        return  "Simulation " + simulation.getNumberOfCompletedSimulationRuns()
            + " of " + simulation.getTotalNumberOfSimulationRuns()
            + ", simulation time: " + getLastSearchTime();
    }
    
    private double getLastSearchTime()
    {
        return simulation.getSearchTimeProvider()
                         .getLastSearchTime();
    }

    private boolean isAnotherPictureToBePainted()
    {
        return missingPicturesCount > 0;
    }
    
    private boolean isSearchTimesWithinSpecifiedTimeFrame()
    {
        return getLastSearchTime() >= lowerBoundOfPictureTimeFrame
            && getLastSearchTime() <= upperBoundOfPictureTimeFrame;
    }
    
    private void saveImage()
    {
        currentNrOfPicInSeries++;
        imagePainter.paint(simulation);
        
        if (isPictureSeriesComplete())
        {
            try
            {
                String fileNameTemp = fileNameProvider.getSubDirectory() + "/" + System.currentTimeMillis() + ".png";
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
    
    @Override
    public void setSimulation(Simulation simulationInstance)
    {
        simulation = simulationInstance;
    }
}
