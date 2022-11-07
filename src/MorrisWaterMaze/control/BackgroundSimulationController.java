package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.WaterMorrisMazeSimulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


final class BackgroundSimulationController extends SimulationController
{
    BackgroundSimulationController(WaterMorrisMazeSimulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainter)
    {
        super(simulationInstance, parameterAccessor, imagePainter);
    }
    
    @Override
    public void start()
    {
        WaterMorrisMazeSimulation simulation = getSimulation();
        while(simulation.isNotFinished())
        {
            simulation.nextStep();
        }
    }
}
