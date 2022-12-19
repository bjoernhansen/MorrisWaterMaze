package morris_water_maze.model.simulation;

import morris_water_maze.util.Stack;

import java.util.function.Consumer;


public final class SearchTimeContainer implements SearchTimeProvider
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
