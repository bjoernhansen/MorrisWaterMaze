package MorrisWaterMaze.model;

import MorrisWaterMaze.util.Calculations;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.MouseParameterAccessor;
import MorrisWaterMaze.util.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

// TODO Implementierung verbessern
public final class MouseMovement implements Paintable
{
	private static final double
		FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
	
	public static final double
		RADIUS = 3;							// Radius des die Maus repräsentierenden Kreises
	
	
	private Point coordinates;				// aktueller Aufenthaltsort der Maus
	private final double speed;                    // Geschwindigkeit der Maus; default: 5
	private double polarAngle;                    // bestimmt in welche Richtung die Maus schwimmt
	private final double maximumSwimmingDuration;		// maximale Schwimmzeit; default: 0 (no restriction)
	
	private double trainingLevel;                    // Trainingslevel der Maus; [0, 1]; default: 0.5
	public double stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	private boolean isSwimming;					// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
	
	private final ArrayList<Double>
		timeSteps = new ArrayList<>();
		
	private double
		maximumDurationOfNextSimulationStep;
	private double
		durationOfNextSimulationStep;
	
	private final Point
		startPosition;
		
	private final EscapeRoute
		escapeRoute;
	
	private final Point
		poolCenter;
	
	public MouseMovement(MouseParameterAccessor parameterAccessor, Pool pool)
	{
		startPosition = getStartPosition(parameterAccessor, pool);
		escapeRoute = new EscapeRoute(startPosition);
		coordinates = startPosition;
		maximumSwimmingDuration = parameterAccessor.getMaximumMouseSwimmingTime();
		trainingLevel = parameterAccessor.getMouseTrainingLevel();
		stepLengthBias = parameterAccessor.getStepLengthBias();
		speed = parameterAccessor.mouseSpeed();
		poolCenter = pool.center;
	}
	
	private Point getStartPosition(MouseParameterAccessor parameterAccessor, Pool pool)
	{
		if(parameterAccessor.getStartingSide() == StartingSide.LEFT)
		{
			return Point.newInstance(pool.border.getCenterX()- Pool.RADIUS + RADIUS, pool.border.getCenterY());
		}
		return Point.newInstance(pool.border.getCenterX() + Pool.RADIUS - RADIUS, pool.border.getCenterY());
	}
	
	public void resetForNextEscapeRun()// Startposition von der Maus
	{
		escapeRoute.reset();
		resetTimeSteps();
		polarAngle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(startPosition, poolCenter));
		coordinates = startPosition;
		isSwimming = true;
	}
	
	private void resetTimeSteps()
	{
		timeSteps.clear();
		timeSteps.add(0.0);
	}
	
	public void move(Pool pool, Platform platform)
	{
		maximumDurationOfNextSimulationStep = calculateRandomSimulationStepDuration();
		durationOfNextSimulationStep = getDurationOfNextSimulationStep();
		
		if(hasReachedSwimmingTimeLimit())
		{
			isSwimming = false;
		}
		
		Point newCoordinates = calculateNewCoordinates(pool, platform);
		
		double timeOfLastMove = coordinates.distance(newCoordinates)/speed;
		double totalSimulationTime = timeOfLastMove + getTotalDurationOfCurrentSimulationRun();
				
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
			return maximumSwimmingDuration - getTotalDurationOfCurrentSimulationRun();
		}
		return maximumDurationOfNextSimulationStep;
	}
	
	private boolean hasReachedSwimmingTimeLimit()
	{
		return hasMaximumSwimmingTimeBeenSet()
			&& maximumDurationOfNextSimulationStep + getTotalDurationOfCurrentSimulationRun() > maximumSwimmingDuration;
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
	
	public double getTotalDurationOfCurrentSimulationRun()
	{
		return timeSteps.get(timeSteps.size()-1);
	}
	
	public void forEachEscapeRouteSection(Consumer<? super EscapeRouteSection> action)
	{
		escapeRoute.forEachSection(action);
	}
	
	private double calculateRandomSimulationStepDuration()
	{
		return Calculations.calculateRandomDuration(stepLengthBias);
	}
}
