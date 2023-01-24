package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.RotationDirection;

import java.util.Random;

import static morris_water_maze.model.mouse.Calculations.angle;


final class MovementDirection
// TODO Klasse verbessern
{
    private static final double
        FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
    
    public static final double
        UNTRAINED_ANGLE_DISTRIBUTION_SIGMA = Math.PI/8;
    
    
    private final Point
        poolCenter;
    
    private final Point
        platformCenter;
    
    private final Point
        mouseStartingCoordinates;
    
    private final Random
        random = new Random();
    
    private final Circle
        movementBoundaries;
    
    
    private double
        angle;    // Winkel im Polarkoordinatensystem, der bestimmt, in welche Richtung die Maus schwimmt
    
    private double
        trainingLevel;		    // Trainingslevel der Maus; [0, 1]; default: 0.5
    
    
    public MovementDirection(double trainingLevel, Circle movementBoundaries, Point mouseStartingCoordinates, Point poolCenter, Point platformCenter)
    {
        this.poolCenter = poolCenter;
        this.trainingLevel = trainingLevel;
        this.platformCenter = platformCenter;
        this.mouseStartingCoordinates = mouseStartingCoordinates;
        this.movementBoundaries = movementBoundaries;
    }
    
    public void recalculateAngleFor(LineSegment nextMove)
    {
        Point newCoordinates = nextMove.getEnd();
        if(newCoordinates.isOnTheEdgeOf(movementBoundaries))
        {
            angle = getMovementDirectionAngleWhenTouchingPoolBorder(nextMove);
        }
        else
        {
            double sigma = calculateSigmaForGaussianDistributedAngles();
            double meanAngle = calculateMeanAngleFor(nextMove);
            angle = gaussian(meanAngle, sigma);
        }
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    public void resetRandomly()
    {
        angle = Math.PI*(Math.random()-0.5) + calculatePolarAngle(VectorBuilder.from(mouseStartingCoordinates).to(poolCenter));
    }
    
    private double calculateSigmaForGaussianDistributedAngles()
    {
        // for a more trained mouse the standard deviation is smaller
        return (1 - trainingLevel) * UNTRAINED_ANGLE_DISTRIBUTION_SIGMA;
    }
    
    private double gaussian(double mean, double sigma)
    {
        return sigma * random.nextGaussian() + mean;
    }
    
    private double getMovementDirectionAngleWhenTouchingPoolBorder(LineSegment nextMove)
    // TODO Methode verbessern: Was genau wird hier berechnet - API anpassen
    {
        Point newCoordinates = nextMove.getEnd();
        Point movementDirectionVector = VectorBuilder.from(newCoordinates)
                                                     .to(poolCenter);
        
        RotationDirection rotationDirection = getRotationDirectionAroundPoolCenterWhenSwimmingAlong(nextMove);
        
        return calculatePolarAngle(movementDirectionVector)
            - rotationDirection.getValue() * (Math.PI / 3 + gaussian(0, Math.PI / 12.0, Math.PI / 6.0));
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
        if(trainingLevel > Math.random() && FIELD_OF_VIEW /2 >= angle(movementVector, mouseToPlatformVector))
        {
            return calculatePolarAngle(mouseToPlatformVector);
        }
        return calculatePolarAngle(movementVector);
    }
    
    private double gaussian(double mean, double sigma, double max)
    {
        // TODO schlechte und unklare Implementierung (rekursiver Aufruf bis Random-Ausgabe stimmig?)
        double gaussian = sigma * random.nextGaussian();
        if(Math.abs(gaussian) < max)
        {
            return gaussian + mean;
        }
        return gaussian(mean, sigma, max);
    }
    
    private double calculatePolarAngle(Point vector)
    {
        return Math.atan2(vector.getY(), vector.getX());
    }
    
    public void setTrainingLevel(double trainingLevel)
    {
        this.trainingLevel = trainingLevel;
    }
}
