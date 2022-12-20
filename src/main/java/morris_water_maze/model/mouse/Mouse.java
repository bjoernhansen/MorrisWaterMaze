package morris_water_maze.model.mouse;

import morris_water_maze.model.Platform;
import morris_water_maze.model.Pool;
import morris_water_maze.model.StartingSide;
import morris_water_maze.parameter.MouseParameterAccessor;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.LineSegmentBuilder;
import morris_water_maze.util.geometry.Point;

import java.util.Objects;
import java.util.Random;

import static morris_water_maze.model.mouse.Calculations.angle;


public final class Mouse
{
    private static final double
        RADIUS = 3;					// Radius des die Maus repräsentierenden Kreises
    
    private static final double
        FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
        
    private double
        trainingLevel;				// Trainingslevel der Maus; [0, 1]; default: 0.5
    
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
        
    private Point
        coordinates;		// aktueller Aufenthaltsort der Maus
    
    private double
        polarAngle;					// bestimmt in welche Richtung die Maus schwimmt
    
    private boolean
        isSwimming;				// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
    private final Random
        random = new Random();
        
    
    public Mouse(MouseParameterAccessor parameterAccessor, Pool pool, Platform platform)
    {
        Circle movementBoundaries = this.movementBoundaries = calculateMovementBoundariesThrough(pool);
        coordinates = startingCoordinates = getStartPosition(parameterAccessor.getStartingSide(), movementBoundaries);
        
        trainingLevel = parameterAccessor.getMouseTrainingLevel();
        speed = parameterAccessor.mouseSpeed();
        
        this.platform = platform;
        this.pool = pool;
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        Point coordinates1 = calculateNewCoordinates(durationOfCurrentSimulationStep);
        Objects.requireNonNull(coordinates1);
        coordinates = coordinates1;
    }
    
    // TODO Methode verbessern
    private Point calculateNewCoordinates(double durationOfNextSimulationStep)
    {
        Point unconstrainedCoordinates = calculateUnconstrainedCoordinatesAfter(durationOfNextSimulationStep);
        LineSegment currentMove = LineSegmentBuilder.from(coordinates)
                                                    .to(unconstrainedCoordinates);
        
        if(isReachingPlatformWithin(currentMove))
        {
            stopSwimming();
            return getLandingPlaceOnPlatformFor(currentMove);
        }
        
        if(isCurrentMoveTakingMouseOutsidePoolBoundary(currentMove)) // Schnittstelle von Pool und Mausschritt
        {
            Point constrainedCoordinates = Geometry.circleLineIntersection(
                movementBoundaries,
                coordinates,
                unconstrainedCoordinates);
            
            Point newPosCenterVector = VectorBuilder.from(constrainedCoordinates).to(pool.getCenter());
    
            double direction = LineSegment.relativeCCW(
                pool.getCenter().getX(),
                pool.getCenter().getY(),
                constrainedCoordinates.getX(),
                constrainedCoordinates.getY(),
                coordinates.getX(),
                coordinates.getY());
                
            polarAngle = calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + gaussian(0, Math.PI/12, Math.PI/6));
            
            return constrainedCoordinates;
        }
      
        polarAngle = gaussian(calculateMeanAngleFor(unconstrainedCoordinates), calculateSigma());  // for a more trained mouse the standard deviation is smaller
        return unconstrainedCoordinates;
    }
    
    private boolean isCurrentMoveTakingMouseOutsidePoolBoundary(LineSegment currentMove)
    {
        return !movementBoundaries.contains(currentMove.getEnd());
    }
    
    private Point getLandingPlaceOnPlatformFor(LineSegment currentMove)
    {
        return Geometry.clipLine(currentMove, platform.getBounds())
                       .map(LineSegment::getStart)
                       .orElse(null);
    }
    
    private boolean isReachingPlatformWithin(LineSegment currentMove)
    {
        return currentMove.intersects(platform.getBounds());
    }
    
    private double calculateMeanAngleFor(Point proposedCoordinates)
    {
        Point movementVector = VectorBuilder.from(coordinates)
                                            .to(proposedCoordinates);
        Point mouseToPlatformVector = VectorBuilder.from(proposedCoordinates).to(platform.getCenter());
        if(trainingLevel > Math.random() && Mouse.FIELD_OF_VIEW /2 >= angle(movementVector, mouseToPlatformVector))
        {
            return calculatePolarAngle(mouseToPlatformVector);
        }
        return calculatePolarAngle(movementVector);
    }
    
    private Point calculateUnconstrainedCoordinatesAfter(double durationOfNextSimulationStep)
    {
        double stepLength = speed * durationOfNextSimulationStep;
        return Point.newInstance(
                coordinates.getX() + stepLength * Math.cos(polarAngle),
                coordinates.getY() + stepLength * Math.sin(polarAngle));
    }
    
    private double gaussian(double mu, double sigma)
    {
        return sigma*random.nextGaussian()+mu;
    }
    
    private double gaussian(double mu, double sigma, double max)
    {
        // TODO schlechte und unklare Implementierung (rekursiver Aufruf bis Random-Ausgabe stimmig?)
        double gaussian = sigma*random.nextGaussian();
        if(Math.abs(gaussian) < max)return gaussian+mu;
        return gaussian(mu, sigma, max);
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
        double radius = pool.getRadius() - Mouse.RADIUS;
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
        polarAngle = Math.PI*(Math.random()-0.5) + calculatePolarAngle(VectorBuilder.from(startingCoordinates).to(pool.getCenter()));
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
