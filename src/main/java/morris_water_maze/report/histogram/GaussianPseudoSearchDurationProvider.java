package morris_water_maze.report.histogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GaussianPseudoSearchDurationProvider
{
    public static final int NUMBER_OF_SEARCH_DURATIONS = 250000;
    public static final double MEAN = 100;
    public static final double SIGMA = 10;
    private final Random
        random = new Random();
    
    private final List<Double>
        list;
    
    
    public GaussianPseudoSearchDurationProvider()
    {
        list = getGaussianTestSearchTimes();
        analyzeList();
    }
    
    private List<Double> getGaussianTestSearchTimes()
    {
        List<Double> list = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_SEARCH_DURATIONS; i++)
        {
            list.add(gaussianCapped());
        }
        return list;
    }
    
    private void analyzeList()
    {
        long count = list.stream()
                         .filter(GaussianPseudoSearchDurationProvider::isInOneSigmaIntervall)
                         .count();
        
        double percentage =  (double) count / NUMBER_OF_SEARCH_DURATIONS;
    
        System.out.println("Percentage: " + percentage);
    }
    
    private static boolean isInOneSigmaIntervall(Double value)
    {
        return value >= MEAN - SIGMA && value <= MEAN + SIGMA;
    }
    
    private double gaussianCapped()
    {
        double gaussian = SIGMA * random.nextGaussian();
        
        if(Math.abs(gaussian) < 2 * SIGMA)
        {
            return gaussian + MEAN;
        }
        return gaussianCapped();
    }
    
    private double gaussian()
    {
        return SIGMA * random.nextGaussian() + MEAN;
    }
    
    private double gaussianMod()
    {
        return (SIGMA * random.nextGaussian())%(2.0*SIGMA) + MEAN;
    }
    
    public List<Double> getPseudoSearchTimeList()
    {
        return list;
    }
}
