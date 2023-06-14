package morris_water_maze.model.mouse;

import morris_water_maze.util.calculations.number_generation_1.RandomDistribution;
import morris_water_maze.util.calculations.number_generation_2.RandomNumberGenerator;
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
        mouseMovementParameterForTest = new MouseMovementParameterForTest();
    
    
    
    @Test
    @DisplayName("is resettet correctly")
    void shouldResetCorrectly()
    {
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        when(randomNumberGenerator.nextDouble()).thenReturn(0.5, 0.0);
        Calculations.setEvenlyDistributedNumberGenerator(randomNumberGenerator);
    
        RandomDistribution randomDistribution = mock(RandomDistribution.class);
        MovementDirection movementDirection = new MovementDirection(
                                                    mouseMovementParameterForTest,
                                                    movementBoundaries,
                                                    startingCoordinates,
                                                    platformCenter,
                                                    randomDistribution,
                                                    randomDistribution);
        movementDirection.resetRandomly();
        
        assertThatDouble(movementDirection.getAngle()).isCloseTo(polarAngleOfStartToPoolCenterVector);
        
        
        movementDirection.resetRandomly();
        double maximumCounterClockwiseTurn = -0.5 * degreesToRadians(mouseMovementParameterForTest.getStartingDirectionAngleRange());
        double smallestStartingDirectionAngle = polarAngleOfStartToPoolCenterVector + maximumCounterClockwiseTurn;
    
        assertThatDouble(movementDirection.getAngle()).isCloseTo(smallestStartingDirectionAngle);
    }
    
    @Test
    @DisplayName("is recalculated correctly")
    void shouldBeRecalculatedCorrectly()
    {
        RandomDistribution randomDistribution = mock(RandomDistribution.class);
        when(randomDistribution.nextDouble()).thenReturn(0.0);
        
        MovementDirection movementDirection = new MovementDirection(
            mouseMovementParameterForTest,
            movementBoundaries,
            startingCoordinates,
            platformCenter,
            randomDistribution,
            randomDistribution);
    
        LineSegment firstMove = LineSegment.from(startingCoordinates).to(poolCenter);
        Point firstMoveVector = VectorBuilder.of(firstMove);
        double polarAngleOfFirstMove = calculatePolarAngle(firstMoveVector);
        System.out.println("polarAngleOfMoveAfterTouchingPoolBorder: ");
        System.out.println(polarAngleOfFirstMove);
        movementDirection.recalculateForMoveAfter(firstMove);
    
        assertThat(poolCenter.isOnTheEdgeOf(movementBoundaries)).isFalse();
        assertThatDouble(polarAngleOfFirstMove).isCloseTo(movementDirection.getAngle());
        
        
        LineSegment secondMove = LineSegment.from(poolCenter).to(startingCoordinates);
        Point secondMoveVector = VectorBuilder.of(secondMove);
        double polarAngleOfMoveAfterTouchingPoolBorder = (calculatePolarAngle(secondMoveVector) + Calculations.degreesToRadians(240.0))%(2*Math.PI);
    
        System.out.println("polarAngleOfMoveAfterTouchingPoolBorder: ");
        System.out.println(polarAngleOfMoveAfterTouchingPoolBorder);
    
        movementDirection.recalculateForMoveAfter(secondMove);
    
        assertThat(startingCoordinates.isOnTheEdgeOf(movementBoundaries)).isTrue();
        assertThatDouble(polarAngleOfMoveAfterTouchingPoolBorder).isCloseTo(movementDirection.getAngle());
    }
}
