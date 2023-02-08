package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import static morris_water_maze.model.mouse.Calculations.angle;
import static morris_water_maze.model.mouse.Calculations.calculatePolarAngle;
import static morris_water_maze.model.mouse.Calculations.degreesToRadians;
import static morris_water_maze.model.mouse.Calculations.dotProduct;
import static morris_water_maze.model.mouse.Calculations.gaussian;
import static morris_water_maze.model.mouse.Calculations.square;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_1;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_2;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_3_4;
import static morris_water_maze.util.DoubleComparisonAssert.assertThatDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CalculationsTest
{
    private final Point
        unitVector1 = Point.newInstance(1,0);
    
    private final Point
        unitVector2 = Point.newInstance(0,1);
    
    
    @Test
    void shouldCalculateVectorLengthCorrectly()
    {
        assertThatDouble(Calculations.length(unitVector1)).isCloseTo(1.0);
        assertThatDouble(Calculations.length(unitVector2)).isCloseTo(1.0);
        assertThatDouble(Calculations.length(VECTOR_3_4)).isCloseTo(5.0);
    }
    
    @Test
    void shouldCalculateDotProductLengthCorrectly()
    {
        assertThatDouble(dotProduct(unitVector1, unitVector2)).isCloseTo(0.0);
        assertThatDouble(dotProduct(unitVector1, VECTOR_1_2)).isCloseTo(VECTOR_1_2.getX());
        assertThatDouble(dotProduct(unitVector2, VECTOR_1_2)).isCloseTo(VECTOR_1_2.getY());
        assertThatDouble(dotProduct(VECTOR_1_2, VECTOR_3_4)).isCloseTo(11.0);
    }
    
    @Test
    void shouldCalculateAnglesCorrectly()
    {
        assertThatDouble(angle(unitVector1, unitVector2)).isCloseTo(Math.PI/2.0);
        assertThatDouble(angle(VECTOR_1_2, VECTOR_1_2)).isCloseTo(0.0);
        assertThatDouble(angle(VECTOR_3_4, negate(VECTOR_3_4))).isCloseTo(Math.PI);
        assertThatDouble(angle(unitVector1, VECTOR_1_1)).isCloseTo(Math.PI/4);
    }
    
    private static Point negate(Point point)
    {
        return Point.newInstance(-point.getX(), -point.getY());
    }
    
    @Test
    void shouldSquareValuesCorrectly()
    {
        assertThatDouble(square(Math.PI)).isCloseTo(Math.PI*Math.PI);
        assertThatDouble(square(VECTOR_3_4)).isCloseTo( 25.0);
    }
    
    @Test
    void shouldCalculateDegreesToRadiansConversionCorrectly()
    {
        assertThatDouble(degreesToRadians(90)).isCloseTo(Math.PI/2.0);
    }
    
    @Test
    void shouldCalculatePolarAngleCorrectly()
    {
        assertThatDouble(calculatePolarAngle(unitVector1)).isCloseTo(degreesToRadians(0.0));
        assertThatDouble(degreesToRadians(90)).isCloseTo(degreesToRadians(90.0));
        assertThatDouble(calculatePolarAngle(VECTOR_1_1)).isCloseTo(degreesToRadians(45.0));
    }
    
    @Test
    void shouldGaussianCorrectly()
    {
        RandomNumbers randomNumberGenerator = mock(RandomNumbers.class);
        when(randomNumberGenerator.nextGaussian()).thenReturn(0.0, 1.0);
        Calculations.setRandom(randomNumberGenerator);
        
        double mean = 30.0;
        double sigma = 10.0;
    
        assertThatDouble(gaussian(mean, sigma)).isCloseTo(mean);
        assertThatDouble(gaussian(mean, sigma)).isCloseTo(mean + sigma);
    }
}