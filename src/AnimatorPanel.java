import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class AnimatorPanel extends JPanel implements Runnable, ActionListener
{
	private static final long serialVersionUID = 1L;
	static private int counter;
	
	static double zoom_factor = 2;
	static double pool_radius = zoom_factor * 61;
	
	static double seconds_per_simstep = 0.1;
	double pictures_per_second = 75;
	//double mouse_speed = zoom_factor * seconds_per_simstep * 5;
	
	double mouse_radius = zoom_factor * 3;
	
	double start_angle = Math.random()*2*Math.PI;
	static double pool_border_distance = zoom_factor * 75;
	static double image_center = pool_radius + pool_border_distance;
	double image_size = 2 * image_center;
	double platform_site_length = zoom_factor * 10;
	int total_number_of_sim = 5;
	int remaining_number_of_sim = total_number_of_sim;
	
	Dimension dim = new Dimension((int)image_size, (int)image_size);
	
	static boolean loop = false;
	Thread animator = new Thread();
	Image offImage = new BufferedImage(dim.height, dim.width, BufferedImage.TYPE_INT_RGB); 
    Graphics2D offGraphics;
	
	JButton button1, neustart_button;
	JPanel main_panel;
	JPanel time_panel;
	JLabel time_label;
		
	Pool pool = new Pool(image_center, image_center, pool_radius, mouse_radius);	
	Mouse mouse = new Mouse(pool, mouse_radius, start_angle, zoom_factor);
	
	
	Rectangle2D platform;

	
	static DecimalFormat decimal_format = new DecimalFormat("0.0");
	long last_painted = System.currentTimeMillis();
		
	static double platform_area_radius = 0.65*pool_radius;
	static Ellipse2D platform_area = new Ellipse2D.Double(	image_center - platform_area_radius, 
													image_center - platform_area_radius, 
													2 *platform_area_radius, 2 * platform_area_radius);
	static Point2D pool_center = new Point2D.Double(AnimatorPanel.image_center, AnimatorPanel.image_center);
		
	public AnimatorPanel()
	{
		this.setLayout(null);		
		offGraphics = (Graphics2D) offImage.getGraphics();
		
		button1 = new JButton("Starten");
		neustart_button = new JButton("Neustart");
		time_label = new JLabel(print_time());
		main_panel = new JPanel(new GridLayout(3,1));
		main_panel.setBounds(650, 25, 220, 120);
		main_panel.setBorder(BorderFactory.createEtchedBorder());
		button1.addActionListener(this);
		neustart_button.addActionListener(this);		
		this.add(main_panel);
		main_panel.add(button1);
		main_panel.add(neustart_button);
		main_panel.add(time_label);
		
		neustart();
		
		animator = new Thread(this);		
	    animator.start();
	}
	
	public void run()
	{
		while (Thread.currentThread() == animator)
		{			
			try{Thread.sleep(0);}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
			repaint();
		}
	}	
	
	void CalculateSim()
	{
		
		
		if(loop && mouse.is_swimming)
		{
			counter++;
			if(counter < 10)
			{
				
				mouse.move2(this, pool, platform, 5);
			}
			else
			{
				/*
				mouse.is_swimming = false;
				System.out.println(print_time());
				remaining_number_of_sim--;
				if (remaining_number_of_sim > 0){
					neustart();
					loop = true;
				}
				*/
			}
		}
		
	}
	
	void drawOffImage(Graphics2D g2d)
	{		
		g2d.setColor(Color.gray);
		g2d.fillRect(0, 0, dim.height, dim.width);		
		pool.paint(g2d);		
		//mouse.paint(g2d);
		
		g2d.setColor(Color.green);
		g2d.fill(platform);
		
		g2d.setColor(Color.red);
		g2d.draw(platform_area);
		
		g2d.fillOval((int)image_center-2, (int)image_center-2, 4, 4);
		
		for (int i = 0; i< mouse.fluchtweg_punkte.size()-1; i++)
		{
			g2d.draw(new Line2D.Double(mouse.fluchtweg_punkte.get(i), mouse.fluchtweg_punkte.get(i+1)));
			
		}

		
	}	
	
	public void paintComponent(Graphics g)
	{
		CalculateSim();
		
		if(System.currentTimeMillis() - last_painted > 1000/pictures_per_second)
		{		
			Graphics2D g2 = (Graphics2D) g.create();			
			drawOffImage(offGraphics);				
			super.paintComponent(g);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
	    	g2.drawImage(offImage, 25, 25, null);	
			g2.dispose();
			last_painted = System.currentTimeMillis();
		}
	}
	
		
	void neustart()
	{
		counter = 0;
		button1.setText("Starten");
		loop = false;
		mouse.is_swimming = true;
		platform = make_platform( pool_radius, platform_site_length);
		time_label.setText(print_time());
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
			
			//mouse.move2(this, pool, platform, 1);
			
		} 
		else if(o == neustart_button)
		{
			neustart();
			remaining_number_of_sim = total_number_of_sim;
		}
	}
	
	static String print_time()
	{
		return "Simulationszeit: " + decimal_format.format(counter*seconds_per_simstep) + " Sekunden";
	}
	
	Rectangle2D make_platform(double pool_radius, double platform_site_length)
	{
		double random = (int)(Math.random()*4);
		double alpha = 0.25*Math.PI + random *0.5*Math.PI;
		//alpha = Math.PI/4;
		
		return new Rectangle2D.Double(	image_center + 0.5*(pool_radius-10*Math.sqrt(2))*Math.cos(alpha)  +(random < 1 || random > 2 ? -10: 0), 
										image_center + 0.5*(pool_radius-10*Math.sqrt(2))*Math.sin(alpha)  +(random < 2  ? -10: 0),
										platform_site_length, platform_site_length);
	}
	
}