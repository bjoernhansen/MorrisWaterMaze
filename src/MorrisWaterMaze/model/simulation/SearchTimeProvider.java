package MorrisWaterMaze.model.simulation;

import java.util.function.Consumer;

public interface SearchTimeProvider
{
    double getLastSearchTime();
    
    double getAverageSearchTime();
    
    void forEachSearchTime(Consumer<String> action);
}
