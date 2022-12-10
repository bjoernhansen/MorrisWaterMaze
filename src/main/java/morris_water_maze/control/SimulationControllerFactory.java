package morris_water_maze.control;

import morris_water_maze.control.gui.GuiType;
import morris_water_maze.control.gui.javafx.JavaFxSimulationController;
import morris_water_maze.control.gui.swing.SwingSimulationController;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;

import static morris_water_maze.Main.GUI_TYPE;


public final class SimulationControllerFactory
{
    public static SimulationController newInstance(Simulation simulation, ParameterAccessor parameterAccessor)
    {
        if(parameterAccessor.isStartingWithGui())
        {
            return GUI_TYPE == GuiType.SWING
                ? new SwingSimulationController(simulation, parameterAccessor)
                : new JavaFxSimulationController(simulation, parameterAccessor);
        }
        else
        {
            return new BackgroundSimulationController(simulation);
        }
    }
}
