package morris_water_maze.model.mouse;

import morris_water_maze.model.Platform;
import morris_water_maze.model.Pool;
import morris_water_maze.model.StartingSide;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.RotationDirection;

import java.util.Random;

import static morris_water_maze.model.mouse.Calculations.angle;


public final class Mouse
// TODO gegebenenfalls weiter "pure functions" auslagern in Klasse Utility-Calculations
{
    private static final double
        RADIUS = 3;					// Radius des die Maus repräsentierenden Kreises
    
    private static final double
        FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
    
    private final double
        speed;     					// Geschwindigkeit der Maus; default: 5
    
    private final Pool
        pool;
    
    private final Platform
        platform;
    
    private final Circle
        movementBoundaries;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
        
    private final Point
        startingCoordinates;  // Startposition von der Maus
    
    private final Random
        random = new Random();
    
    
    // variable attributes
    private Point
        coordinates;		    // aktueller Aufenthaltsort der Maus
    
    private double
        movementDirectionAngle;	// Winkel im Polarkoordinatensystem, der bestimmt, in welche Richtung die Maus schwimmt
    
    private boolean
        isSwimming;				// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
    private double
        trainingLevel;		    // Trainingslevel der Maus; [0, 1]; default: 0.5
    
        
    public Mouse(MouseParameter mouseParameter, Pool pool, Platform platform)
    {
        Circle movementBoundaries = this.movementBoundaries = calculateMovementBoundariesThrough(pool);
        coordinates = startingCoordinates = getStartPosition(mouseParameter.getStartingSide(), movementBoundaries);
        
        trainingLevel = mouseParameter.getMouseTrainingLevel();
        speed = mouseParameter.mouseSpeed();
        
        this.platform = platform;
        this.pool = pool;
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        Point newCoordinates = calculateNewCoordinatesAfter(durationOfCurrentSimulationStep);
        
        LineSegment nextMove = LineSegment.from(coordinates)
                                          .to(newCoordinates);
    
        coordinates = newCoordinates;
        movementDirectionAngle = recalculateMovementDirectionAngleFor(nextMove);
    }
    
    private double recalculateMovementDirectionAngleFor(LineSegment nextMove)
    {
        Point newCoordinates = nextMove.getEnd();
        if(newCoordinates.isOnTheEdgeOf(movementBoundaries))
        {
            return getMovementDirectionAngle(nextMove);
        }
        else
        {
            return gaussian(calculateMeanAngleFor(nextMove), calculateSigma());  // for a more trained mouse the standard deviation is smaller
        }
    }
    
    private Point calculateNewCoordinatesAfter(double durationOfNextSimulationStep)
    {
        Point unconstrainedCoordinates = calculateUnconstrainedCoordinatesAfter(durationOfNextSimulationStep);
        
        LineSegment proposedMove = LineSegment.from(coordinates)
                                              .to(unconstrainedCoordinates);
        
        if(isReachingPlatformWithin(proposedMove))
        {
            stopSwimming();
            return calculateLandingPlaceOnPlatformFor(proposedMove);
        }
        if(isProposedMoveTakingMouseOutsidePoolBoundary(proposedMove))
        {
            return calculateMostDistantReachablePositionFor(proposedMove);
        }
        return unconstrainedCoordinates;
    }
    
    private Point calculateMostDistantReachablePositionFor(LineSegment proposedMove)
    {
        return proposedMove.getExitPointOutOf(movementBoundaries);
    }
    
    private double getMovementDirectionAngle(LineSegment nextMove)
    // TODO Methode verbessern: Was genau wird hier berechnet - API anpassen
    {
        Point newCoordinates = nextMove.getEnd();
        Point movementDirectionVector = VectorBuilder.from(newCoordinates)
                                                     .to(pool.getCenter());
        
        RotationDirection rotationDirection = getRotationDirectionAroundPoolCenterWhenSwimmingAlong(nextMove);
        
        return calculatePolarAngle(movementDirectionVector)
               - rotationDirection.getValue() * (Math.PI / 3 + gaussian(0, Math.PI / 12, Math.PI / 6));
    }
    
    private RotationDirection getRotationDirectionAroundPoolCenterWhenSwimmingAlong(LineSegment nextMove)
    {
        return nextMove.getRotationDirectionWithRespectTo(pool.getCenter());
    }
    
    private boolean isProposedMoveTakingMouseOutsidePoolBoundary(LineSegment currentMove)
    {
        return !movementBoundaries.contains(currentMove.getEnd());
    }
    
    private Point calculateLandingPlaceOnPlatformFor(LineSegment currentMove)
    {
        return currentMove.getEntryPointInto(platform.getBounds());
    }
    
    private boolean isReachingPlatformWithin(LineSegment currentMove)
    {
        return currentMove.intersects(platform.getBounds());
    }
    
    private double calculateMeanAngleFor(LineSegment nextMove)
    {
        Point newCoordinates = nextMove.getEnd();
        Point mouseToPlatformVector = VectorBuilder.from(newCoordinates)
                                                   .to(platform.getCenter());
        
        Point movementVector = VectorBuilder.of(nextMove);
        if(trainingLevel > Math.random() && FIELD_OF_VIEW /2 >= angle(movementVector, mouseToPlatformVector))
        {
            return calculatePolarAngle(mouseToPlatformVector);
        }
        return calculatePolarAngle(movementVector);
    }
    
    private Point calculateUnconstrainedCoordinatesAfter(double durationOfNextSimulationStep)
    {
        double stepLength = speed * durationOfNextSimulationStep;
        return coordinates.translate(stepLength, movementDirectionAngle);
    }
    
    private double gaussian(double mu, double sigma)
    {
        return sigma*random.nextGaussian()+mu;
    }
    
    private double gaussian(double mean, double sigma, double max)
    {
        // TODO schlechte und unklare Implementierung (rekursiver Aufruf bis Random-Ausgabe stimmig?)
        double gaussian = sigma*random.nextGaussian();
        if(Math.abs(gaussian) < max)
        {
            return gaussian+mean;
        }
        return gaussian(mean, sigma, max);
    }
    
    private double calculatePolarAngle(Point vector)
    {
        return Math.atan2(vector.getY(), vector.getX());
    }
    
    private double calculateSigma()
    {
        return (1 - trainingLevel) * 22.5 * Math.PI / 180;
    }
    
    static Point getStartPosition(StartingSide startingSide, Circle movementBoundaries)
    {
        if(startingSide == StartingSide.LEFT)
        {
            return Point.newInstance(movementBoundaries.getX(), movementBoundaries.getCenter().getY());
        }
        return Point.newInstance(movementBoundaries.getMaxX(), movementBoundaries.getCenter().getY());
    }
    
    static Circle calculateMovementBoundariesThrough(Pool pool)
    {
        double radius = Pool.RADIUS - RADIUS;
        return Circle.newInstance(pool.getCenter(), radius);
    }
    
    public Point getCoordinates()
    {
        return coordinates;
    }
    
    public void reset()
    {
        coordinates = startingCoordinates;
        resetPolarAngleRandomly();
    }
    
    private void resetPolarAngleRandomly()
    {
        movementDirectionAngle = Math.PI*(Math.random()-0.5) + calculatePolarAngle(VectorBuilder.from(startingCoordinates).to(pool.getCenter()));
    }
    
    public boolean isSwimming()
    {
        return isSwimming;
    }
    
    public void stopSwimming()
    {
        isSwimming = false;
    }
    
    public void startSwimming()
    {
        isSwimming = true;
    }
    
    public void setTrainingLevel(double trainingLevel)
    {
        this.trainingLevel = trainingLevel;
    }
}
