package MorrisWaterMaze.control.observer;

import MorrisWaterMaze.model.simulation.Simulation;


public interface SimulationStepObserver
{
    void beNotifiedAboutEndOfLastSimulationStep();
    
    void setSimulation(Simulation simulation);
}
