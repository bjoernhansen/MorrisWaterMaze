package morris_water_maze.model.simulation;

import java.util.List;
import java.util.function.Consumer;

public interface SearchTimeProvider
{
    double getLastSearchTime();
    
    void forEachSearchTime(Consumer<String> action);
    
    List<Double> getSearchTimes();
}
