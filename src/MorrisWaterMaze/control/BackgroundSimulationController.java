package MorrisWaterMaze.control;

import MorrisWaterMaze.model.simulation.Simulation;


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
        while(simulation.isNotFinished())
        {
            simulation.nextStep();
        }
    }
}
