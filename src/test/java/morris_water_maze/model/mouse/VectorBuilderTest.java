package morris_water_maze.model.mouse;

import morris_water_maze.util.Point;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VectorBuilderTest
{
    static final Point
        VECTOR_1_1 = Point.newInstance(1,1);
    
    static final Point
        VECTOR_1_2 = Point.newInstance(1,2);
    
    static final Point
        VECTOR_3_4 = Point.newInstance(3,4);
    
    
    
    @Test
    void shouldCalculateVectorsCorrectly()
    {
        assertThat(VectorBuilder.from(VECTOR_3_4).to(VECTOR_1_1)).isEqualTo(Point.newInstance(-2,-3));
        assertThat(VectorBuilder.from(VECTOR_1_1).to(VECTOR_1_1)).isEqualTo(Point.newInstance(0,0));
        assertThat(VectorBuilder.from(VECTOR_1_2).to(VECTOR_3_4)).isEqualTo(Point.newInstance(2,2));
    }
}