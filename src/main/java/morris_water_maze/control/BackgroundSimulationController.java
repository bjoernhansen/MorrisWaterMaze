package morris_water_maze.control;

import morris_water_maze.model.simulation.Simulation;


final class BackgroundSimulationController extends SimulationController
{
    BackgroundSimulationController(Simulation simulation)
    {
        super(simulation);
    }
    
    @Override
    public void start()
    {
        Simulation simulation = getSimulation();
        while(!simulation.areAllSimulationRunsCompleted())
        {
            simulation.nextStep();
        }
    }
}
