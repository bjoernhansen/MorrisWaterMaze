package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


final class BackgroundSimulationController extends SimulationController
{
    BackgroundSimulationController(Simulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainter)
    {
        super(simulationInstance, parameterAccessor, imagePainter);
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
