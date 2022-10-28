package MorrisWaterMaze;

import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;

public abstract class SimulationController implements LoopController
{
	public static final double
		ZOOM_FACTOR = 4;
	
	static final int
		IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
		
	static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	private final boolean
		isStartingWithGui;
	
	private boolean
		loop = false;
	
	
	private int maxNrOfPicInSeries;
	private int currentNrOfPicInSeries = 0;
	int numberOfPics = 0;
	double picTimeFrameUpperBound;
	
	double picTimeFrameLowerBound;
	
	final Image
		offImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
  
	private final Graphics2D
		offGraphics = getGraphics(offImage);

	private final String
		fileName;

	private final Simulation
		simulation;
	
	
	SimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor)
	{
		simulation = simulationInstance;
		isStartingWithGui = parameterAccessor.isStartingWithGui();
		fileName = parameterAccessor.getFilename();
		String directoryName = LOG_DIRECTORY_NAME + fileName;
		makeDirectory(directoryName);
		
		if(parameterAccessor.getNumberOfPics() > 0)
		{
			numberOfPics = parameterAccessor.getNumberOfPics();
			picTimeFrameLowerBound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
			picTimeFrameUpperBound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
			maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
		}
		simulation.determineMouseStartingPosition();
	}
	
	private Graphics2D getGraphics(Image Image)
	{
		Graphics2D graphics2D = (Graphics2D) Image.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		return graphics2D;
	}
	
	public void makeDirectory(String directoryName)
	{
		File pictureDirectory = new File(directoryName);
		pictureDirectory.mkdir();
	}
	
	void drawOffImage()
	{
		if(isStartingWithGui || currentNrOfPicInSeries == 1)
		{
			// weiﬂer Hintergrund
			simulation.paintBackground(offGraphics);
			
			// der Pool
			simulation.paintPool(offGraphics);
			
			// die Plattform
			simulation.paintPlatform(offGraphics);
		
		}
		simulation.paintMouseTrajectory(offGraphics);
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
		drawOffImage();
		if(isStartingWithGui || currentNrOfPicInSeries == maxNrOfPicInSeries)
		{
			try
			{
				String fileNameTemp = LOG_DIRECTORY_NAME + fileName + "/" + System.currentTimeMillis() + ".png";
				System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
				ImageIO.write((RenderedImage)offImage, "png", new File(fileNameTemp));
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
	
	@Override
	public void switchLoopState()
	{
		loop = !loop;
	}
	
	@Override
	public boolean getLoopState()
	{
		return loop;
	}
	
	@Override
	public void stopLooping()
	{
		loop = false;
	}
	
	protected Simulation getSimulation()
	{
		return simulation;
	}
}