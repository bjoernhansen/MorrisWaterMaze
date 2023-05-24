package morris_water_maze.report.histogram.number_generation;

import java.util.Random;


public class GaussianNumberGenerator implements RandomNumberGenerator
{
    public final double
        mean;
    
    public final double
        sigma;
    
    private final Random
        random = new Random();
    
    
    public GaussianNumberGenerator(double mean, double sigma)
    {
        this.mean = mean; // 100
        this.sigma = sigma; // 10
    }
    
    @Override
    public double nextRandomNumber()
    {
        return sigma * random.nextGaussian() + mean;
    }
}
