package morris_water_maze.util;

import org.junit.jupiter.api.Test;

import static morris_water_maze.util.DoubleComparison.EPSILON;
import static morris_water_maze.util.DoubleComparison.doubleEquals;
import static org.assertj.core.api.Assertions.assertThat;


class DoubleComparisonTest
{
    @Test
    void shouldReturnRightComparisonResult()
    {
        double value = 1.234;
        assertThat(doubleEquals(value, value + EPSILON * 1.1)).isFalse();
        assertThat(doubleEquals(value, value + EPSILON)).isTrue();
        assertThat(doubleEquals(value, value + EPSILON * 0.9)).isTrue();
    }
}
