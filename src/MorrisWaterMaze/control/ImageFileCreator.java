package MorrisWaterMaze.control;

import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.simulation.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;


public final class ImageFileCreator implements SimulationStepObserver
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
    public void beNotifiedAboutEndOfLastSimulationStep()
    {
        System.out.println(getSimulationStepCompletionMessage());
        if (isAnotherPictureToBePainted() && isSearchTimesWithinSpecifiedTimeFrame())
        {
            saveImage();
        }
        simulation.reset();
    }
    
    private String getSimulationStepCompletionMessage()
    {
        return  "Simulation " + simulation.getNumberOfCompletedSimulations()
            + " of " + simulation.getTotalNumberOfSimulations()
            + ", simulation time: " + simulation.getLastSearchTime();
    }
    
    private boolean isAnotherPictureToBePainted()
    {
        return missingPicturesCount > 0;
    }
    
    private boolean isSearchTimesWithinSpecifiedTimeFrame()
    {
        return simulation.getLastSearchTime() >= lowerBoundOfPictureTimeFrame
            && simulation.getLastSearchTime() <= upperBoundOfPictureTimeFrame;
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
