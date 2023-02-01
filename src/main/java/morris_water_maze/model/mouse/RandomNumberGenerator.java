package morris_water_maze.model.mouse;

import java.util.Random;

public class RandomNumberGenerator implements RandomNumbers
{
    private final Random
        random = new Random();
    
    
    @Override
    public double nextDouble()
    {
        return random.nextDouble();
    }
    
    @Override
    public double nextGaussian()
    {
        return random.nextGaussian();
    }
}
