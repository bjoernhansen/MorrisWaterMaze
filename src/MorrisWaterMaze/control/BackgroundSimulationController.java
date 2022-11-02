package MorrisWaterMaze.control;

import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


final class BackgroundSimulationController extends SimulationController
{
    BackgroundSimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor)
    {
        super(simulationInstance, parameterAccessor);
    }
    
    @Override
    public void start()
    {
        while(getSimulation().isAnotherSimulationToBeStarted())
        {
            getSimulation().nextStep(this);
        }
    }
}
