package morris_water_maze.control;

import morris_water_maze.control.gui.SimulationControllerWithGui;
import morris_water_maze.graphics.painter.ImagePainter;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;


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
