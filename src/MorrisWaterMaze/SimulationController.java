package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
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
import java.awt.Font;
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
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SimulationController extends JPanel implements Runnable, ActionListener, ChangeListener
{
	public static final int
		PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;
	
	private static final long
		serialVersionUID = 4301592417000711331L;
	
	private static final String
		LOG_DIRECTORY_NAME = "logs/";
	
	static boolean isStartingWithGui;
	static int 	max_nr_of_pic_in_series;
	private static int current_nr_of_pic_in_series = 0;
	static int numberOfPics = 0;
	static double pic_time_frame_upper_bound, 
			      picTimeFrameLowerBound;
	
	private static int counter;
	static boolean loop = false;
	double picturesPerSecond = 75;
	static final double ZOOM_FACTOR = 4;
	private static final double POOL_RADIUS = 65;
	private static final double POOL_BORDER_DISTANCE = 25;
	private static final double IMAGE_CENTER = POOL_RADIUS + POOL_BORDER_DISTANCE;
	private static final double IMAGE_SIZE = 2 * IMAGE_CENTER;
	private static final Dimension dim = new Dimension((int)(ZOOM_FACTOR * IMAGE_SIZE), (int)(ZOOM_FACTOR * IMAGE_SIZE));
		
	static int totalNumberOfSimulations,
			   remainingNumberOfSimulations;
	private static double sumOfSearchTime = 0;
	private long lastPainted = System.currentTimeMillis();
	
	
	
	private static final ArrayList<Double> searchTime = new ArrayList<>(0);
	
	private static final Rectangle2D platform = makePlatform();
	static Point2D center_of_platform = new Point2D.Double(platform.getCenterX(), platform.getCenterY());
	private static final AffineTransform AFFINE_TRANSFORMATION = new AffineTransform(ZOOM_FACTOR, 0, 0, ZOOM_FACTOR, 0, 0);
	
	private Thread animator;
	private static final Image offImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    private static final Graphics2D offGraphics = getGraphics(offImage);
	private static JButton startAndPauseButton;
	private final JButton restartButton;
	private final JSpinner mouseLevel;
	private final JSpinner numberOfSimulations;
	
	private static final Pool pool = new Pool(IMAGE_CENTER, IMAGE_CENTER, POOL_RADIUS);
	
	static Mouse mouse;
	static final DecimalFormat decimal_format = new DecimalFormat("0.000");
	static final Font myFontPLAIN36 = new Font("Dialog", Font.BOLD, 36);
		
	static String fileName;
	static File pictureDirectory;
	
	static Color dark_grey = new Color(75, 75, 75);	
	static Color light_grey = new Color(150, 150, 150);
	
	private ParameterAccessor
		parameterAccessor;
	
	
	SimulationController()
	{			
		this.setLayout(null);		
		startAndPauseButton = new JButton("Starten");
		JLabel mouse_level_label = new JLabel("training level");
		JLabel number_of_sim_label = new JLabel("number of simulations");
		this.restartButton = new JButton("Neustart");
		this.mouseLevel = new JSpinner(new SpinnerNumberModel(mouse.trainingLevel, 0.0, 1.0, 0.01));
		this.numberOfSimulations = new JSpinner(new SpinnerNumberModel(totalNumberOfSimulations, 1.0, Double.MAX_VALUE, 1.00));
		this.mouseLevel.addChangeListener(this);
		this.numberOfSimulations.addChangeListener(this);
		JPanel mainPanel = new JPanel(new GridLayout(3, 2));
		mainPanel.setBounds(675, 25, 300, 120);
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		startAndPauseButton.addActionListener(this);
		this.restartButton.addActionListener(this);
		this.add(mainPanel);
		mainPanel.add(startAndPauseButton);
		mainPanel.add(this.restartButton);
		mainPanel.add(mouse_level_label);
		mainPanel.add(this.mouseLevel);
		mainPanel.add(number_of_sim_label);
		mainPanel.add(this.numberOfSimulations);
		
		this.animator = new Thread(this);		
		this.animator.start();
		
		restart();
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
			repaint();
		}
	}	
		
	private static void calculateSim()
	{
		if(loop)
		{
			if(mouse.isSwimming)
			{
				counter++;
				double timeStep = Math.log(mouse.stepLengthBias / nonZeroRandom());
				
				mouse.move(pool, platform, timeStep);
			}
			else if (remainingNumberOfSimulations >= 1)
			{
				double lastSearchTime = mouse.timeSteps.get(mouse.timeSteps.size()-1);
				searchTime.add(lastSearchTime);
				sumOfSearchTime += lastSearchTime;
				
				System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations + 1) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);
				remainingNumberOfSimulations--;
				
				if(	numberOfPics > 0 && lastSearchTime >= picTimeFrameLowerBound && lastSearchTime <= pic_time_frame_upper_bound)
			    {					
					saveImage();					
			    }
							
				if(remainingNumberOfSimulations == 0)
				{
					System.out.println("\nDurchschnittliche Suchzeit: " + (sumOfSearchTime / totalNumberOfSimulations) + "\n");
					
					BufferedWriter bw;
					String file_name_temp = LOG_DIRECTORY_NAME + fileName + "/" + fileName + ".txt";
					System.out.println("Schreibe Datei: " + file_name_temp);				
				    try 
				    {				    	
				    	searchTime.size();
				        bw = new BufferedWriter(new FileWriter(file_name_temp));
						for (Double aDouble : searchTime)
						{
							bw.write(aDouble + System.getProperty("line.separator"));
						}
				        bw.close();
				    }					    
				    catch (IOException ioe) 
				    {
				    	System.out.println("caught error: " + ioe);
				    }				    
				}				
				restart();
			}
		}
	}
	
	private static double nonZeroRandom()
	{
		return 1.0 - Math.random();
	}
	
	static void drawOffImage()
	{		
		if(isStartingWithGui || current_nr_of_pic_in_series == 1)
		{
			// wei�er Hintergrund
			offGraphics.setColor(Color.white);
			offGraphics.fillRect(0, 0, (int) ZOOM_FACTOR *dim.height, (int) ZOOM_FACTOR *dim.width);
			
			// der Pool
			pool.paint(offGraphics, AFFINE_TRANSFORMATION);
			
			// die Plattform
			offGraphics.setColor(dark_grey);
			offGraphics.fill(AFFINE_TRANSFORMATION.createTransformedShape(platform));
			offGraphics.setColor(Color.black);
			offGraphics.fillOval((int)(ZOOM_FACTOR *(IMAGE_CENTER -1)), (int)(ZOOM_FACTOR *(IMAGE_CENTER -1)), (int)(2* ZOOM_FACTOR), (int)(2* ZOOM_FACTOR));
		}
		mouse.paint_trajectory(offGraphics);
	}	
		
	@Override
	public void paintComponent(Graphics g)
	{
		calculateSim();
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
			
	static void restart()
	{
		counter = 0;
		if(isStartingWithGui){
			startAndPauseButton.setText("Starten");}
		mouse.getRandomStartingPosition(pool);
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
			this.mouseLevel.setEnabled(false);
			this.numberOfSimulations.setEnabled(false);
		} 
		else if(o == this.restartButton)
		{
			restart();
			loop = false;
			remainingNumberOfSimulations = totalNumberOfSimulations;
			searchTime.clear();
			sumOfSearchTime = 0;
			this.mouseLevel.setEnabled(true);
			this.numberOfSimulations.setEnabled(true);
		}
	}
		
	public void stateChanged(ChangeEvent e)
    {
        Object o = e.getSource();           
        if(o==this.mouseLevel)
        {
            if( 0 > Double.parseDouble(this.mouseLevel.getValue().toString()))
            {
            	this.mouseLevel.setValue(0.0);
            }
            else if( 1 < Double.parseDouble(this.mouseLevel.getValue().toString()))
            {
            	this.mouseLevel.setValue(1.0);
            }
            mouse.trainingLevel = Double.parseDouble(this.mouseLevel.getValue().toString());
        }
        if(o==this.numberOfSimulations)
        {
            if( 0 > Double.parseDouble(this.mouseLevel.getValue().toString()))
            {
            	this.numberOfSimulations.setValue(0.0);
            }
            else if( 1 < Double.parseDouble((this.mouseLevel).getValue().toString()))
            {
            	this.numberOfSimulations.setValue(1.0);
            }
            remainingNumberOfSimulations = totalNumberOfSimulations = (int)Double.valueOf((this.numberOfSimulations).getValue().toString()).doubleValue();
        }
    }
    
	/*
	static void print_time(Mouse mouse)
	{
		String output = "Simulationszeit: " + decimal_format.format(mouse.time_steps.get(mouse.time_steps.size()-1))+"s";
		System.out.println(output);
		mouse_level_label.setText(output);
	}
	*/
		
	static Rectangle2D makePlatform()
	{
		double alpha = 0.25*Math.PI;
		double platform_site_length = 10;
		double outer_corner_radius = 0.5*(POOL_RADIUS + platform_site_length *Math.sqrt(2));
		return new Rectangle2D.Double(	IMAGE_CENTER + outer_corner_radius*Math.cos(alpha) - platform_site_length,
										IMAGE_CENTER + outer_corner_radius*Math.sin(alpha) - platform_site_length,
			platform_site_length, platform_site_length);
	}	
	
	public static void startSim()
	{					
		restart();
		while(remainingNumberOfSimulations >= 1){
			calculateSim();}
	}
	
	public static void saveImage()
	{
		if(!isStartingWithGui){current_nr_of_pic_in_series++;}
		drawOffImage();	
		if(isStartingWithGui || current_nr_of_pic_in_series == max_nr_of_pic_in_series)
		{
			try
			{
				String fileNameTemp = LOG_DIRECTORY_NAME + fileName + "/" + System.currentTimeMillis() + ".png";
				System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
				ImageIO.write((RenderedImage)offImage, "png", new File(fileNameTemp));
				numberOfPics--;
			}
			catch(Exception exc){System.out.println(exc);}
		}
		if(!isStartingWithGui && current_nr_of_pic_in_series == max_nr_of_pic_in_series)
		{
			current_nr_of_pic_in_series = 0;
		}
	}
	
	public void addParameterAccessor(ParameterAccessor parameterAccessor)
	{
		this.parameterAccessor = parameterAccessor;
	}
}