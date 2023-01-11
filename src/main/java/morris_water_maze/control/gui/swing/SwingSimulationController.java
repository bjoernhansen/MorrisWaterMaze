package morris_water_maze.control.gui.swing;

import morris_water_maze.control.gui.GuiSimulationController;
import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterProvider;
import morris_water_maze.parameter.SimulationParameterProvider;

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
    


    public SwingSimulationController(Simulation simulationInstance, SimulationParameterProvider simulationParameterProvider)
    {
        super(simulationInstance);
    
        simulationPanel = new SimulationPanel(getSimulation(), simulationParameterProvider, this, ImagePainterType.DEFAULT.makeInstance(), getSimulation());
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
