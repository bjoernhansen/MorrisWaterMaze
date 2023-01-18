package morris_water_maze.control;

import morris_water_maze.control.gui.GuiType;
import morris_water_maze.control.gui.javafx.JavaFxSimulationController;
import morris_water_maze.control.gui.swing.SwingSimulationController;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterProvider;

import static morris_water_maze.Main.GUI_TYPE;


public final class SimulationControllerFactory
{
    public static SimulationController newInstance(Simulation simulation, ParameterProvider parameterProvider)
    {
        boolean isStartingWithGui = parameterProvider.getSimulationParameterProvider()
                                                     .isStartingWithGui();
        if(isStartingWithGui)
        {
            return GUI_TYPE == GuiType.SWING
                ? new SwingSimulationController(simulation, parameterProvider)
                : new JavaFxSimulationController(simulation);
        }
        else
        {
            return new BackgroundSimulationController(simulation);
        }
    }
}
