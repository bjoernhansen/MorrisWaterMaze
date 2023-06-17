package morris_water_maze.model.mouse;

import morris_water_maze.util.calculations.number_generation.RandomDistribution;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.RotationDirection;

import static morris_water_maze.model.mouse.Calculations.angle;
import static morris_water_maze.model.mouse.Calculations.calculatePolarAngle;
import static morris_water_maze.model.mouse.Calculations.degreesToRadians;
import static morris_water_maze.model.mouse.Calculations.random;


public final class MovementDirection
{
    private final Point
        poolCenter;
    
    private final Point
        platformCenter;
    
    private final Circle
        movementBoundaries;
    
    private double
        angle;    // Winkel im Polarkoordinatensystem, der bestimmt, in welche Richtung die Maus schwimmt
    
    private final double
        polarAngleOfStartToPoolCenterVector;
    
    private final double
        trainingLevel;            // Trainingslevel der Maus; [0, 1]; default: 0.5
    
    private final double
        fieldOfView;
    
    private final double
        startingDirectionAngleRange;
    
    private final RandomDistribution
        reboundAngleDistribution;
    
    private final RandomDistribution
        movementDirectionDistribution;
    
    
    public MovementDirection(MovementDirectionParameter movementDirectionParameter,
                             Circle movementBoundaries,
                             Point mouseStartingCoordinates,
                             Point platformCenter,
                             RandomDistribution movementDirectionDistribution,
                             RandomDistribution reboundAngleDistribution)
    {
        this.movementBoundaries = movementBoundaries;
        this.poolCenter = movementBoundaries.getCenter();
        this.platformCenter = platformCenter;
        this.trainingLevel = movementDirectionParameter.getMouseTrainingLevel();
        // TODO die erstellung sollte nicht in Maus sondern deutlich weiter oben erfolgen,
        //  ggf. könnte es eigene Klasse erstellt werden, die beide VErteilungen liefert und ggf. über die MausMovemenbtParameter abrufbar ist
        this.movementDirectionDistribution = movementDirectionDistribution;
        this.reboundAngleDistribution = reboundAngleDistribution;
        
        Point startToPoolCenterVector = VectorBuilder.from(mouseStartingCoordinates)
                                                     .to(poolCenter);
        polarAngleOfStartToPoolCenterVector = calculatePolarAngle(startToPoolCenterVector);
        fieldOfView = degreesToRadians(movementDirectionParameter.getFieldOfView());
        startingDirectionAngleRange = degreesToRadians(movementDirectionParameter.getStartingDirectionAngleRange());
    }
    
    public void recalculateForMoveAfter(LineSegment currentMove)
    {
        Point newCoordinates = currentMove.getEnd();
        if(newCoordinates.isOnTheEdgeOf(movementBoundaries))
        {
            angle = getMovementDirectionAngleWhenTouchingPoolBorder(currentMove);
        }
        else
        {
            angle = getMovementDirectionWhenAwayFromPoolBorder(currentMove);
        }
    }
    
    public void resetRandomly()
    {
        double angularDeviation = startingDirectionAngleRange * (random() - 0.5);
        angle = polarAngleOfStartToPoolCenterVector + angularDeviation;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    private double getMovementDirectionAngleWhenTouchingPoolBorder(LineSegment currentMove)
    {
        Point newCoordinates = currentMove.getEnd();
        Point currentPositionToPoolCenterVector = VectorBuilder.from(newCoordinates)
                                                               .to(poolCenter);
        
        RotationDirection rotationDirection = getRotationDirectionAroundPoolCenterWhenSwimmingAlong(currentMove);
        
        double gaussian = reboundAngleDistribution.nextDouble();
        return calculatePolarAngle(currentPositionToPoolCenterVector) - gaussian * rotationDirection.getValue();
    }
    
    private double getMovementDirectionWhenAwayFromPoolBorder(LineSegment currentMove)
    {
        double meanAngle = calculateMeanAngleFor(currentMove);
        return movementDirectionDistribution.nextDouble(meanAngle);
    }
    
    private RotationDirection getRotationDirectionAroundPoolCenterWhenSwimmingAlong(LineSegment nextMove)
    {
        return nextMove.getRotationDirectionWithRespectTo(poolCenter);
    }
    
    private double calculateMeanAngleFor(LineSegment nextMove)
    {
        Point newCoordinates = nextMove.getEnd();
        Point mouseToPlatformVector = VectorBuilder.from(newCoordinates)
                                                   .to(platformCenter);
        Point movementVector = VectorBuilder.of(nextMove);
        if(isMouseDetectingPlatformGiven(mouseToPlatformVector, movementVector))
        {
            return calculatePolarAngle(mouseToPlatformVector);
        }
        return calculatePolarAngle(movementVector);
    }
    
    private boolean isMouseDetectingPlatformGiven(Point mouseToPlatformVector, Point movementVector)
    {
        return trainingLevel > random() && fieldOfView >= 2 * angle(movementVector, mouseToPlatformVector);
    }
}
