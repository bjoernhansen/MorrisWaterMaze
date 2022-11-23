package morris_water_maze.control.observer;

import morris_water_maze.model.simulation.Simulation;


public interface SimulationRunCompletionObserver
{
    void beNotifiedAboutCompletionOfCurrentSimulationRun();
    
    void setSimulation(Simulation simulation);
}
