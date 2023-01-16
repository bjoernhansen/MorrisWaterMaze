package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class GeometryTest
{
    private static final Point
        CIRCLE_CENTER = Point.newInstance(100.0, 100.0);
    
    private static final Circle
        CIRCLE = Circle.newInstance(CIRCLE_CENTER, 50.0);
    
    private static final Point
        P1 = Point.newInstance(2.0 * CIRCLE.getMaxX(), 0.5 * CIRCLE.getY());
    
    private static final Point
        P2 = Point.newInstance(2.0 * CIRCLE.getMaxX(), 1.5 * CIRCLE.getDiameter() + CIRCLE.getY());
    
    private static final LineSegment
        LINE_SEGMENT_OUTSIDE = LineSegment.from(P1).to(P2);
    
    private static final LineSegment
        LINE_SEGMENT_WITH_INTERSECTION = LineSegment.from(CIRCLE_CENTER).to(P1);
    
    
    @Test
    void shouldCalculateExitPointsCorrectly()
    {
        assertThat(Geometry.circleLineSegmentIntersection(CIRCLE, LINE_SEGMENT_OUTSIDE)).isEmpty();
    
        Optional<Point> optionalExitPoint = Geometry.circleLineSegmentIntersection(CIRCLE, LINE_SEGMENT_WITH_INTERSECTION);
        assertThat(optionalExitPoint).isNotEmpty();
        Point exitPoint = optionalExitPoint.get();
        assertThat(Geometry.isPointOnCircle(CIRCLE, exitPoint)).isTrue();
    }
}
