package MorrisWaterMaze.control.observer;

import MorrisWaterMaze.model.simulation.Simulation;


public interface SimulationSeriesCompletionObserver
{
    void beNotifiedAboutEndOfAllSimulations();
    
    void setSimulation(Simulation simulation);
}
