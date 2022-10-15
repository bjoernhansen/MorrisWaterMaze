package MorrisWaterMaze;

import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Controller extends JPanel implements Runnable, ActionListener, ChangeListener
{
	public static final int
		PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;

	private static final long
		serialVersionUID = 4301592417000711331L;
	
	static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	public static boolean isStartingWithGui;
	static int maxNrOfPicInSeries;
	static int currentNrOfPicInSeries = 0;
	static int numberOfPics = 0;
	static double picTimeFrameUpperBound,
			      picTimeFrameLowerBound;

	static boolean loop = false;
	double picturesPerSecond = 75;
	public static final double ZOOM_FACTOR = 4;


	private static final double IMAGE_SIZE = 2 * Pool.CENTER_TO_BORDER_DISTANCE;
	private static final Dimension dim = new Dimension((int)(ZOOM_FACTOR * IMAGE_SIZE), (int)(ZOOM_FACTOR * IMAGE_SIZE));
		
	static int totalNumberOfSimulations,
			   remainingNumberOfSimulations;
	static double sumOfSearchTime = 0;
	private long lastPainted = System.currentTimeMillis();
	
	
	
	static final ArrayList<Double> searchTime = new ArrayList<>();
	

	static final AffineTransform AFFINE_TRANSFORMATION = new AffineTransform(ZOOM_FACTOR, 0, 0, ZOOM_FACTOR, 0, 0);
	
	private Thread animator;
	static final Image offImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    private static final Graphics2D offGraphics = getGraphics(offImage);
	private static JButton startAndPauseButton;
	private final JButton restartButton;
	private final JSpinner mouseTrainingLevelSpinner;
	private final JSpinner numberOfSimulationsSpinner;
	


	static String fileName;
	static File pictureDirectory;
	
	static Color dark_grey = new Color(75, 75, 75);	
	public static Color light_grey = new Color(150, 150, 150);
	
	private ParameterAccessor
		parameterAccessor;

	static Simulation
		simulation;
	
	
	Controller(Simulation simulation)
	{
		Controller.simulation = simulation;

		this.setLayout(null);		
		startAndPauseButton = new JButton("Starten");
		JLabel mouse_level_label = new JLabel("training level");
		JLabel number_of_sim_label = new JLabel("number of simulations");
		this.restartButton = new JButton("Neustart");
		this.mouseTrainingLevelSpinner = new JSpinner(new SpinnerNumberModel(simulation.getMouseTrainingLevel(), 0.0, 1.0, 0.01));
		this.mouseTrainingLevelSpinner.addChangeListener(this);
		this.numberOfSimulationsSpinner = new JSpinner(new SpinnerNumberModel(totalNumberOfSimulations, 1.0, Double.MAX_VALUE, 1.00));
		this.numberOfSimulationsSpinner.addChangeListener(this);
		JPanel mainPanel = new JPanel(new GridLayout(3, 2));
		mainPanel.setBounds(675, 25, 300, 120);
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		startAndPauseButton.addActionListener(this);
		this.restartButton.addActionListener(this);
		this.add(mainPanel);
		mainPanel.add(startAndPauseButton);
		mainPanel.add(this.restartButton);
		mainPanel.add(mouse_level_label);
		mainPanel.add(this.mouseTrainingLevelSpinner);
		mainPanel.add(number_of_sim_label);
		mainPanel.add(this.numberOfSimulationsSpinner);
		
		this.animator = new Thread(this);		
		this.animator.start();
		
		reset();
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
	
	public void run()
	{
		while (Thread.currentThread() == this.animator)
		{			
			try{
				Thread.sleep(PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS);}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
			if(loop)
			{
				simulation.nextStep();
			}
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		if(this.picturesPerSecond != 0 && System.currentTimeMillis() - this.lastPainted > 1000/this.picturesPerSecond)
		{		
			Graphics2D g2 = (Graphics2D) g.create();		
			drawOffImage();				
			super.paintComponent(g);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
	    	g2.drawImage(offImage, 25, 25, null);	
			g2.dispose();
			this.lastPainted = System.currentTimeMillis();
		}	
	}

	static void drawOffImage()
	{
		if(isStartingWithGui || currentNrOfPicInSeries == 1)
		{
			// weißer Hintergrund
			offGraphics.setColor(Color.white);
			offGraphics.fillRect(0, 0, (int) ZOOM_FACTOR *dim.height, (int) ZOOM_FACTOR *dim.width);

			// der Pool
			simulation.paintPool(offGraphics);



			// die Plattform
			offGraphics.setColor(dark_grey);
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
			startAndPauseButton.setText("Starten");
		}
		simulation.determineMouseStartingPosition();
	}
		
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		if (o == startAndPauseButton)
		{
			if(loop)
			{
				startAndPauseButton.setText("Starten");
			}
			else
			{
				startAndPauseButton.setText("Anhalten");
			}
			loop = !loop;
			this.mouseTrainingLevelSpinner.setEnabled(false);
			this.numberOfSimulationsSpinner.setEnabled(false);
		} 
		else if(o == this.restartButton)
		{
			reset();
			loop = false;
			remainingNumberOfSimulations = totalNumberOfSimulations;
			searchTime.clear();
			sumOfSearchTime = 0;
			this.mouseTrainingLevelSpinner.setEnabled(true);
			this.numberOfSimulationsSpinner.setEnabled(true);
		}
	}
		
	public void stateChanged(ChangeEvent e)
    {
        Object o = e.getSource();           
        if(o==this.mouseTrainingLevelSpinner)
        {
            if( 0 > Double.parseDouble(this.mouseTrainingLevelSpinner.getValue().toString()))
            {
            	this.mouseTrainingLevelSpinner.setValue(0.0);
            }
            else if( 1 < Double.parseDouble(this.mouseTrainingLevelSpinner.getValue().toString()))
            {
            	this.mouseTrainingLevelSpinner.setValue(1.0);
            }
			double mouseTrainingLevel = Double.parseDouble(this.mouseTrainingLevelSpinner.getValue().toString());
            simulation.setMouseTrainingLevel(mouseTrainingLevel);
        }
        if(o==this.numberOfSimulationsSpinner)
        {
            if( 0 > Double.parseDouble(this.mouseTrainingLevelSpinner.getValue().toString()))
            {
            	this.numberOfSimulationsSpinner.setValue(0.0);
            }
            else if( 1 < Double.parseDouble((this.mouseTrainingLevelSpinner).getValue().toString()))
            {
            	this.numberOfSimulationsSpinner.setValue(1.0);
            }
            remainingNumberOfSimulations = totalNumberOfSimulations = (int)Double.valueOf((this.numberOfSimulationsSpinner).getValue().toString()).doubleValue();
        }
    }



	public void addParameterAccessor(ParameterAccessor parameterAccessor)
	{
		this.parameterAccessor = parameterAccessor;
	}
}