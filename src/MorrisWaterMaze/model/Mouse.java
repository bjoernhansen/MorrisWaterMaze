package MorrisWaterMaze.model;

import MorrisWaterMaze.Calculations;
import MorrisWaterMaze.Controller;
import MorrisWaterMaze.parameter.ParameterAccessor;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public class Mouse 
{
	private Point2D position;				// aktueller Aufenthaltsort der Maus	
	private final double speed;                    // Geschwindigkeit der Maus; default: 5
	private double angle;                    // bestimmt in welche Richtung die Maus schwimmt
	private final double maximumSwimmingTime;		// maximale Schwimmzeit; default: 0 (no restriction)
	private final boolean isStartPositionLeft;	// gibt an, ob die Maus am linken oder rechten Rand des Pools erscheint; default: true
	private double trainingLevel;                    // Trainingslevel der Maus; [0, 1]; default: 0.5
		   public double stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	public boolean isSwimming;					// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde

	private final ArrayList<Point2D> escapeRoutePoints = new ArrayList<>(0);
	public ArrayList<Double> timeSteps = new ArrayList<>(0);
		
	static final double RADIUS = 3;							// Radius des die Maus repräsentierenden Kreises
	private static final double FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180�
		
	public Mouse(ParameterAccessor parameterAccessor)
	{		
		this.position = new Point2D.Double();
		this.maximumSwimmingTime = parameterAccessor.getMaximumMouseSwimmingTime();
		this.trainingLevel = parameterAccessor.getMouseTrainingLevel();
		this.stepLengthBias = parameterAccessor.getStepLengthBias();
		this.isStartPositionLeft = parameterAccessor.isMouseStartPositionLeft();
		this.speed = parameterAccessor.mouseSpeed();
	}
		
	public void determineStartingPosition(Pool pool)// Startposition von der Maus
	{
		if(this.isStartPositionLeft)
		{
			this.position.setLocation(pool.border.getCenterX()- Pool.RADIUS + RADIUS, pool.border.getCenterY());
		}
		else
		{
			this.position.setLocation(pool.border.getCenterX() + Pool.RADIUS - RADIUS, pool.border.getCenterY());
		}
		this.angle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(this.position, pool.center));
		this.isSwimming = true;
		this.escapeRoutePoints.clear();
		this.timeSteps.clear();
		this.escapeRoutePoints.add(this.position);
		this.timeSteps.add((double)0);
	}
		
	public void move(Pool pool, Platform platform, double timeStep)
	{
		double realTimeStep;
		if(this.maximumSwimmingTime != 0 && timeStep + this.timeSteps.get(this.timeSteps.size()-1) > this.maximumSwimmingTime)
		{
			realTimeStep = this.maximumSwimmingTime - this.timeSteps.get(this.timeSteps.size()-1);
			this.isSwimming = false;
		}
		else
		{
			realTimeStep = timeStep;
		}
		Point2D newPos = new Point2D.Double(	this.position.getX()+this.speed*realTimeStep*Math.cos(this.angle),
												this.position.getY()+this.speed*realTimeStep*Math.sin(this.angle));
		if(pool.collisionSize.contains(newPos))
		{			
			double meanAngle;
			Point2D movementVector = Calculations.calculateVector(this.position, newPos);
			Point2D newPosMousePlatformVector = Calculations.calculateVector(newPos, platform.getCenter());
			
			if(this.trainingLevel > Math.random() && FIELD_OF_VIEW/2 >= Calculations.angle(movementVector, newPosMousePlatformVector))
			{
				meanAngle = Calculations.calculatePolarAngle(newPosMousePlatformVector);
				//this.angle = MyMath.gausian(meanAngle, (1-this.trainingLevel)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
				
			}
			else
			{
				meanAngle = Calculations.calculatePolarAngle(movementVector);
				//this.angle = MyMath.gausian(meanAngle, 22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
			}			
			this.angle = Calculations.gaussian(meanAngle, (1-this.trainingLevel)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
		}
		else // Schnittstelle von Pool und Mausschritt
		{			
			newPos = Calculations.circleLineIntersection(pool.center, Pool.RADIUS - RADIUS, this.position, newPos);
			Point2D newPosCenterVector = Calculations.calculateVector(newPos, pool.center);
			double direction = Line2D.relativeCCW(pool.center.getX(), pool.center.getY(), newPos.getX(), newPos.getY(), this.position.getX(), this.position.getY());
			this.angle = Calculations.calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + Calculations.gaussian(0, Math.PI/12, Math.PI/6));
			this.isSwimming = true;
		}		
		Line2D lastMove = new Line2D.Double(this.position, newPos);
		if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
		{	
			newPos = Objects.requireNonNull(Calculations.clipLine(lastMove, platform.getBounds())).getP1();
			this.isSwimming = false;
		}
		double timeOfLastMove = this.position.distance(newPos)/this.speed; //?
		double totalSimulationTime = timeOfLastMove + this.timeSteps.get(this.timeSteps.size()-1);
				
		this.timeSteps.add(totalSimulationTime);
		this.escapeRoutePoints.add(newPos);
		this.position = newPos;
	}

	
	public void paintTrajectory(Graphics2D g2d)
	{
		if(!this.escapeRoutePoints.isEmpty())
		{			
			for (int i = 0; i< this.escapeRoutePoints.size()-1; i++)
			{
				g2d.setColor(Color.BLACK);
				g2d.draw(	new Line2D.Double(Calculations.scalePoint(this.escapeRoutePoints.get(i),
							Controller.ZOOM_FACTOR),
							Calculations.scalePoint(this.escapeRoutePoints.get(i+1),
							Controller.ZOOM_FACTOR)));
				g2d.fillOval(	(int)(Controller.ZOOM_FACTOR *(this.escapeRoutePoints.get(i+1).getX()-0.5)),
								(int)(Controller.ZOOM_FACTOR *(this.escapeRoutePoints.get(i+1).getY()-0.5)),
								(int) Controller.ZOOM_FACTOR, (int) Controller.ZOOM_FACTOR);
			}	
			if(Controller.isStartingWithGui)
			{
				g2d.setColor(Controller.light_grey);
				g2d.fillOval(	(int)(Controller.ZOOM_FACTOR *(this.escapeRoutePoints.get(this.escapeRoutePoints.size()-1).getX()-RADIUS)),
								(int)(Controller.ZOOM_FACTOR *(this.escapeRoutePoints.get(this.escapeRoutePoints.size()-1).getY()-RADIUS)),
								(int)(Controller.ZOOM_FACTOR *RADIUS*2), (int)(Controller.ZOOM_FACTOR *RADIUS*2));
			}
		}		
	}

	public double getTrainingLevel() {
		return trainingLevel;
	}

	public void setTrainingLevel(double trainingLevel) {
		this.trainingLevel = trainingLevel;
	}
}
