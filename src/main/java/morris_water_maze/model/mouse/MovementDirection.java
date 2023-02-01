package morris_water_maze.model.mouse;

import morris_water_maze.model.simulation.MouseTrainingLevelModifier;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.RotationDirection;

import static morris_water_maze.model.mouse.Calculations.angle;
import static morris_water_maze.model.mouse.Calculations.calculatePolarAngle;
import static morris_water_maze.model.mouse.Calculations.degreesToRadians;
import static morris_water_maze.model.mouse.Calculations.gaussian;
import static morris_water_maze.model.mouse.Calculations.random;


public final class MovementDirection implements MouseTrainingLevelModifier
{
    //TODO make parameter
    private static final double
        FIELD_OF_VIEW = degreesToRadians(90);	// Sichtfenster der Maus; default: 90°
    
    //TODO make parameter
    private static final double
        UNTRAINED_ANGLE_DISTRIBUTION_SIGMA = degreesToRadians(22.5);
    
    //TODO make parameter
    private static final double
        MEAN_POOL_BORDER_REBOUND_ANGLE = degreesToRadians(60.0);
    
    //TODO make parameter
    private static final double
        REBOUND_ANGLE_DISTRIBUTION_SIGMA = degreesToRadians(15.0);
    
    private static final double
        STARTING_DIRECTION_ANGLE_RANGE = degreesToRadians(90.0);
    
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
    
    private double
        trainingLevel;		    // Trainingslevel der Maus; [0, 1]; default: 0.5
    
    
    public MovementDirection(double trainingLevel,
                             Circle movementBoundaries,
                             Point mouseStartingCoordinates,
                             Point platformCenter)
    {
        this.trainingLevel = trainingLevel;
        this.movementBoundaries = movementBoundaries;
        this.poolCenter = movementBoundaries.getCenter();
        this.platformCenter = platformCenter;
        Point startToPoolCenterVector = VectorBuilder.from(mouseStartingCoordinates)
                                                     .to(poolCenter);
        polarAngleOfStartToPoolCenterVector = calculatePolarAngle(startToPoolCenterVector);
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
        double angularDeviation = STARTING_DIRECTION_ANGLE_RANGE * (random()-0.5);
        angle = polarAngleOfStartToPoolCenterVector + angularDeviation;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    @Override
    public void setMouseTrainingLevel(double trainingLevel)
    {
        this.trainingLevel = trainingLevel;
    }
    
    private double getMovementDirectionAngleWhenTouchingPoolBorder(LineSegment currentMove)
    {
        Point newCoordinates = currentMove.getEnd();
        Point currentPositionToPoolCenterVector = VectorBuilder.from(newCoordinates).to(poolCenter);
        
        RotationDirection rotationDirection = getRotationDirectionAroundPoolCenterWhenSwimmingAlong(currentMove);
    
        double gaussian = gaussian(MEAN_POOL_BORDER_REBOUND_ANGLE, REBOUND_ANGLE_DISTRIBUTION_SIGMA);
        return calculatePolarAngle(currentPositionToPoolCenterVector) - rotationDirection.getValue() * gaussian;
    }
    
    private double getMovementDirectionWhenAwayFromPoolBorder(LineSegment currentMove)
    {
        double sigma = calculateSigmaForGaussianDistributedAngles();
        double meanAngle = calculateMeanAngleFor(currentMove);
        return gaussian(meanAngle, sigma);
    }
    
    private double calculateSigmaForGaussianDistributedAngles()
    {
        // for a more trained mouse the standard deviation is smaller
        return (1 - trainingLevel) * UNTRAINED_ANGLE_DISTRIBUTION_SIGMA;
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
        return trainingLevel > random() && FIELD_OF_VIEW >= 2 * angle(movementVector, mouseToPlatformVector);
    }
}
