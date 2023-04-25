package morris_water_maze;

import morris_water_maze.control.SimulationController;
import morris_water_maze.control.SimulationControllerFactory;
import morris_water_maze.control.gui.GuiType;
import morris_water_maze.control.observer.ObserverRegistrator;
import morris_water_maze.model.Pool;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;
import morris_water_maze.parameter.ParameterProvider;
import morris_water_maze.report.FileNameProvider;
import morris_water_maze.util.DirectoryCreator;


public final class Main
{
    public static final GuiType
        GUI_TYPE = GuiType.SWING;
    
    public static final double
        ZOOM_FACTOR = 4.0;
    
    public static final int
        IMAGE_SIZE = (int) (ZOOM_FACTOR * 2.0 * Pool.CENTER_TO_BORDER_DISTANCE);
    
    
    private Main()
    {
        throw new UnsupportedOperationException();
    }
    
    public static void main(String[] args)
    {
        ParameterProvider parameterProvider = new ParameterProvider();
        createDirectories(parameterProvider);
        
        Simulation simulation = getSimulationWithObserversRegistered(parameterProvider);
        
        SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterProvider);
        simulationController.start();
    }
    
    private static void createDirectories(ParameterProvider parameterProvider)
    {
        FileNameProvider fileNameProvider = parameterProvider.getFileNameProvider();
        DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
        directoryCreator.makeDirectories();
    }
    
    private static Simulation getSimulationWithObserversRegistered(ParameterProvider parameterProvider)
    {
        Simulation simulation = new WaterMorrisMazeSimulation(parameterProvider);

        ObserverRegistrator observerRegistrator = new ObserverRegistrator(parameterProvider);
        observerRegistrator.registerObserversFor(simulation);
        
        return simulation;
    }
}
