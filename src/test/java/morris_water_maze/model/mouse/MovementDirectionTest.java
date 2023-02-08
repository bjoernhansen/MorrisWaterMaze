package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static morris_water_maze.model.mouse.Calculations.calculatePolarAngle;
import static morris_water_maze.model.mouse.Calculations.degreesToRadians;
import static morris_water_maze.util.DoubleComparisonAssert.assertThatDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Movement direction")
class MovementDirectionTest
{
    private final Point
        poolCenter = Point.newInstance(100.0, 100.0);
    
    private final Point
        startingCoordinates = Point.newInstance(0.0, 100.0);
    
    private final Point
        platformCenter = Point.newInstance(200.0, 100.0);
    
    private final Circle
        movementBoundaries = Circle.newInstance(poolCenter, 100.0);
    
    private final Point
        startToPoolCenterVector = VectorBuilder.from(startingCoordinates)
                                               .to(poolCenter);
    private final double
        polarAngleOfStartToPoolCenterVector = calculatePolarAngle(startToPoolCenterVector);
        
    private final MovementDirectionParameter
        mouseParameter = new MouseParameterForTest();
    
    private final MovementDirection
        movementDirection = new MovementDirection(
                                    mouseParameter,
                                    0.5,
                                    movementBoundaries,
                                    startingCoordinates,
                                    platformCenter);
    
    @Test
    @DisplayName("is resettet correctly")
    void shouldResetCorrectly()
    {
        RandomNumbers randomNumberGenerator = mock(RandomNumbers.class);
        when(randomNumberGenerator.nextDouble()).thenReturn(0.5, 0.0);
        Calculations.setRandom(randomNumberGenerator);
        
        movementDirection.resetRandomly();
        
        assertThatDouble(movementDirection.getAngle()).isCloseTo(polarAngleOfStartToPoolCenterVector);
        
        movementDirection.resetRandomly();
        double maximumCounterClockwiseTurn = -0.5 * degreesToRadians(mouseParameter.getStartingDirectionAngleRange());
        double smallestStartingDirectionAngle = polarAngleOfStartToPoolCenterVector + maximumCounterClockwiseTurn;
    
        assertThatDouble(movementDirection.getAngle()).isCloseTo(smallestStartingDirectionAngle);
    }
    
    @Test
    @DisplayName("is recalculated correctly")
    void shouldBeRecalculatedCorrectly()
    {
        RandomNumbers randomNumberGenerator = mock(RandomNumbers.class);
        when(randomNumberGenerator.nextGaussian()).thenReturn(0.0);
        Calculations.setRandom(randomNumberGenerator);
    
        LineSegment firstMove = LineSegment.from(startingCoordinates).to(poolCenter);
        Point firstMoveVector = VectorBuilder.of(firstMove);
        double polarAngleOfFirstMove = calculatePolarAngle(firstMoveVector);
        
        movementDirection.recalculateForMoveAfter(firstMove);
    
        assertThatDouble(polarAngleOfFirstMove).isCloseTo(movementDirection.getAngle());
    
        
        LineSegment secondMove = LineSegment.from(poolCenter).to(startingCoordinates);
        Point secondMoveVector = VectorBuilder.of(secondMove);
        double polarAngleOfMoveAfterTouchingPoolBorder = (calculatePolarAngle(secondMoveVector) + Calculations.degreesToRadians(240.0))%(2*Math.PI);
        
        movementDirection.recalculateForMoveAfter(secondMove);
    
        assertThat(startingCoordinates.isOnTheEdgeOf(movementBoundaries)).isTrue();
        assertThatDouble(polarAngleOfMoveAfterTouchingPoolBorder).isCloseTo(movementDirection.getAngle());
    }
}
