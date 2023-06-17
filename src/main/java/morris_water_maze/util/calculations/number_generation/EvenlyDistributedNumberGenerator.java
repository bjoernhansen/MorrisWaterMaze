package morris_water_maze.util.calculations.number_generation;

import java.util.Random;


public class EvenlyDistributedNumberGenerator implements RandomNumberGenerator
{
    private final Random
        random = new Random();
    
    
    @Override
    public double nextDouble()
    {
        return random.nextDouble();
    }
}
