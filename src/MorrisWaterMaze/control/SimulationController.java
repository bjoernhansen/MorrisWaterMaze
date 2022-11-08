package MorrisWaterMaze.control;

import MorrisWaterMaze.model.simulation.Simulation;


public abstract class SimulationController
{
    private final Simulation
        simulation;
    
    SimulationController(Simulation simulationInstance)
    {
        simulation = simulationInstance;
        simulation.reset();
    }
    
    Simulation getSimulation()
    {
        return simulation;
    }
    
    public abstract void start();
}