package morris_water_maze.util.geometry;

import morris_water_maze.util.calculations.DoubleComparison;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class CircleTest
{
    private static final double 
        X = 10.0;
    private static final double 
        Y = 20.0;
    
    private static final Point
        CENTER = Point.newInstance(X, Y);
    
    private static final double
        RADIUS = 30;
    
    private static final Circle
        CIRCLE = Circle.newInstance(CENTER, RADIUS);

    
    @Test
    void shouldInstantiateCircleCorrectly()
    {
        assertThat(CIRCLE.getCenter()).isEqualTo(CENTER);
        assertThat(CIRCLE.getRadius()).isEqualTo(RADIUS);
        
        assertThat(CIRCLE.getX()).isCloseTo(X - RADIUS, within(DoubleComparison.EPSILON));
        assertThat(CIRCLE.getMaxX()).isCloseTo(X + RADIUS, within(DoubleComparison.EPSILON));
        assertThat(CIRCLE.getY()).isCloseTo(Y - RADIUS, within(DoubleComparison.EPSILON));
        
        assertThat(CIRCLE.getDiameter()).isCloseTo(2.0 * RADIUS, within(DoubleComparison.EPSILON));
        assertThat(CIRCLE.getX() + CIRCLE.getDiameter()).isCloseTo(CIRCLE.getMaxX(), within(DoubleComparison.EPSILON));
    }
    
    @Test
    void shouldCalculateCorrectlyWhetherGivenPointIsContainedInCircle()
    {
        assertThat(CIRCLE.contains(CENTER)).isTrue();
        
        Point position = Point.newInstance(CIRCLE.getX(), CIRCLE.getY());
        assertThat(CIRCLE.contains(position)).isFalse();
        
        Point top = Point.newInstance(CIRCLE.getCenter().getX(), CIRCLE.getY() + DoubleComparison.EPSILON);
        assertThat(CIRCLE.contains(top)).isTrue();
        
        Point right = Point.newInstance(CIRCLE.getMaxX() - DoubleComparison.EPSILON, CIRCLE.getCenter().getY());
        assertThat(CIRCLE.contains(right)).isTrue();
    }
}