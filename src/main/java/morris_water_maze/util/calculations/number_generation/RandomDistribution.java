package morris_water_maze.util.calculations.number_generation;


public interface RandomDistribution
{
    double nextDouble();
    
    double nextDouble(double mean);
}
