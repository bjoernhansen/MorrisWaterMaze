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
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Simulation extends JPanel implements Runnable, ActionListener, ChangeListener
{
	private static final long
		serialVersionUID = 4301592417000711331L;
	public static final String LOG_DIRECTORY_NAME = "logs/";
	
	
	static boolean isStartingWithGui;
	static int 	max_nr_of_pic_in_series;
	private static int current_nr_of_pic_in_series = 0;
	static int number_of_pics = 0;
	static double pic_time_frame_upper_bound, 
			      pic_time_frame_lower_bound;
	
	private static int counter;
	static boolean loop = false;
	double pictures_per_second = 75;
	static double zoom_factor = 4;
	private static final double pool_radius = 65;
	private static final double pool_border_distance = 25;
	private static final double image_center = pool_radius + pool_border_distance;
	private static final double image_size = 2 * image_center;
	private static final Dimension dim = new Dimension((int)(zoom_factor*image_size), (int)(zoom_factor*image_size));
		
	static int totalNumberOfSimulations,
			   remainingNumberOfSimulations;
	private static double sum_of_search_time = 0;
	private long last_painted = System.currentTimeMillis();
	
	
	
	private static final ArrayList<Double> search_time = new ArrayList<>(0);
	
	private static final Rectangle2D platform = make_platform();
	static Point2D center_of_platform = new Point2D.Double(platform.getCenterX(), platform.getCenterY());
	private static final AffineTransform AFFINE_TRANSFORMATION = new AffineTransform(zoom_factor, 0, 0, zoom_factor, 0, 0);
	
	private Thread animator = new Thread();
	private static final Image offImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    private static final Graphics2D offGraphics = getGraphics(offImage);
	private static JButton button1;
	private final JButton neustart_button;
	private final JSpinner mouse_level;
	private final JSpinner number_of_sim;
	
	private static final Pool pool = new Pool(image_center, image_center, pool_radius);
	
	static Mouse mouse;
	static final DecimalFormat decimal_format = new DecimalFormat("0.000");
	static final Font myFontPLAIN36 = new Font("Dialog", Font.BOLD, 36);
		
	static String fileName;
	static File pictureDirectory;
	
	static Color dark_grey = new Color(75, 75, 75);	
	static Color light_grey = new Color(150, 150, 150);
	
	private ParameterAccessor
		parameterAccessor;
	
	
	Simulation()
	{			
		this.setLayout(null);		
		button1 = new JButton("Starten");
		JLabel mouse_level_label = new JLabel("training level");
		JLabel number_of_sim_label = new JLabel("number of simulations");
		this.neustart_button = new JButton("Neustart");
		this.mouse_level = new JSpinner(new SpinnerNumberModel(mouse.trainingLevel, 0.0, 1.0, 0.01));
		this.number_of_sim = new JSpinner(new SpinnerNumberModel(totalNumberOfSimulations, 1.0, Double.MAX_VALUE, 1.00));
		this.mouse_level.addChangeListener(this); 
		this.number_of_sim.addChangeListener(this);
		JPanel main_panel = new JPanel(new GridLayout(3, 2));
		main_panel.setBounds(675, 25, 300, 120);
		main_panel.setBorder(BorderFactory.createEtchedBorder());
		button1.addActionListener(this);
		this.neustart_button.addActionListener(this);		
		this.add(main_panel);
		main_panel.add(button1);
		main_panel.add(this.neustart_button);
		main_panel.add(mouse_level_label);
		main_panel.add(this.mouse_level);
		main_panel.add(number_of_sim_label);
		main_panel.add(this.number_of_sim);
		
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
				int pause_between_simulation_steps = 100;
				Thread.sleep(pause_between_simulation_steps);}
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
			if(mouse.is_swimming)
			{
				counter++;
				double time_step =  Math.log(mouse.stepLengthBias / nonZeroRandom());
				
				mouse.move(pool, platform, time_step);
			}
			else if (remainingNumberOfSimulations >= 1)
			{
				double last_search_time = mouse.time_steps.get(mouse.time_steps.size()-1);
				search_time.add(last_search_time);
				sum_of_search_time += last_search_time;
				
				System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations + 1) + " of " + totalNumberOfSimulations + ", simulation time: " + last_search_time);
				remainingNumberOfSimulations--;
				
				if(	number_of_pics > 0 && last_search_time >= pic_time_frame_lower_bound && last_search_time <= pic_time_frame_upper_bound)
			    {					
					saveImage();					
			    }
							
				if(remainingNumberOfSimulations == 0)
				{
					System.out.println("\nDurchschnittliche Suchzeit: " + (sum_of_search_time/ totalNumberOfSimulations) + "\n");
					
					BufferedWriter bw;
					String file_name_temp = LOG_DIRECTORY_NAME + fileName + "/" + fileName + ".txt";
					System.out.println("Schreibe Datei: " + file_name_temp);				
				    try 
				    {				    	
				    	search_time.size();
				        bw = new BufferedWriter(new FileWriter(file_name_temp));
						for (Double aDouble : search_time)
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
			// weißer Hintergrund
			offGraphics.setColor(Color.white);
			offGraphics.fillRect(0, 0, (int)zoom_factor*dim.height, (int)zoom_factor*dim.width);	
			
			// der Pool
			pool.paint(offGraphics, AFFINE_TRANSFORMATION);
			
			// die Plattform
			offGraphics.setColor(dark_grey);
			offGraphics.fill(AFFINE_TRANSFORMATION.createTransformedShape(platform));
			offGraphics.setColor(Color.black);
			offGraphics.fillOval((int)(zoom_factor*(image_center-1)), (int)(zoom_factor*(image_center-1)), (int)(2*zoom_factor), (int)(2*zoom_factor));
		}
		mouse.paint_trajectory(offGraphics);
	}	
		
	@Override
	public void paintComponent(Graphics g)
	{
		calculateSim();
		if(this.pictures_per_second != 0 && System.currentTimeMillis() - this.last_painted > 1000/this.pictures_per_second)
		{		
			Graphics2D g2 = (Graphics2D) g.create();		
			drawOffImage();				
			super.paintComponent(g);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
	    	g2.drawImage(offImage, 25, 25, null);	
			g2.dispose();
			this.last_painted = System.currentTimeMillis();
		}	
	}
			
	static void restart()
	{
		counter = 0;
		if(isStartingWithGui){button1.setText("Starten");}
		mouse.get_random_pos(pool);
	}
		
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		if (o == button1)
		{
			if(loop)
			{
				button1.setText("Starten");
			}
			else
			{
				button1.setText("Anhalten");
			}
			loop = !loop;
			this.mouse_level.setEnabled(false);
			this.number_of_sim.setEnabled(false);
		} 
		else if(o == this.neustart_button)
		{
			restart();
			loop = false;
			remainingNumberOfSimulations = totalNumberOfSimulations;
			search_time.clear();
			sum_of_search_time = 0;
			this.mouse_level.setEnabled(true);		
			this.number_of_sim.setEnabled(true);
		}
	}
		
	public void stateChanged(ChangeEvent e)
    {
        Object o = e.getSource();           
        if(o==this.mouse_level)
        {
            if( 0 > Double.parseDouble(this.mouse_level.getValue().toString()))
            {
            	this.mouse_level.setValue(0.0);
            }
            else if( 1 < Double.parseDouble(this.mouse_level.getValue().toString()))
            {
            	this.mouse_level.setValue(1.0);
            }
            mouse.trainingLevel = Double.parseDouble(this.mouse_level.getValue().toString());
        }
        if(o==this.number_of_sim)
        {
            if( 0 > Double.parseDouble(this.mouse_level.getValue().toString()))
            {
            	this.number_of_sim.setValue(0.0);
            }
            else if( 1 < Double.parseDouble((this.mouse_level).getValue().toString()))
            {
            	this.number_of_sim.setValue(1.0);
            }
            remainingNumberOfSimulations = totalNumberOfSimulations = (int)Double.valueOf((this.number_of_sim).getValue().toString()).doubleValue();
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
		
	static Rectangle2D make_platform()
	{
		double alpha = 0.25*Math.PI;
		double platform_site_length = 10;
		double outer_corner_radius = 0.5*(pool_radius+ platform_site_length *Math.sqrt(2));
		return new Rectangle2D.Double(	image_center + outer_corner_radius*Math.cos(alpha) - platform_site_length, 
										image_center + outer_corner_radius*Math.sin(alpha) - platform_site_length,
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
				number_of_pics--;
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