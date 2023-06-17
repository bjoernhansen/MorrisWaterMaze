package morris_water_maze.util.calculations.number_generation;

import java.util.List;


public class TestingGaussianDistribution extends GaussianDistribution
{
    private final List<Double>
        gaussianValues;
    
    private int
        index;
    
    
    public TestingGaussianDistribution(double mean, double sigma, List<Double> gaussianValues)
    {
        super(mean, sigma);
        this.gaussianValues = gaussianValues;
    }
    
    @Override
    public double nextGaussian()
    {
        int lastIndex = gaussianValues.size() - 1;
        if(index >= lastIndex)
        {
            return gaussianValues.get(lastIndex);
        }
        return gaussianValues.get(index++);
    }
}
