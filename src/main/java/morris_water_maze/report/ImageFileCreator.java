package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.simulation.Simulation;
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
    
    
    public ImageFileCreator(ImagePainter imagePainter, ImageFileParameter imageFileParameter, FileNameProvider fileNameProvider)
    {
        this.imagePainter = imagePainter;
        this.fileNameProvider = fileNameProvider;
        missingPicturesCount = imageFileParameter.getNumberOfPics();
        lowerBoundOfPictureTimeFrame = imageFileParameter.getLowerBoundOfPictureTimeFrame();
        upperBoundOfPictureTimeFrame = imageFileParameter.getUpperBoundOfPictureTimeFrame();
        maxNrOfPicInSeries = imageFileParameter.getMaximumTrajectoriesPerPicture();
        imageFileFormat = imageFileParameter.getImageFileFormat();
    }
    
    @Override
    public void beNotifiedAboutCompletionOfCurrentSimulationRun()
    {
        if (isAnotherPictureToBePainted() && isSearchTimesWithinSpecifiedTimeFrame())
        {
            saveImage();
        }
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
                String fileNameTemp = fileNameProvider.getSubDirectory() + "/" + System.currentTimeMillis() + "." + imageFileFormat.getName();
                System.out.println("\nSchreibe Datei: " + fileNameTemp);
    
                File file = new File(fileNameTemp);
                
                if(imageFileFormat == ImageFileFormat.SVG)
                {
                    String svgString = imagePainter.getSvgString();
                    SVGUtils.writeToSVG(file, svgString, false);
                }
                else if(imageFileFormat == ImageFileFormat.PNG)
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
