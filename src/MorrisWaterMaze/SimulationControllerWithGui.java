package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.JFrame;

public class SimulationControllerWithGui extends SimulationController implements Runnable, LoopController
{
    private static final int
        PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS = 100;
        
    private boolean
        loop = false;
    
    private final Thread
        animator;
    
    private final JFrame
        simulationFrame;
    
    private final SimulationPanel
        simulationPanel;
    

    SimulationControllerWithGui(Simulation simulationInstance, ParameterAccessor parameterAccessor)
    {
        super(simulationInstance, parameterAccessor);
    
        simulationPanel = new SimulationPanel(getSimulation(), parameterAccessor, this, imagePainter, getSimulation());
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
    
    @Override
    public void switchLoopState()
    {
        loop = !loop;
    }
    
    @Override
    public boolean getLoopState()
    {
        return loop;
    }
    
    @Override
    public void stopLooping()
    {
        loop = false;
    }
}
