package morris_water_maze.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


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
        Assertions.assertEquals(point.getX(), X);
        Assertions.assertEquals(point.getY(), Y);
    }
}
