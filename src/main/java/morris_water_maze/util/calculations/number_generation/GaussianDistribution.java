package morris_water_maze.util.calculations.number_generation;

import java.util.Random;


public class GaussianDistribution implements RandomDistribution
{
    // TODO Eingabe des Seeds fehlt noch
    
    private final double
        mean;
    
    private final double
        sigma;
    
    private final Random
        random = new Random();
    
    
    public GaussianDistribution(double sigma)
    {
        this(0, sigma);
    }
    
    public GaussianDistribution(double mean, double sigma)
    {
        this.mean = mean;
        this.sigma = sigma;
    }
    
    @Override
    public double nextDouble()
    {
        return nextDouble(mean);
    }
    
    @Override
    public double nextDouble(double mean)
    {
        return sigma * nextGaussian() + mean;
    }
    
    public VonMisesDistribution getCorrespondingVonMisesDistribution()
    {
        return new VonMisesDistribution(mean, sigma);
    }
    
    double nextGaussian()
    {
        return random.nextGaussian();
    }
}
