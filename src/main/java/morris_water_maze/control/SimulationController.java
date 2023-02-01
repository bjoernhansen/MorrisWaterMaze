package morris_water_maze.control;

import morris_water_maze.model.simulation.Simulation;


public abstract class SimulationController
{
    private final Simulation
        simulation;
    
    protected SimulationController(Simulation simulation)
    {
        simulation.reset();
        this.simulation = simulation;
    }
    
    protected Simulation getSimulation()
    {
        return simulation;
    }
    
    public abstract void start();
}