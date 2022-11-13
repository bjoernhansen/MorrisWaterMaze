package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.util.Stack;

import java.util.function.Consumer;


public class SearchTimeContainer implements SearchTimeProvider
{
    private final Stack<Double>
        searchTimes = new Stack<>();
        
    
    @Override
    public double getLastSearchTime()
    {
        return searchTimes.peek();
    }
    
    @Override
    public void forEachSearchTime(Consumer<String> action)
    {
        searchTimes.stream()
                   .map(String::valueOf)
                   .forEach(action);
    }
    
    void add(double lastSearchTime)
    {
        searchTimes.push(lastSearchTime);
    }
    
    double calculateSumOfSearchTimes()
    {
        return searchTimes.stream()
                          .mapToDouble(Double::doubleValue)
                          .sum();
    }
    
    void clear()
    {
        searchTimes.clear();
    }
}
