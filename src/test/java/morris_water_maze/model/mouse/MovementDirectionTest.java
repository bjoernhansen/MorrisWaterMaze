package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static morris_water_maze.model.mouse.Calculations.calculatePolarAngle;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    
    
    
    @Test
    void shouldResetCorrectly()
    {
        // TODO test fertig stellen
        RandomNumbers randomNumberGenerator = mock(RandomNumbers.class);
        when(randomNumberGenerator.nextDouble()).thenReturn(0.5, 0.0);
        Calculations.setRandom(randomNumberGenerator);
    
        MouseParameter mouseParameter= new MouseParameterForTest();
        
        MovementDirection movementDirection = new MovementDirection(
                                                    mouseParameter,
                                                    movementBoundaries,
                                                    startingCoordinates,
                                                    platformCenter);
    
        movementDirection.resetRandomly();
        
        
        
            /*
              public MovementDirection(double trainingLevel,
                             Circle movementBoundaries,
                             Point mouseStartingCoordinates,
                             Point platformCenter)
             */
            
    }
    
    @Test
    void resetRandomly()
    {
    }
    
    @Test
    void getAngle()
    {
    }
    
    @Test
    void setMouseTrainingLevel()
    {
    }
}