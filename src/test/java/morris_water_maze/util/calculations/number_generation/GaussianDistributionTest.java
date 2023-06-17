package morris_water_maze.util.calculations.number_generation;

import morris_water_maze.util.calculations.DoubleComparison;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class GaussianDistributionTest
{
    private static final double
        MEAN = 100;
    
    private static final double
        SIGMA = 100;
    
    private final static List<Double>
        gaussianValues = List.of(0.0, 0.5);
    
    
    @Test
    void shouldCalculateGaussianValuesCorrectly()
    {
        TestingGaussianDistribution gaussianDistribution
            = new TestingGaussianDistribution(MEAN, SIGMA, gaussianValues);
        
        gaussianValues.forEach(value -> {
            assertThat(gaussianDistribution.nextDouble()).isCloseTo(value * SIGMA + MEAN, within(DoubleComparison.EPSILON));
        });
    }
}
