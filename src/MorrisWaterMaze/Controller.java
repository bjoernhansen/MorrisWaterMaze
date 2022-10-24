package MorrisWaterMaze;

import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller implements Runnable
{
	public static final int
		PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;
	
	public static final double
		ZOOM_FACTOR = 4;
	
	private static final int
		IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
		
	static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	
	
	public static boolean isStartingWithGui;
	static int maxNrOfPicInSeries;
	static int currentNrOfPicInSeries = 0;
	static int numberOfPics = 0;
	static double picTimeFrameUpperBound,
			      picTimeFrameLowerBound;

	static boolean loop = false;


	
	static double sumOfSearchTime = 0;


	
	static final ArrayList<Double> searchTime = new ArrayList<>();
	

	static final AffineTransform AFFINE_TRANSFORMATION = new AffineTransform(ZOOM_FACTOR, 0, 0, ZOOM_FACTOR, 0, 0);
	
	private Thread animator;
	static final Image offImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
    private static final Graphics2D offGraphics = getGraphics(offImage);
	
	


	static String fileName;
	static File pictureDirectory;
	
	private static final Color DARK_GREY = new Color(75, 75, 75);


	private static Simulation
		simulation;
	
	private static JFrame
		simulationFrame;
	
	private static SimulationPanel
		simulationPanel;
	

	Controller(Simulation simulationInstance, ParameterAccessor parameterAccessor)
	{
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
			simulationPanel = new SimulationPanel(simulation, parameterAccessor);
			simulationFrame = new SimulationFrame(simulationPanel);
			simulationFrame.setVisible(true);
			
			animator = new Thread(this);
			animator.start();
		}
		else
		{
			while(simulation.isAnotherSimulationToBeStarted())
			{
				simulation.nextStep();
			}
		}
	}
	
	private static Graphics2D getGraphics(Image Image)
	{
		Graphics2D graphics2D = (Graphics2D) Image.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		return graphics2D;
	}
	
	public static void makeDirectory()
	{
		String directoryName = LOG_DIRECTORY_NAME + fileName;
		pictureDirectory = new File(directoryName);
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
			if(loop)
			{
				simulation.nextStep();
			}
			simulationFrame.repaint();
		}
	}

	static void drawOffImage()
	{
		if(isStartingWithGui || currentNrOfPicInSeries == 1)
		{
			// weiﬂer Hintergrund
			offGraphics.setColor(Color.white);
			offGraphics.fillRect(0, 0, (int) ZOOM_FACTOR * IMAGE_SIZE, (int) ZOOM_FACTOR * IMAGE_SIZE);

			// der Pool
			simulation.paintPool(offGraphics);
			
			// die Plattform
			offGraphics.setColor(DARK_GREY);
			offGraphics.fill(AFFINE_TRANSFORMATION.createTransformedShape(simulation.getPlatformBounds()));
			offGraphics.setColor(Color.black);
			offGraphics.fillOval((int)(ZOOM_FACTOR *(Pool.CENTER_TO_BORDER_DISTANCE -1)), (int)(ZOOM_FACTOR *(Pool.CENTER_TO_BORDER_DISTANCE -1)), (int)(2* ZOOM_FACTOR), (int)(2* ZOOM_FACTOR));
		}
		simulation.paintMouseTrajectory(offGraphics);
	}
			
	static void reset()
	{
		if(isStartingWithGui)
		{
			simulationPanel.setStartAndPauseButtonText("Start");
		}
		simulation.determineMouseStartingPosition();
	}
	
	
	public static void saveImage()
	{
		if(!Controller.isStartingWithGui)
		{
			Controller.currentNrOfPicInSeries++;
		}
		Controller.drawOffImage();
		if(Controller.isStartingWithGui || Controller.currentNrOfPicInSeries == Controller.maxNrOfPicInSeries)
		{
			try
			{
				String fileNameTemp = Controller.LOG_DIRECTORY_NAME + Controller.fileName + "/" + System.currentTimeMillis() + ".png";
				System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
				ImageIO.write((RenderedImage)Controller.offImage, "png", new File(fileNameTemp));
				Controller.numberOfPics--;
			}
			catch(Exception exception)
			{
				System.out.println(Arrays.toString(exception.getStackTrace()));
			}
		}
		if(!Controller.isStartingWithGui && Controller.currentNrOfPicInSeries == Controller.maxNrOfPicInSeries)
		{
			Controller.currentNrOfPicInSeries = 0;
		}
	}
}