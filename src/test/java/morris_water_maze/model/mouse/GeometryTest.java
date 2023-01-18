package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import java.lang.IllegalArgumentException;
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
    
    private static final LineSegment
        INVERSE_LINE_SEGMENT_WITH_INTERSECTION = LineSegment.from(P1).to(CIRCLE_CENTER);
    
    
    @Test
    void shouldCalculateExitPointsCorrectly()
    {
        Optional<Point> optionalExitPoint1 = Geometry.lineSegmentExitPointOutOfCircle(LINE_SEGMENT_WITH_INTERSECTION, CIRCLE);
        assertThat(optionalExitPoint1).isNotEmpty();
        Point exitPoint1 = optionalExitPoint1.get();
        assertThat(exitPoint1.isOnTheEdgeOf(CIRCLE)).isTrue();
    
        Optional<Point> optionalExitPoint2 = Geometry.lineSegmentExitPointOutOfCircle(INVERSE_LINE_SEGMENT_WITH_INTERSECTION, CIRCLE);
        assertThat(optionalExitPoint2).isNotEmpty();
        Point exitPoint2 = optionalExitPoint1.get();
        
        assertThat(exitPoint1).isEqualTo(exitPoint2);
    }
    
    @Test
    void shouldNotReturnExitPointForLineSegmentsOutOfCircle()
    {
        assertThatThrownBy(() -> LINE_SEGMENT_OUTSIDE.getExitPointOutOf(CIRCLE)).isInstanceOf(IllegalArgumentException.class);
        assertThat(Geometry.lineSegmentExitPointOutOfCircle(LINE_SEGMENT_OUTSIDE, CIRCLE)).isEmpty();
    }
}
