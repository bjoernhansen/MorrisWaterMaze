package morris_water_maze.util;

import morris_water_maze.util.geometry.Point;
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
}
