package morris_water_maze.model.mouse;

import morris_water_maze.model.Pool;
import morris_water_maze.model.StartingSide;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;


public final class Mouse
// TODO gegebenenfalls weiter "pure functions" auslagern in Klasse Utility-Calculations
{
    private static final double
        RADIUS = 3;					// Radius des die Maus repräsentierenden Kreises
    
    private final double
        speed;     					// Geschwindigkeit der Maus; default: 5
    
    private final Square
        platformBounds;
    
    private final Circle
        movementBoundaries;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
        
    private final Point
        startingCoordinates;  // Startposition von der Maus
    
    // variable attributes
    private Point
        coordinates;		    // aktueller Aufenthaltsort der Maus
    
    private final MovementDirection
        movementDirection;
    
    private boolean
        isSwimming;				// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
        
    public Mouse(MouseParameter mouseParameter, Point poolCenter, Square platformBounds)
    {
        Circle movementBoundaries = this.movementBoundaries = calculateMovementBoundariesThrough(poolCenter);
        coordinates = startingCoordinates = getStartPosition(mouseParameter.getStartingSide(), movementBoundaries);
        
        speed = mouseParameter.mouseSpeed();
        
        this.platformBounds = platformBounds;
        
        movementDirection = new MovementDirection(mouseParameter.getMouseTrainingLevel(), movementBoundaries, startingCoordinates, poolCenter, platformBounds.getCenter());
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        Point newCoordinates = calculateNewCoordinatesAfter(durationOfCurrentSimulationStep);
        
        LineSegment nextMove = LineSegment.from(coordinates)
                                          .to(newCoordinates);
    
        coordinates = newCoordinates;
        
        movementDirection.recalculateAngleFor(nextMove);
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
    
    private boolean isProposedMoveTakingMouseOutsidePoolBoundary(LineSegment currentMove)
    {
        return !movementBoundaries.contains(currentMove.getEnd());
    }
    
    private Point calculateLandingPlaceOnPlatformFor(LineSegment currentMove)
    {
        return currentMove.getEntryPointInto(platformBounds);
    }
    
    private boolean isReachingPlatformWithin(LineSegment currentMove)
    {
        return currentMove.intersects(platformBounds);
    }
    
    private Point calculateUnconstrainedCoordinatesAfter(double durationOfNextSimulationStep)
    {
        double stepLength = speed * durationOfNextSimulationStep;
        return coordinates.translate(stepLength, movementDirection.getAngle());
    }
    
    private Point getStartPosition(StartingSide startingSide, Circle movementBoundaries)
    {
        if(startingSide == StartingSide.LEFT)
        {
            return Point.newInstance(movementBoundaries.getX(), movementBoundaries.getCenter().getY());
        }
        return Point.newInstance(movementBoundaries.getMaxX(), movementBoundaries.getCenter().getY());
    }
    
    private Circle calculateMovementBoundariesThrough(Point poolCenter)
    {
        double radius = Pool.RADIUS - RADIUS;
        return Circle.newInstance(poolCenter, radius);
    }
    
    public Point getCoordinates()
    {
        return coordinates;
    }
    
    public void reset()
    {
        coordinates = startingCoordinates;
        movementDirection.resetRandomly();
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
        movementDirection.setTrainingLevel(trainingLevel);
    }
}
