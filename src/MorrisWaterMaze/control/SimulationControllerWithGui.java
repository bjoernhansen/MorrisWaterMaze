package MorrisWaterMaze.control;

import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.simulation.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.JFrame;
import java.util.Optional;


final class SimulationControllerWithGui extends SimulationController implements Runnable
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
    

    public SimulationControllerWithGui(Simulation simulationInstance, ParameterAccessor parameterAccessor, ImagePainter imagePainter, FileNameProvider fileNameProvider)
    {
        super(simulationInstance);
    
        simulationPanel = new SimulationPanel(getSimulation(), parameterAccessor, this, imagePainter.makeCopy(), getSimulation());
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
                getSimulation().nextStep();
            }
            simulationFrame.repaint();
        }
    }
    
    public void reset()
    {
        Optional.ofNullable(simulationPanel).ifPresent(
            panel -> panel.setStartAndPauseButtonText("Start"));
        getSimulation().reset();
    }
    
    public void switchLoopState()
    {
        loop = !loop;
    }
    
    public boolean getLoopState()
    {
        return loop;
    }
    
    public void stopLooping()
    {
        loop = false;
    }
}
