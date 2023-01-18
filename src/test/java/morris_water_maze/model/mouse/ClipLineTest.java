package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;
import org.junit.jupiter.api.Test;

import java.lang.IllegalArgumentException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ClipLineTest
{
    private static final Square
        SQUARE = Square.newInstance(2, 2, 4);
    
    private static final LineSegment
        NON_INTERSECTING_LINE_SEGMENT = LineSegment.from(Point.newInstance(1,1)).to(Point.newInstance(1,7));
    
    private static final LineSegment
        INTERSECTING_LINE_SEGMENT_1 = LineSegment.from(Point.newInstance(1,1)).to(Point.newInstance(7,7));
    
    private static final LineSegment
        EXPECTED_CLIPPED_LINE_1 = LineSegment.from(Point.newInstance(2,2)).to(Point.newInstance(6,6));
    
    private static final LineSegment
        INTERSECTING_LINE_SEGMENT_2 = LineSegment.from(Point.newInstance(4,1)).to(Point.newInstance(4,7));
    
    private static final LineSegment
        EXPECTED_CLIPPED_LINE_2 = LineSegment.from(Point.newInstance(4,2)).to(Point.newInstance(4,6));
    
        
    @Test
    void shouldCalculateClippedLinesCorrectly()
    {
        Optional<LineSegment> optionalClippedLine1 = Geometry.clipLine(INTERSECTING_LINE_SEGMENT_1, SQUARE);
        assertThat(optionalClippedLine1).isNotEmpty();
        assertThat(optionalClippedLine1.get()).isEqualTo(EXPECTED_CLIPPED_LINE_1);
    
        Optional<LineSegment> optionalClippedLine2 = Geometry.clipLine(INTERSECTING_LINE_SEGMENT_2, SQUARE);
        assertThat(optionalClippedLine2).isNotEmpty();
        assertThat(optionalClippedLine2.get()).isEqualTo(EXPECTED_CLIPPED_LINE_2);
    }
    
    @Test
    void shouldNotReturnIntersectionLineWithoutIntersection()
    {
        assertThatThrownBy(() -> NON_INTERSECTING_LINE_SEGMENT.getEntryPointInto(SQUARE)).isInstanceOf(IllegalArgumentException.class);
        Optional<LineSegment> optionalClippedLine1 = Geometry.clipLine(NON_INTERSECTING_LINE_SEGMENT, SQUARE);
        assertThat(optionalClippedLine1).isEmpty();
    }
}
