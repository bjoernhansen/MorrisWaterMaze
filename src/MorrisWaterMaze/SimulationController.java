package MorrisWaterMaze;

import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;

public abstract class SimulationController
{
	public static final double
		ZOOM_FACTOR = 4;
	
	public static final int
		IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
		
	static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	private final boolean
		isStartingWithGui;
	

	
	private int maxNrOfPicInSeries;
	private int currentNrOfPicInSeries = 0;
	int numberOfPics = 0;
	double picTimeFrameUpperBound;
	
	double picTimeFrameLowerBound;
	
	private final String
		fileName;

	private final Simulation
		simulation;
	
	protected final ImagePainter
		imagePainter;
	
	
	SimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor)
	{
		simulation = simulationInstance;
		isStartingWithGui = parameterAccessor.isStartingWithGui();
		fileName = parameterAccessor.getFilename();
		String directoryName = LOG_DIRECTORY_NAME + fileName;
		makeDirectory(directoryName);
		imagePainter = new ImagePainterImplementation(IMAGE_SIZE);
		
		if(parameterAccessor.getNumberOfPics() > 0)
		{
			numberOfPics = parameterAccessor.getNumberOfPics();
			picTimeFrameLowerBound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
			picTimeFrameUpperBound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
			maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
		}
		simulation.determineMouseStartingPosition();
	}
	

	
	public void makeDirectory(String directoryName)
	{
		File pictureDirectory = new File(directoryName);
		pictureDirectory.mkdir();
	}
	
	
			
	void reset()
	{
		simulation.determineMouseStartingPosition();
	}
	
	public void saveImage()
	{
		if(!isStartingWithGui)
		{
			currentNrOfPicInSeries++;
		}
		if(isStartingWithGui || currentNrOfPicInSeries == maxNrOfPicInSeries)
		{
			try
			{
				String fileNameTemp = LOG_DIRECTORY_NAME + fileName + "/" + System.currentTimeMillis() + ".png";
				System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
				Image image = imagePainter.paintImageOf(simulation);
				ImageIO.write((RenderedImage) image, "png", new File(fileNameTemp));
				numberOfPics--;
			}
			catch(Exception exception)
			{
				System.out.println(Arrays.toString(exception.getStackTrace()));
			}
		}
		if(!isStartingWithGui && currentNrOfPicInSeries == maxNrOfPicInSeries)
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
}