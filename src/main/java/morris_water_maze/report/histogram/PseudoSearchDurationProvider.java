package morris_water_maze.report.histogram;

import morris_water_maze.model.simulation.SearchTimeProvider;
import morris_water_maze.util.calculations.number_generation.RandomDistribution;
import morris_water_maze.util.Stack;

import java.util.List;
import java.util.function.Consumer;


final class PseudoSearchDurationProvider implements SearchTimeProvider
{
    private final int
        numberOfGeneratedValues;
        
    private final Stack<Double>
        stack;
    
    private final RandomDistribution randomDistribution;
    
    
    public PseudoSearchDurationProvider(int numberOfGeneratedValues, RandomDistribution randomDistribution)
    {
        this.numberOfGeneratedValues = numberOfGeneratedValues; // 25000
        this.randomDistribution = randomDistribution;
        stack = getPseudoSearchTimes();
    }
    
    private Stack<Double> getPseudoSearchTimes()
    {
        Stack<Double> list = new Stack<>();
        for(int i = 0; i < numberOfGeneratedValues; i++)
        {
            list.push(randomDistribution.nextDouble(0));
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
