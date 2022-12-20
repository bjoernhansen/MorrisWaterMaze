package morris_water_maze.util.geometry;

import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.LineSegmentBuilder;
import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LineSegmentBuilderTest
{
    private static final Point
        VECTOR_1_1 = Point.newInstance(1,1);
    
    private static final Point
        VECTOR_3_4 = Point.newInstance(3,4);
    
    
    @Test
    void shouldCalculateLineSegmentsCorrectly()
    {
        LineSegment lineSegment = LineSegmentBuilder.from(VECTOR_3_4)
                                                    .to(VECTOR_1_1);
    
        assertThat(lineSegment.getStart()
                              .equals(VECTOR_3_4)).isTrue();
    
        assertThat(lineSegment.getEnd()
                              .equals(VECTOR_1_1)).isTrue();
    }
}
