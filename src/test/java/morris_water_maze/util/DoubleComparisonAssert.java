package morris_water_maze.util;

import morris_water_maze.util.calculations.DoubleComparison;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DoubleComparisonAssert extends AbstractAssert<DoubleComparisonAssert, Double>
{
    protected DoubleComparisonAssert(Double actualValue)
    {
        super(actualValue, DoubleComparisonAssert.class);
    }
    
    public void isCloseTo(Double expected)
    {
        if(DoubleComparison.doubleEquals(actual, expected))
        {
            assertTrue(true);
        }
        else
        {
            Assertions.assertThat(actual)
                      .isEqualTo(expected);
        }
    }
    
    public static DoubleComparisonAssert assertThatDouble(Double actualValue)
    {
        return new DoubleComparisonAssert(actualValue);
    }
}
