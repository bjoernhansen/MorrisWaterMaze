package MorrisWaterMaze.model;

import MorrisWaterMaze.Calculations;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.MouseParameterAccessor;
import MorrisWaterMaze.util.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;


public class MouseMovement implements Paintable
{
	private static final double
		FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
	
	public static final double
		RADIUS = 3;							// Radius des die Maus repräsentierenden Kreises
	
	
	private Point coordinates;				// aktueller Aufenthaltsort der Maus
	private final double speed;                    // Geschwindigkeit der Maus; default: 5
	private double polarAngle;                    // bestimmt in welche Richtung die Maus schwimmt
	private final double maximumSwimmingDuration;		// maximale Schwimmzeit; default: 0 (no restriction)
	private final boolean
		isStartingPositionLeft;	// gibt an, ob die Maus am linken oder rechten Rand des Pools erscheint; default: true
	private double trainingLevel;                    // Trainingslevel der Maus; [0, 1]; default: 0.5
	public double stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	private boolean isSwimming;					// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde

	private final boolean
		isBodyToBeDrawn;
	
	private final ArrayList<Double>
		timeSteps = new ArrayList<>();
		
	private double
		maximumDurationOfNextSimulationStep;
	private double
		durationOfNextSimulationStep;
	
	private final EscapeRoute
		escapeRoute = new EscapeRoute();
	
	public MouseMovement(MouseParameterAccessor parameterAccessor)
	{
		coordinates = Point.newInstance(0,0); // TODO gleich die richtigen Werte setzen
		maximumSwimmingDuration = parameterAccessor.getMaximumMouseSwimmingTime();
		trainingLevel = parameterAccessor.getMouseTrainingLevel();
		stepLengthBias = parameterAccessor.getStepLengthBias();
		isStartingPositionLeft = parameterAccessor.getStartingPosition() == StartingSide.LEFT;
		speed = parameterAccessor.mouseSpeed();
		isBodyToBeDrawn = parameterAccessor.isStartingWithGui();
	}
		
	public void determineStartingPosition(Pool pool)// Startposition von der Maus
	{
		if(isStartingPositionLeft)
		{
			coordinates = Point.newInstance(pool.border.getCenterX()- Pool.RADIUS + RADIUS, pool.border.getCenterY());
		}
		else
		{
			coordinates = Point.newInstance(pool.border.getCenterX() + Pool.RADIUS - RADIUS, pool.border.getCenterY());
		}
		polarAngle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(coordinates, pool.center));
		isSwimming = true;
		escapeRoute.resetTo(coordinates);
		timeSteps.clear();
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
		
		Point newCoordinates = calculateNewCoordinates(pool, platform);
		
		double timeOfLastMove = coordinates.distance(newCoordinates)/speed;
		double totalSimulationTime = timeOfLastMove + getTotalDurationOfCurrentSimulation();
				
		timeSteps.add(totalSimulationTime);
		escapeRoute.addNextSectionTo(newCoordinates);
		coordinates = newCoordinates;
	}
	
	private Point calculateNewCoordinates(Pool pool, Platform platform)
	{
		double stepLength = speed * durationOfNextSimulationStep;
		Point newCoordinates = Point.newInstance(
									coordinates.getX() + stepLength * Math.cos(polarAngle),
									coordinates.getY() + stepLength * Math.sin(polarAngle));
		
		if(pool.collisionSize.contains(newCoordinates.asPoint2D()))
		{
			double meanAngle;
			Point movementVector = Calculations.calculateVector(coordinates, newCoordinates);
			Point newPosMousePlatformVector = Calculations.calculateVector(newCoordinates, platform.getCenter());
			
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
			Point newPosCenterVector = Calculations.calculateVector(newCoordinates, pool.center);
			double direction = Line2D.relativeCCW(pool.center.getX(), pool.center.getY(), newCoordinates.getX(), newCoordinates.getY(), coordinates.getX(), coordinates.getY());
			polarAngle = Calculations.calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + Calculations.gaussian(0, Math.PI/12, Math.PI/6));
			isSwimming = true;
		}
		Line2D lastMove = new Line2D.Double(coordinates.asPoint2D(), newCoordinates.asPoint2D());
		if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
		{
			newCoordinates = Point.of(Objects.requireNonNull(Calculations.clipLine(lastMove, platform.getBounds())).getP1());
			isSwimming = false;
		}
		return newCoordinates;
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
	
	public void forEachEscapeRouteSection(Consumer<? super EscapeRouteSection> action)
	{
		escapeRoute.forEachSection(action);
	}
	
	public Point getCurrentPosition()
	{
		return coordinates;
	}
}
