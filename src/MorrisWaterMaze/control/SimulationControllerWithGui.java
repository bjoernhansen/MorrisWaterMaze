package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.JFrame;
import java.util.Optional;


final class SimulationControllerWithGui extends SimulationController implements Runnable, LoopController
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
    

    public SimulationControllerWithGui(Simulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainter)
    {
        super(simulationInstance, parameterAccessor, imagePainter);
    
        simulationPanel = new SimulationPanel(getSimulation(), parameterAccessor, this, imagePainter, getSimulation());
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
    public void reset()
    {
        Optional.ofNullable(simulationPanel).ifPresent(
            panel -> panel.setStartAndPauseButtonText("Start"));
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
