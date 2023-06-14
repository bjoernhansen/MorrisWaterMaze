package morris_water_maze.util.calculations.number_generation_1;

import ec.util.MersenneTwisterFast;
import sim.util.distribution.VonMises;


public class VonMisesDistribution implements RandomDistribution
{
    private final double
        mean;
    
    private final VonMises
        vonMises;
    
    
    VonMisesDistribution(double sigma)
    {
        this(0, sigma);
    }
    
    VonMisesDistribution(double mean, double sigma)
    {
        this.mean = mean;
        MersenneTwisterFast numberGenerator = new MersenneTwisterFast(); // new MersenneTwisterFast(seed);
        double kappa = 1 / (sigma * sigma);
        vonMises = new VonMises(kappa, numberGenerator);
    }
    
    @Override
    public double nextDouble()
    {
        return nextDouble(mean);
    }
    
    @Override
    public double nextDouble(double mean)
    {
        double v = mean + vonMises.nextDouble();
        return v > Math.PI ? v - 2 * Math.PI : v;
    }
}
