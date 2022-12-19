package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import static morris_water_maze.model.mouse.Calculations.angle;
import static morris_water_maze.model.mouse.Calculations.dotProduct;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_1;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_2;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_3_4;
import static morris_water_maze.util.DoubleComparison.EPSILON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class CalculationsTest
{
    private final Point
        unitVector1 = Point.newInstance(1,0);
    
    private final Point
        unitVector2 = Point.newInstance(0,1);
    

    
    
    @Test
    void shouldCalculateVectorLengthCorrectly()
    {
        assertThat(Calculations.length(unitVector1)).isEqualTo(1.0);
        assertThat(Calculations.length(unitVector2)).isEqualTo(1.0);
        assertThat(Calculations.length(VECTOR_3_4)).isEqualTo(5.0);
    }
    
    @Test
    void shouldCalculateDotProductLengthCorrectly()
    {
        assertThat(dotProduct(unitVector1, unitVector2)).isEqualTo(0.0);
        assertThat(dotProduct(unitVector1, VECTOR_1_2)).isEqualTo(VECTOR_1_2.getX());
        assertThat(dotProduct(unitVector2, VECTOR_1_2)).isEqualTo(VECTOR_1_2.getY());
        assertThat(dotProduct(VECTOR_1_2, VECTOR_3_4)).isEqualTo(11.0);
    }
    
    @Test
    void shouldCalculateAnglesCorrectly()
    {
        assertThat(angle(unitVector1, unitVector2)).isCloseTo(Math.PI/2.0, within(EPSILON));
        assertThat(angle(VECTOR_1_2, VECTOR_1_2)).isCloseTo(0.0, within(EPSILON));
        assertThat(angle(VECTOR_3_4, negate(VECTOR_3_4))).isCloseTo(Math.PI, within(EPSILON));
        assertThat(angle(unitVector1, VECTOR_1_1)).isCloseTo(Math.PI/4, within(EPSILON));
    }
    
    private static Point negate(Point point)
    {
        return Point.newInstance(-point.getX(), -point.getY());
    }
}