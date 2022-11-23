package morris_water_maze.control.observer;

import morris_water_maze.model.simulation.Simulation;


public interface SimulationSeriesCompletionObserver
{
    void beNotifiedAboutEndOfAllSimulations();
    
    void setSimulation(Simulation simulation);
}
