package MorrisWaterMaze.model;

import MorrisWaterMaze.control.observer.SimulationCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.Paintable;

import java.util.List;


public interface Simulation extends SettingModifier, Paintable
{
    public void nextStep();
    
    public boolean isNotFinished();
    
    public double calculateSumOfSearchTimes();
    
    public void reset();
    
    public void registerSimulationStepObserver(SimulationStepObserver simulationStepObserver);
    
    public void registerSimulationCompletionObserver(SimulationCompletionObserver simulationCompletionObserver);
    
    public int getNumberOfCompletedSimulations();
    
    public int getTotalNumberOfSimulations();
    
    public double getLastSearchTime();
    
    public List<Double> getSearchTimes();
}
