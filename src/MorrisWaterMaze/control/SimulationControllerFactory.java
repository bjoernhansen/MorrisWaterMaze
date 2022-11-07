package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.WaterMorrisMazeSimulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


public final class SimulationControllerFactory
{
    public static SimulationController newInstance(WaterMorrisMazeSimulation simulation, ParameterAccessor parameterAccessor, ImagePainter imagePainter)
    {
        if(parameterAccessor.isStartingWithGui())
        {
            return new SimulationControllerWithGui(simulation, parameterAccessor, imagePainter);
        }
        else
        {
            return new BackgroundSimulationController(simulation, parameterAccessor, imagePainter);
        }
    }
}
