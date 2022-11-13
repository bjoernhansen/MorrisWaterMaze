package MorrisWaterMaze.control.observer;

import MorrisWaterMaze.model.simulation.Simulation;


public interface SimulationRunCompletionObserver
{
    void beNotifiedAboutCompletionOfCurrentSimulationRun();
    
    void setSimulation(Simulation simulation);
}
