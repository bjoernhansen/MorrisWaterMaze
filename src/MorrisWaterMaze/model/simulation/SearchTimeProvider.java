package MorrisWaterMaze.model.simulation;

import java.util.function.Consumer;

public interface SearchTimeProvider
{
    double getLastSearchTime();
    
    void forEachSearchTime(Consumer<String> action);
}
