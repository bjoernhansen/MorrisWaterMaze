package morris_water_maze.util.geometry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineSegmentTest
{
    private static final Point
        POINT_1 = Point.newInstance(50.0, 50.0);
    private static final Point
        POINT_2 = Point.newInstance(50.0, 100.0);
    
    private static final Point
        POINT_3 = Point.newInstance(100.0, 50.0);
    
    private static final LineSegment
        VERTICAL_LINE_SEGMENT = LineSegmentBuilder.from(POINT_1).to(POINT_2);
    
    private static final LineSegment
        HORIZONTAL_LINE_SEGMENT = LineSegmentBuilder.from(POINT_1).to(POINT_3);
    
   
    @Test
    void shouldInstantiateLineSegmentCorrectly()
    {
        assertThatThrownBy(() -> LineSegment.newInstance(null, POINT_2)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> LineSegment.newInstance(POINT_1, null)).isInstanceOf(NullPointerException.class);
        assertThatCode(() -> LineSegment.newInstance(POINT_1, POINT_2)).doesNotThrowAnyException();
        assertThat(VERTICAL_LINE_SEGMENT.getStart()).isEqualTo(POINT_1);
        assertThat(VERTICAL_LINE_SEGMENT.getEnd()).isEqualTo(POINT_2);
    }
    
    @Test
    void shouldCalculateIntersectionsCorrectly()
    {
        Square squareAboveHorizontalLine = Square.newInstance(60.0, 20.0, 20.0);
        Square squareIntersectingHorizontalLine = Square.newInstance(60.0, 20.0, 50.0);
        Square squareBelowHorizontalLine = Square.newInstance(60.0, 80.0, 100.0);
    
        Square squareLeftOfVerticalLine = Square.newInstance(20.0, 60.0, 20.0);
        Square squareIntersectingVerticalLine = Square.newInstance(20.0, 60.0, 50.0);
        Square squareBelowRightOfVerticalLine = Square.newInstance(80.0, 60.0, 100.0);
    
        assertThat(HORIZONTAL_LINE_SEGMENT.intersects(squareAboveHorizontalLine)).isFalse();
        assertThat(HORIZONTAL_LINE_SEGMENT.intersects(squareIntersectingHorizontalLine)).isTrue();
        assertThat(HORIZONTAL_LINE_SEGMENT.intersects(squareBelowHorizontalLine)).isFalse();
        
        assertThat(VERTICAL_LINE_SEGMENT.intersects(squareLeftOfVerticalLine)).isFalse();
        assertThat(VERTICAL_LINE_SEGMENT.intersects(squareIntersectingVerticalLine)).isTrue();
        assertThat(VERTICAL_LINE_SEGMENT.intersects(squareBelowRightOfVerticalLine)).isFalse();
    }
}
