package morris_water_maze.control.gui;

import morris_water_maze.control.SimulationController;
import morris_water_maze.model.simulation.Simulation;


public abstract class GuiSimulationController extends SimulationController
{
    private boolean
        isSimulationInProgress = false;
    
    protected GuiSimulationController(Simulation simulationInstance)
    {
        super(simulationInstance);
    }
    
    public void switchSimulationActivityState()
    {
        isSimulationInProgress = !isSimulationInProgress;
    }
    
    public boolean isSimulationInProgress()
    {
        return isSimulationInProgress;
    }
    
    public void stopLooping()
    {
        isSimulationInProgress = false;
    }
}
