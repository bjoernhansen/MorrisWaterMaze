package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.simulation.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


public final class SimulationControllerFactory
{
    public static SimulationController newInstance(Simulation simulation, ParameterAccessor parameterAccessor, ImagePainter imagePainter, FileNameProvider fileNameProvider)
    {
        if(parameterAccessor.isStartingWithGui())
        {
            return new SimulationControllerWithGui(simulation, parameterAccessor, imagePainter, fileNameProvider);
        }
        else
        {
            return new BackgroundSimulationController(simulation);
        }
    }
}
