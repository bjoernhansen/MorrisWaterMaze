package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

public class Mouse 
{
	private Point2D position;				// aktueller Aufenthaltsort der Maus	
	private final double speed;                    // Geschwindigkeit der Maus; default: 5
	private double angle;                    // bestimmt in welche Richtung die Maus schwimmt
	private final double maximumSwimmingTime;		// maximale Schwimmzeit; default: 0 (no restriction)
	private final boolean isStartPositionLeft;	// gibt an, ob die Maus am linken oder rechten Rand des Pools erscheint; default: true
	double trainingLevel, 					// Trainingslevel der Maus; [0, 1]; default: 0.5
		   stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	boolean is_swimming;					// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde

	private final ArrayList<Point2D> fluchtweg_punkte = new ArrayList<>(0);
	ArrayList<Double> time_steps = new ArrayList<>(0);
		
	static final double RADIUS = 3;							// Radius des die Maus repräsentierenden Kreises
	private static final double FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180�
		
	Mouse(ParameterAccessor parameterAccessor)
	{		
		this.position = new Point2D.Double();
		this.maximumSwimmingTime = parameterAccessor.getMaximumMouseSwimmingTime();
		this.trainingLevel = parameterAccessor.getMouseTrainingLevel();
		this.stepLengthBias = parameterAccessor.getStepLengthBias();
		this.isStartPositionLeft = parameterAccessor.isMouseStartPositionLeft();
		this.speed = parameterAccessor.mouseSpeed();
	}
		
	void getRandomStartingPosition(Pool pool)// Startposition von der Maus
	{
		if(this.isStartPositionLeft)
		{
			this.position.setLocation(pool.border.getCenterX()- pool.radius + RADIUS, pool.border.getCenterY());
		}
		else
		{
			this.position.setLocation(pool.border.getCenterX() + pool.radius - RADIUS, pool.border.getCenterY());	
		}
		this.angle = Math.PI*(Math.random()-0.5) + MyMath.calculate_polar_angle(MyMath.calculate_vector(this.position, pool.center));		
		this.is_swimming = true;
		this.fluchtweg_punkte.clear();
		this.time_steps.clear();
		this.fluchtweg_punkte.add(this.position);
		this.time_steps.add((double)0);
	}
		
	void move(Pool pool, Rectangle2D platform, double time_step)
	{
		double real_time_step;
		if(this.maximumSwimmingTime != 0 && time_step + this.time_steps.get(this.time_steps.size()-1) > this.maximumSwimmingTime)
		{
			real_time_step = this.maximumSwimmingTime - this.time_steps.get(this.time_steps.size()-1);
			this.is_swimming = false;
		}
		else
		{
			real_time_step = time_step;
		}
		Point2D new_pos = new Point2D.Double(	this.position.getX()+this.speed*real_time_step*Math.cos(this.angle),
												this.position.getY()+this.speed*real_time_step*Math.sin(this.angle));
		if(pool.collision_size.contains(new_pos))
		{			
			double mean_angle;
			Point2D movement_vector = MyMath.calculate_vector(this.position, new_pos);
			Point2D new_pos_mouse_platform_vector = MyMath.calculate_vector(new_pos, SimulationController.center_of_platform);
			
			if(this.trainingLevel > Math.random() && FIELD_OF_VIEW/2 >= MyMath.angle(movement_vector, new_pos_mouse_platform_vector))
			{
				mean_angle = MyMath.calculate_polar_angle(new_pos_mouse_platform_vector);
				//this.angle = MyMath.gausian(mean_angle, (1-this.training_level)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
				
			}
			else
			{
				mean_angle = MyMath.calculate_polar_angle(movement_vector);
				//this.angle = MyMath.gausian(mean_angle, 22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
			}			
			this.angle = MyMath.gausian(mean_angle, (1-this.trainingLevel)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
		}
		else // Schnittstelle von Pool und Mausschritt
		{			
			new_pos = MyMath.circle_line_intersection(pool.center, pool.radius - RADIUS, this.position, new_pos);			
			Point2D new_pos_center_vector = MyMath.calculate_vector(new_pos, pool.center);			
			double direction = Line2D.relativeCCW(pool.center.getX(), pool.center.getY(), new_pos.getX(), new_pos.getY(), this.position.getX(), this.position.getY());
			this.angle = MyMath.calculate_polar_angle(new_pos_center_vector) - direction * (Math.PI/3 + MyMath.gausian(0, Math.PI/12, Math.PI/6));
			this.is_swimming = true;
		}		
		Line2D last_move = new Line2D.Double(this.position, new_pos);			
		if(last_move.intersects(platform)) // Schnittstelle von Maus und Plattform
		{	
			new_pos = Objects.requireNonNull(MyMath.clipLine(last_move, platform)).getP1();
			this.is_swimming = false;
		}
		double time_of_last_move = this.position.distance(new_pos)/this.speed; //?
		double total_simulation_time = time_of_last_move + this.time_steps.get(this.time_steps.size()-1);
				
		this.time_steps.add(total_simulation_time);
		this.fluchtweg_punkte.add(new_pos);
		this.position = new_pos;
	}

	
	void paint_trajectory(Graphics2D g2d)
	{
		if(!this.fluchtweg_punkte.isEmpty())
		{			
			for (int i = 0; i< this.fluchtweg_punkte.size()-1; i++)
			{
				g2d.setColor(Color.BLACK);
				g2d.draw(	new Line2D.Double(MyMath.scale_point(this.fluchtweg_punkte.get(i), 
							SimulationController.zoom_factor),
							MyMath.scale_point(this.fluchtweg_punkte.get(i+1),
							SimulationController.zoom_factor)));
				g2d.fillOval(	(int)(SimulationController.zoom_factor*(this.fluchtweg_punkte.get(i+1).getX()-0.5)),
								(int)(SimulationController.zoom_factor*(this.fluchtweg_punkte.get(i+1).getY()-0.5)),
								(int) SimulationController.zoom_factor, (int) SimulationController.zoom_factor);
			}	
			if(SimulationController.isStartingWithGui)
			{
				g2d.setColor(SimulationController.light_grey);
				g2d.fillOval(	(int)(SimulationController.zoom_factor*(this.fluchtweg_punkte.get(this.fluchtweg_punkte.size()-1).getX()-RADIUS)),
								(int)(SimulationController.zoom_factor*(this.fluchtweg_punkte.get(this.fluchtweg_punkte.size()-1).getY()-RADIUS)),
								(int)(SimulationController.zoom_factor*RADIUS*2), (int)(SimulationController.zoom_factor*RADIUS*2));
			}
		}		
	}
	
}
