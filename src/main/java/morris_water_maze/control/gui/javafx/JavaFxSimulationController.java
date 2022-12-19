package morris_water_maze.control.gui.javafx;

import javafx.application.Application;
import morris_water_maze.control.gui.GuiSimulationController;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;


public final class JavaFxSimulationController extends GuiSimulationController
{
    public JavaFxSimulationController(Simulation simulation, ParameterAccessor parameterAccessor)
    {
        super(simulation);
        SimulationApplication.simulation = simulation;
    }
    
    @Override
    public void start()
    {
        Application.launch(SimulationApplication.class);
    }
}
