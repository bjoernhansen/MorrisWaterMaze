package morris_water_maze.model.mouse;

import morris_water_maze.model.StartingSide;
import morris_water_maze.util.calculations.number_generation.GaussianDistribution;
import morris_water_maze.util.calculations.number_generation.RandomDistribution;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;

import static morris_water_maze.model.mouse.Calculations.degreesToRadians;


public final class Mouse
{
    public static final double RADIUS = 3;                    // Radius des die Maus repräsentierenden Kreises
    
    private final double speed;                        // Geschwindigkeit der Maus; default: 5
    
    private final Square platformBounds;
    
    private final Circle movementBoundaries;    // Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
    
    private final Point startingCoordinates;  // Startposition von der Maus
    
    private final MovementDirection movementDirection;
    
    // variable attributes
    private Point coordinates;            // aktueller Aufenthaltsort der Maus
    
    private boolean isSwimming;                // = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
    
    public Mouse(MouseParameter mouseParameter, Circle movementBoundaries, Square platformBounds)
    {
        this.movementBoundaries = movementBoundaries;
        coordinates = this.startingCoordinates = getStartPosition(mouseParameter.getStartingSide(), movementBoundaries);
        speed = mouseParameter.getMouseSpeed();
        this.platformBounds = platformBounds;
        
        RandomDistribution reboundAngleDistribution = getReboundAngleDistribution(mouseParameter);
        RandomDistribution movementDirectionDistribution = getMovementDirectionDistribution(mouseParameter);
        
        this.movementDirection = new MovementDirection(
                                        mouseParameter,
                                        movementBoundaries,
                                        startingCoordinates,
                                        platformBounds.getCenter(),
                                        movementDirectionDistribution,
                                        reboundAngleDistribution);
    }
    
    private RandomDistribution getMovementDirectionDistribution(MouseParameter mouseParameter)
    {
        double untrainedAngleDistributionSigma = degreesToRadians(mouseParameter.getUntrainedAngleDistributionSigma());
        double sigmaForGaussianDistributedAngles = calculateSigmaForGaussianDistributedAngles(mouseParameter.getMouseTrainingLevel(), untrainedAngleDistributionSigma);
        GaussianDistribution gaussianDistribution = new GaussianDistribution(sigmaForGaussianDistributedAngles);
        return gaussianDistribution;//.getCorrespondingVonMisesDistribution();
    }
    
    private double calculateSigmaForGaussianDistributedAngles(double trainingLevel, double untrainedAngleDistributionSigma)
    {
        // for a more trained mouse the standard deviation is smaller
        return (1 - trainingLevel) * untrainedAngleDistributionSigma;
    }
    
    private static RandomDistribution getReboundAngleDistribution(MouseParameter mouseParameter)
    {
        double meanPoolBorderReboundAngle = degreesToRadians(mouseParameter.getMeanPoolBorderReboundAngle());
        double reboundAngleDistributionSigma = degreesToRadians(mouseParameter.getReboundAngleDistributionSigma());
        GaussianDistribution gaussianDistribution = new GaussianDistribution(meanPoolBorderReboundAngle, reboundAngleDistributionSigma);
        return gaussianDistribution;//.getCorrespondingVonMisesDistribution();
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        Point newCoordinates = calculateNewCoordinatesAfter(durationOfCurrentSimulationStep);
        LineSegment currentMove = LineSegment.from(coordinates)
                                             .to(newCoordinates);
        coordinates = newCoordinates;
        movementDirection.recalculateForMoveAfter(currentMove);
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
            return Point.newInstance(movementBoundaries.getX(), movementBoundaries.getCenter()
                                                                                  .getY());
        }
        return Point.newInstance(movementBoundaries.getMaxX(), movementBoundaries.getCenter()
                                                                                 .getY());
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
}
