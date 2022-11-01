package MorrisWaterMaze.model;

import MorrisWaterMaze.Calculations;
import MorrisWaterMaze.SimulationController;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.MouseParameterAccessor;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public class MouseMovement implements Paintable
{
	private Point2D coordinates;				// aktueller Aufenthaltsort der Maus
	private final double speed;                    // Geschwindigkeit der Maus; default: 5
	private double polarAngle;                    // bestimmt in welche Richtung die Maus schwimmt
	private final double maximumSwimmingDuration;		// maximale Schwimmzeit; default: 0 (no restriction)
	private final boolean isStartingPositionLeft;	// gibt an, ob die Maus am linken oder rechten Rand des Pools erscheint; default: true
	private double trainingLevel;                    // Trainingslevel der Maus; [0, 1]; default: 0.5
	public double stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	private boolean isSwimming;					// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde

	private final boolean
		isBodyToBeDrawn;
	
	public final ArrayList<Point2D> escapeRoutePoints = new ArrayList<>();
	private final ArrayList<Double>
		timeSteps = new ArrayList<>();
		
	public static final double RADIUS = 3;							// Radius des die Maus repräsentierenden Kreises
	private static final double FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180�
	private double maximumDurationOfNextSimulationStep;
	private double durationOfNextSimulationStep;
	
	public MouseMovement(MouseParameterAccessor parameterAccessor)
	{		
		coordinates = new Point2D.Double();
		maximumSwimmingDuration = parameterAccessor.getMaximumMouseSwimmingTime();
		trainingLevel = parameterAccessor.getMouseTrainingLevel();
		stepLengthBias = parameterAccessor.getStepLengthBias();
		isStartingPositionLeft = parameterAccessor.isMouseStartingPositionLeft();
		speed = parameterAccessor.mouseSpeed();
		isBodyToBeDrawn = parameterAccessor.isStartingWithGui();
	}
		
	public void determineStartingPosition(Pool pool)// Startposition von der Maus
	{
		if(isStartingPositionLeft)
		{
			coordinates.setLocation(pool.border.getCenterX()- Pool.RADIUS + RADIUS, pool.border.getCenterY());
		}
		else
		{
			coordinates.setLocation(pool.border.getCenterX() + Pool.RADIUS - RADIUS, pool.border.getCenterY());
		}
		polarAngle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(coordinates, pool.center));
		isSwimming = true;
		escapeRoutePoints.clear();
		timeSteps.clear();
		escapeRoutePoints.add(coordinates);
		timeSteps.add(0.0);
	}
		
	public void move(Pool pool, Platform platform, double maximumDurationOfNextSimulationStepValue)
	{
		maximumDurationOfNextSimulationStep = maximumDurationOfNextSimulationStepValue;
		durationOfNextSimulationStep = getDurationOfNextSimulationStep();
		
		if(hasReachedSwimmingTimeLimit())
		{
			isSwimming = false;
		}
		
		Point2D newCoordinates = calculateNewCoordinates();
		if(pool.collisionSize.contains(newCoordinates))
		{			
			double meanAngle;
			Point2D movementVector = Calculations.calculateVector(coordinates, newCoordinates);
			Point2D newPosMousePlatformVector = Calculations.calculateVector(newCoordinates, platform.getCenter());
			
			if(trainingLevel > Math.random() && FIELD_OF_VIEW/2 >= Calculations.angle(movementVector, newPosMousePlatformVector))
			{
				meanAngle = Calculations.calculatePolarAngle(newPosMousePlatformVector);
			}
			else
			{
				meanAngle = Calculations.calculatePolarAngle(movementVector);
			}			
			polarAngle = Calculations.gaussian(meanAngle, (1-trainingLevel)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
		}
		else // Schnittstelle von Pool und Mausschritt
		{			
			newCoordinates = Calculations.circleLineIntersection(pool.center, Pool.RADIUS - RADIUS, coordinates, newCoordinates);
			Point2D newPosCenterVector = Calculations.calculateVector(newCoordinates, pool.center);
			double direction = Line2D.relativeCCW(pool.center.getX(), pool.center.getY(), newCoordinates.getX(), newCoordinates.getY(), coordinates.getX(), coordinates.getY());
			polarAngle = Calculations.calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + Calculations.gaussian(0, Math.PI/12, Math.PI/6));
			isSwimming = true;
		}		
		Line2D lastMove = new Line2D.Double(coordinates, newCoordinates);
		if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
		{	
			newCoordinates = Objects.requireNonNull(Calculations.clipLine(lastMove, platform.getBounds())).getP1();
			isSwimming = false;
		}
		double timeOfLastMove = coordinates.distance(newCoordinates)/speed; //?
		double totalSimulationTime = timeOfLastMove + getTotalDurationOfCurrentSimulation();
				
		timeSteps.add(totalSimulationTime);
		escapeRoutePoints.add(newCoordinates);
		coordinates = newCoordinates;
	}
	
	private Point2D calculateNewCoordinates()
	{
		double stepLength = speed * durationOfNextSimulationStep;
		return new Point2D.Double(	coordinates.getX() + stepLength * Math.cos(polarAngle),
									coordinates.getY() + stepLength * Math.sin(polarAngle));
	}
	
	private double getDurationOfNextSimulationStep()
	{
		if(hasReachedSwimmingTimeLimit())
		{
			return maximumSwimmingDuration - getTotalDurationOfCurrentSimulation();
		}
		return maximumDurationOfNextSimulationStep;
	}
	
	private boolean hasReachedSwimmingTimeLimit()
	{
		return hasMaximumSwimmingTimeBeenSet()
			&& maximumDurationOfNextSimulationStep + getTotalDurationOfCurrentSimulation() > maximumSwimmingDuration;
	}
	
	private boolean hasMaximumSwimmingTimeBeenSet()
	{
		return maximumSwimmingDuration != 0;
	}
	

	public void setTrainingLevel(double trainingLevel) {
		this.trainingLevel = trainingLevel;
	}

	public boolean isSwimming() {
		return isSwimming;
	}
	
	public double getTotalDurationOfCurrentSimulation()
	{
		return timeSteps.get(timeSteps.size()-1);
	}
	
	public boolean isBodyToBeDrawn()
	{
		return isBodyToBeDrawn;
	}
}
