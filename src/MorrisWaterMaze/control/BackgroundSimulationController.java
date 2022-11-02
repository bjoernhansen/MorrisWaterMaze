package MorrisWaterMaze.control;

import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


public class BackgroundSimulationController extends SimulationController
{
    BackgroundSimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor)
    {
        super(simulationInstance, parameterAccessor);
    
        while(getSimulation().isAnotherSimulationToBeStarted())
        {
            getSimulation().nextStep(this);
        }
    }
}
