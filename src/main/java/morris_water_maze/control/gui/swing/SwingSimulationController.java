package morris_water_maze.control.gui.swing;

import morris_water_maze.control.gui.GuiSimulationController;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterProviderGenerator;

import javax.swing.JFrame;
import java.util.Optional;


public final class SwingSimulationController extends GuiSimulationController implements Runnable
{
    private static final long
        SIMULATION_STEP_TIME_INTERVAL = 100;
    
    private final Thread
        animator;
    
    private final JFrame
        simulationFrame;
    
    private final SimulationPanel
        simulationPanel;
    


    public SwingSimulationController(Simulation simulationInstance, ParameterProviderGenerator parameterProviderGenerator)
    {
        super(simulationInstance);
    
        simulationPanel = new SimulationPanel(getSimulation(), parameterProviderGenerator, this, getSimulation());
        simulationFrame = new SimulationFrame(simulationPanel);
        simulationFrame.setVisible(true);
    
        animator = new Thread(this);
    }
    
    @Override
    public void start()
    {
        animator.start();
    }
    
    @Override
    public void run()
    {
        ConsecutiveInvocationTimer timer = new ConsecutiveInvocationTimer(SIMULATION_STEP_TIME_INTERVAL);
        
        while(Thread.currentThread() == this.animator)
        {
            if(timer.isNextSimulationStepToBeStartedNow())
            {
                if(isSimulationInProgress())
                {
                    getSimulation().nextStep();
                }
                simulationFrame.repaint();
            }
        }
    }
    
    public void reset()
    {
        Optional.ofNullable(simulationPanel).ifPresent(
            SimulationPanel::resetStartAndPauseButton);
        getSimulation().reset();
    }
}
