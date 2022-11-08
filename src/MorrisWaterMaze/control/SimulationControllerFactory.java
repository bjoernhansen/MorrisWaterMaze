package MorrisWaterMaze.control;

import MorrisWaterMaze.control.gui.SimulationControllerWithGui;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.simulation.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;


public final class SimulationControllerFactory
{
    public static SimulationController newInstance(Simulation simulation, ParameterAccessor parameterAccessor, ImagePainter imagePainter)
    {
        if(parameterAccessor.isStartingWithGui())
        {
            return new SimulationControllerWithGui(simulation, parameterAccessor, imagePainter);
        }
        else
        {
            return new BackgroundSimulationController(simulation);
        }
    }
}
