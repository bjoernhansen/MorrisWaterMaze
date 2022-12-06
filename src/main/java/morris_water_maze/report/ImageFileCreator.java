package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;
import org.jfree.graphics2d.svg.SVGUtils;

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
    
    private final double
        upperBoundOfPictureTimeFrame;
    
    private final double
        lowerBoundOfPictureTimeFrame;
    
    private int
        currentNrOfPicInSeries = 0;
    
    private int
        missingPicturesCount;
    
    private Simulation
        simulation;
    
    private final FileNameProvider
        fileNameProvider;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    public ImageFileCreator(ImagePainter imagePainter, ParameterAccessor parameterAccessor, FileNameProvider fileNameProvider)
    {
        this.imagePainter = imagePainter;
        this.fileNameProvider = fileNameProvider;
        missingPicturesCount = parameterAccessor.getNumberOfPics();
        lowerBoundOfPictureTimeFrame = parameterAccessor.getLowerBoundOfPictureTimeFrame();
        upperBoundOfPictureTimeFrame = parameterAccessor.getUpperBoundOfPictureTimeFrame();
        maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
        imageFileFormat = parameterAccessor.getImagePainterTypeForPictureExport()
                                           .getImageFileFormat();
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
                String fileNameTemp = fileNameProvider.getSubDirectory() + "/" + System.currentTimeMillis() + "." + imageFileFormat.getName();
                System.out.println("\nSchreibe Datei: " + fileNameTemp);
    
                File file = new File(fileNameTemp);
                
                if(imageFileFormat == ImageFileFormat.SVG)
                {
                    String svgString = imagePainter.getSvgString();
                    SVGUtils.writeToSVG(file, svgString, false);
                }
                else
                {
                    ImageIO.write((RenderedImage) imagePainter.getImage(), ImageFileFormat.PNG.getName(), file);
                }
                
                missingPicturesCount--;
                System.out.println("Missing Pictures: " + missingPicturesCount + "\n");
            }
            catch (Exception exception)
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
