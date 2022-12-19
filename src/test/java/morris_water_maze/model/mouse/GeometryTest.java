package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;

import static org.assertj.core.api.Assertions.assertThat;


class GeometryTest
{
    private static final Point
        POSITION = Point.newInstance(10.0, 20.0);
    
    private static final double
        SIZE = 30;
    
    private static final Ellipse2D
        ellipse = new Ellipse2D.Double(POSITION.getX(), POSITION.getY(), SIZE, SIZE);
    
        
    @Test
    void shouldCalculateEllipseCorrectly()
    {
        Point center = Point.newInstance(ellipse.getCenterX(), ellipse.getCenterY());
        double radius = SIZE /2.0;
        Ellipse2D calculatedEllipse = Geometry.calculateEllipse(center, radius);
        
        assertThat(ellipse).isEqualTo(calculatedEllipse);
    }
}
