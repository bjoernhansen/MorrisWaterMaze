package MorrisWaterMaze.control.observer;

import MorrisWaterMaze.model.simulation.Simulation;


public interface SimulationCompletionObserver
{
    void beNotifiedAboutEndOfSimulation();
    
    void setSimulation(Simulation simulation);
}
