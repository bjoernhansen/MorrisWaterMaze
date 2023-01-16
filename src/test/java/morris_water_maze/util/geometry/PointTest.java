package morris_water_maze.util.geometry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PointTest
{
    private static final double
        X = 2;
    
    private static final double
        Y = 3;
    
    
    private static Point point;
 
    @BeforeAll
    static void setUp()
    {
        point = Point.newInstance(X, Y);
    }
    
    @Test
    void shouldReturnCorrectCoordinates()
    {
        assertThat(point.getX()).isEqualTo(X);
        assertThat(point.getY()).isEqualTo(Y);
    }
    
    @Test
    void shouldTranslateCorrectly()
    {
        double distance = 3;
        assertThat(point.translate(distance, Math.PI)).isEqualTo(Point.newInstance(X-distance, Y));
        assertThat(point.translate(distance, 0)).isEqualTo(Point.newInstance(X+distance, Y));
        assertThat(point.translate(distance, 2*Math.PI)).isEqualTo(Point.newInstance(X+distance, Y));
        assertThat(point.translate(distance, Math.PI/2.0)).isEqualTo(Point.newInstance(X, Y+distance));
        assertThat(point.translate(distance, 3*Math.PI/2.0)).isEqualTo(Point.newInstance(X, Y-distance));
        assertThat(point.translate(distance, Math.PI/4.0)).isEqualTo(Point.newInstance(X+distance*(1.0/Math.sqrt(2)), Y+distance*(1.0/Math.sqrt(2))));
    }
    
}
