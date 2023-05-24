package morris_water_maze.report.histogram;

import morris_water_maze.model.simulation.SearchTimeProvider;
import morris_water_maze.report.histogram.number_generation.RandomNumberGenerator;
import morris_water_maze.util.Stack;

import java.util.List;
import java.util.function.Consumer;


public class PseudoSearchDurationProvider implements SearchTimeProvider
{
    public final int
        numberOfGeneratedValues;
        
    private final Stack<Double>
        stack;
    
    private final RandomNumberGenerator randomNumberGenerator;
    
    
    public PseudoSearchDurationProvider(int numberOfGeneratedValues, RandomNumberGenerator randomNumberGenerator)
    {
        this.numberOfGeneratedValues = numberOfGeneratedValues; // 25000
        this.randomNumberGenerator = randomNumberGenerator;
        stack = getPseudoSearchTimes();
    }
    
    private Stack<Double> getPseudoSearchTimes()
    {
        Stack<Double> list = new Stack<>();
        for(int i = 0; i < numberOfGeneratedValues; i++)
        {
            list.push(randomNumberGenerator.nextRandomNumber());
        }
        return list;
    }
    
    @Override
    public double getLastSearchTime()
    {
        return stack.peek();
    }
    
    @Override
    public void forEachSearchTime(Consumer<String> action)
    {
        stack.stream()
             .map(String::valueOf)
             .forEach(action);
    }
    
    @Override
    public List<Double> getSearchTimes()
    {
        return stack.asList();
    }
}
