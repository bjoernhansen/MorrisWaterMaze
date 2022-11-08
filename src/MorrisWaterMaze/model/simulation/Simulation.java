package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.control.observer.SimulationCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.SettingModifier;


public interface Simulation extends SearchTimeProvider, SettingModifier, Paintable
{
    void nextStep();
    
    boolean isNotFinished();
    
    void reset();
    
    void registerSimulationStepObservers(SimulationStepObserver simulationStepObserver);
    
    void registerSimulationCompletionObservers(SimulationCompletionObserver simulationCompletionObserver);
    
    int getNumberOfCompletedSimulations();
    
    int getTotalNumberOfSimulations();
}
