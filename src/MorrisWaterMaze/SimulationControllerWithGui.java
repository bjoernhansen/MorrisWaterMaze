package MorrisWaterMaze;

import MorrisWaterMaze.graphics.painter.PaintManager;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.JFrame;

public class SimulationControllerWithGui extends SimulationController implements Runnable
{
    private static final int
        PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;
    
    private final Thread
        animator;
    
    private final JFrame
        simulationFrame;
    
    private final SimulationPanel
        simulationPanel;
    
    private final PaintManager
        paintManager = new PaintManager();
    
    
    SimulationControllerWithGui(Simulation simulationInstance, ParameterAccessor parameterAccessor)
    {
        super(simulationInstance, parameterAccessor);
    
        simulationPanel = new SimulationPanel(getSimulation(), parameterAccessor, this);
        simulationFrame = new SimulationFrame(simulationPanel);
        simulationFrame.setVisible(true);
    
        animator = new Thread(this);
        animator.start();
    }
    
    @Override
    public void run()
    {
        while (Thread.currentThread() == this.animator)
        {
            try
            {
                Thread.sleep(PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                break;
            }
            if(getLoopState())
            {
                getSimulation().nextStep(this);
            }
            simulationFrame.repaint();
        }
    }
    
    @Override
    void reset()
    {
        simulationPanel.setStartAndPauseButtonText("Start");
        super.reset();
    }
}
