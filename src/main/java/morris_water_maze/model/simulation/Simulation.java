package morris_water_maze.model.simulation;

import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.graphics.Paintable;


public interface Simulation extends SettingModifier, Paintable
{
    void nextStep();
    
    boolean areAllSimulationRunsCompleted();
    
    void reset();
    
    void registerSimulationStepObservers(SimulationRunCompletionObserver simulationRunCompletionObserver);
    
    void registerSimulationSeriesCompletionObservers(SimulationSeriesCompletionObserver simulationSeriesCompletionObserver);
    
    int getNumberOfCompletedSimulationRuns();
    
    int getTotalNumberOfSimulationRuns();
    
    double getAverageSearchTime();
    
    SearchTimeProvider getSearchTimeProvider();
}
