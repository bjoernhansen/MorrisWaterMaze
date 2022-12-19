package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;
import org.junit.jupiter.api.Test;

import java.awt.geom.Line2D;

import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_1_1;
import static morris_water_maze.model.mouse.VectorBuilderTest.VECTOR_3_4;
import static org.assertj.core.api.Assertions.assertThat;


class LineSegmentBuilderTest
{
    @Test
    void shouldCalculateLineSegmentsCorrectly()
    {
        Line2D line = LineSegmentBuilder.from(VECTOR_3_4)
                                        .to(VECTOR_1_1);
        
        assertThat(Point.of(line.getP1())
                        .equals(VECTOR_3_4)).isTrue();
        
        assertThat(Point.of(line.getP2())
                        .equals(VECTOR_1_1)).isTrue();
    }
}
