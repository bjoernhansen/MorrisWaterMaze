package morris_water_maze.report.histogram.number_generation;

import ec.util.MersenneTwisterFast;
import sim.util.distribution.VonMises;

public class VonMisesNumberGenerator implements RandomNumberGenerator
{
    private final VonMises vonMises;
    
    
    public VonMisesNumberGenerator()
    {
        MersenneTwisterFast numberGenerator = new MersenneTwisterFast();
        vonMises = new VonMises(100, numberGenerator);
    }
    
    public VonMisesNumberGenerator(int seed)
    {
        MersenneTwisterFast numberGenerator = new MersenneTwisterFast(seed);
        vonMises = new VonMises(100, numberGenerator);
    }
    
    @Override
    public double nextRandomNumber()
    {
        return vonMises.nextDouble();
    }
}
