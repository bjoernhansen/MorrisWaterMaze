package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.control.observer.SimulationSeriesCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationRunCompletionObserver;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.SettingModifier;


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
