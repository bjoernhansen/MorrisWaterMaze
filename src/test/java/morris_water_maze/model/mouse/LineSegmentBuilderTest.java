package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Line;
import org.junit.jupiter.api.Test;

import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_1;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_3_4;
import static org.assertj.core.api.Assertions.assertThat;


class LineSegmentBuilderTest
{
    @Test
    void shouldCalculateLineSegmentsCorrectly()
    {
        Line line = LineSegmentBuilder.from(VECTOR_3_4)
                                      .to(VECTOR_1_1);
    
        assertThat(line.getStart()
                       .equals(VECTOR_3_4)).isTrue();
    
        assertThat(line.getEnd()
                       .equals(VECTOR_1_1)).isTrue();
    }
}
