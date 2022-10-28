package MorrisWaterMaze;

import MorrisWaterMaze.graphics.painter.PaintManager;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Controller implements Runnable, LoopController
{
	private static final int
		PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;
	
	public static final double
		ZOOM_FACTOR = 4;
	
	static final int
		IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
		
	static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	private static Controller
		instance;
	
	private final boolean
		isStartingWithGui;
	
	private boolean
		loop = false;
	
	
	private int maxNrOfPicInSeries;
	private int currentNrOfPicInSeries = 0;
	int numberOfPics = 0;
	double picTimeFrameUpperBound;
	
	double picTimeFrameLowerBound;


	private Thread
		animator;
	
	final Image
		offImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
  
	private final Graphics2D
		offGraphics = getGraphics(offImage);

	private final String
		fileName;

	private final Simulation
		simulation;
	
	private JFrame
		simulationFrame;
	
	private SimulationPanel
		simulationPanel;
		
	private final PaintManager
		paintManager = new PaintManager();
	

	
	Controller(Simulation simulationInstance, ParameterAccessor parameterAccessor)
	{
		instance = this;
		simulation = simulationInstance;
		isStartingWithGui = parameterAccessor.isStartingWithGui();
		fileName = parameterAccessor.getFilename();
		makeDirectory();
		
		if(parameterAccessor.getNumberOfPics() > 0)
		{
			numberOfPics = parameterAccessor.getNumberOfPics();
			picTimeFrameLowerBound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
			picTimeFrameUpperBound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
			maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
		}
		simulation.determineMouseStartingPosition();
		
		
		if(isStartingWithGui)
		{
			simulationPanel = new SimulationPanel(simulation, parameterAccessor, this);
			simulationFrame = new SimulationFrame(simulationPanel);
			simulationFrame.setVisible(true);
			
			animator = new Thread(this);
			animator.start();
		}
		else
		{
			while(simulation.isAnotherSimulationToBeStarted())
			{
				simulation.nextStep(this);
			}
		}
	}
	
	private Graphics2D getGraphics(Image Image)
	{
		Graphics2D graphics2D = (Graphics2D) Image.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		return graphics2D;
	}
	
	public void makeDirectory()
	{
		String directoryName = LOG_DIRECTORY_NAME + fileName;
		File pictureDirectory = new File(directoryName);
		pictureDirectory.mkdir();
	}
	
	@Override
	public void run()
	{
		while (Thread.currentThread() == this.animator)
		{			
			try
			{
				Thread.sleep(PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
			if(getLoopState())
			{
				simulation.nextStep(this);
			}
			simulationFrame.repaint();
		}
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
		if(isStartingWithGui)
		{
			simulationPanel.setStartAndPauseButtonText("Start");
		}
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
	
	public static Controller getInstance()
	{
		return Optional.of(instance).get();
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
}