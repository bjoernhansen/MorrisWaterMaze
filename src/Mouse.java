import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Mouse
{
	Point2D position;
	Ellipse2D body;
	double speed;
	double angle;
	double radius;
	double sum_of_mouse;
	boolean is_swimming = true;
	double trainig_level;

	static double mouse_speed = 3;
	
	ArrayList<Point2D> fluchtweg_punkte = new ArrayList<Point2D>(0);
	
	Mouse(Pool pool, double radius, double angle, double zoom_factor)
	{		
		this.position = new Point2D.Double();
		this.get_random_pos(pool);
		this.speed = zoom_factor * mouse_speed;
		this.angle = angle;
		this.radius = radius;
		this.body = new Ellipse2D.Double(	this.position.getX() - this.radius, 
											this.position.getY() - this.radius, 
											2*this.radius, 2*this.radius);
		this.trainig_level = 0;
		
	}
		
	void get_random_pos(Pool pool)
	{
		/*double stabling_radius = Math.random()*0.9*(pool.radius-this.radius);		
		double rand_x = Math.random()*stabling_radius*Math.cos(2*Math.random()*Math.PI);
		double rand_y = Math.random()*stabling_radius*Math.sin(2*Math.random()*Math.PI);		
		this.position.setLocation(pool.border.getCenterX()+rand_x, pool.border.getCenterY()+rand_y);
		this.body = new Ellipse2D.Double(	this.position.getX() - this.radius, 
											this.position.getY() - this.radius, 
											2*this.radius, 2*this.radius);	*/
		if(Math.random()<0.5)
		{
			if(Math.random() < 0.5)
			{
				this.position.setLocation(pool.border.getCenterX()+ pool.radius * 0.5, pool.border.getCenterY());	
			}
			else
			{
				this.position.setLocation(pool.border.getCenterX()- pool.radius * 0.5, pool.border.getCenterY());
			}
		}
		else
		{
			if(Math.random() < 0.5)
			{
				this.position.setLocation(pool.border.getCenterX(), pool.border.getCenterY()+ pool.radius * 0.5);	
			}
			else
			{
				this.position.setLocation(pool.border.getCenterX(), pool.border.getCenterY()- pool.radius * 0.5);
			}
		}
		
		this.body = new Ellipse2D.Double(	this.position.getX() - this.radius, 
											this.position.getY() - this.radius, 
											2*this.radius, 2*this.radius);
		this.fluchtweg_punkte.clear();
		this.fluchtweg_punkte.add(this.position);
	}
	
	void paint(Graphics2D g2d)
	{		
		g2d.setPaint(Color.orange);
		g2d.fill(body);		
	}
	
	
	void move2(AnimatorPanel animator, Pool pool, Rectangle2D platform, double step_time)
	{
		Point2D new_pos = new Point2D.Double(	this.position.getX()+this.speed*step_time*Math.cos(this.angle),
												this.position.getY()+this.speed*step_time*Math.sin(this.angle));
		this.position = new_pos;
		this.fluchtweg_punkte.add(new_pos);	
		this.angle += 0.5*Math.PI*(Math.random()-0.5);
		
		
		/*
		
		if(pool.collison_size.contains(new_pos))
		{									
			if(Math.random() < 0.05)
			{
				if(AnimatorPanel.platform_area.contains(this.position))
				{
					this.angle += 0.5*Math.PI*(Math.random()-0.5) ;
				}
				else 
				{
					int clockwise = calculate_angle(this.position, new_pos, AnimatorPanel.pool_center)>0 ? -1 : 1;
					this.angle += 0.5*Math.PI*(Math.random()-0.5)+clockwise*this.trainig_level*0.125*Math.PI;
				}
			}			
			this.position.setLocation(new_pos);
			this.body = new Ellipse2D.Double(	this.position.getX() - this.radius, 
												this.position.getY() - this.radius, 
												2*this.radius, 2*this.radius);						
			
			
			
			if (this.body.intersects(platform))
			{
				this.is_swimming = false;
				System.out.println(AnimatorPanel.print_time());
				animator.remaining_number_of_sim--;
				if (animator.remaining_number_of_sim > 0)
				{
					animator.neustart();
					AnimatorPanel.loop = true;
				}
			}			
		}
		else 
		{
			this.angle += 0.5*Math.PI*(((int)2*Math.random())-0.5);
			this.move(animator, pool, platform);
		}
		*/
		
		
	}
	
	void move(AnimatorPanel animator, Pool pool, Rectangle2D platform)
	{
		Point2D new_pos = new Point2D.Double(	this.position.getX()+this.speed*Math.cos(this.angle),
												this.position.getY()+this.speed*Math.sin(this.angle));		
		if(pool.collison_size.contains(new_pos))
		{									
			if(Math.random() < 0.05)
			{
				if(AnimatorPanel.platform_area.contains(this.position))
				{
					this.angle += 0.5*Math.PI*(Math.random()-0.5) ;
				}
				else 
				{
					int clockwise = calculate_angle(this.position, new_pos, AnimatorPanel.pool_center)>0 ? -1 : 1;
					this.angle += 0.5*Math.PI*(Math.random()-0.5)+clockwise*this.trainig_level*0.125*Math.PI;
				}
			}			
			this.position.setLocation(new_pos);
			this.body = new Ellipse2D.Double(	this.position.getX() - this.radius, 
												this.position.getY() - this.radius, 
												2*this.radius, 2*this.radius);						
			
			
			
			if (this.body.intersects(platform))
			{
				this.is_swimming = false;
				System.out.println(AnimatorPanel.print_time());
				animator.remaining_number_of_sim--;
				if (animator.remaining_number_of_sim > 0)
				{
					animator.neustart();
					AnimatorPanel.loop = true;
				}
			}			
		}
		else 
		{
			this.angle += 0.5*Math.PI*(((int)2*Math.random())-0.5);
			this.move(animator, pool, platform);
		}
	}
	
	static Point2D calculate_vector(Point2D start_point, Point2D end_point)
	{
		return new Point2D.Double(end_point.getX()-start_point.getX(), end_point.getY()- start_point.getY());
	}
	
	static double calculate_angle(Point2D vector1, Point2D vector2)
	{		
		return Math.atan2(vector1.getY(),vector1.getX()) - Math.atan2(vector2.getY(),vector2.getX());
	}
	
	static double calculate_angle(Point2D start_point, Point2D end_point, Point2D center_point)
	{		
		return calculate_angle(calculate_vector(start_point, end_point), calculate_vector(start_point, center_point) );
	}
	
}
