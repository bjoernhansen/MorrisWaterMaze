package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class GeometryTest
{
    private static final Circle
        CIRCLE = Circle.newInstance(Point.newInstance(100.0, 100.0), 50.0);
    
    private static final Point
        P1 = Point.newInstance(2.0 * CIRCLE.getMaxX(), 0.5 * CIRCLE.getY());
    
    private static final Point
        P2 = Point.newInstance(2.0 * CIRCLE.getMaxX(), 1.5 * CIRCLE.getDiameter() + CIRCLE.getY());
    
    private static final LineSegment
        LINE_SEGMENT = LineSegment.from(P1).to(P2);
    
    @Test
    void shouldCalculateVectorsCorrectly()
    {
        assertThatThrownBy(() -> Geometry.circleLineSegmentIntersection(CIRCLE, LINE_SEGMENT)).isInstanceOf(InvalidParameterException.class);
    }
}
