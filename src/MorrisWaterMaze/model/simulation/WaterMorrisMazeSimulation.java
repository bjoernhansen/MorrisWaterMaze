package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.control.observer.SimulationCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;
import MorrisWaterMaze.util.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public final class WaterMorrisMazeSimulation extends AbstractSimulation
{
    private final Pool
        pool;
    
    private final Platform
        platform;
    
    private final MouseMovement
        mouseMovement;
    
    private final Stack<Double>
        searchTimes = new Stack<>();
    
    private final List<SimulationCompletionObserver>
        simulationCompletionObservers = new ArrayList<>();
    
    private final List<SimulationStepObserver>
        simulationStepObservers = new ArrayList<>();
    
    
    public WaterMorrisMazeSimulation(SimulationParameterAccessor parameterAccessor)
    {
        super(parameterAccessor);
        
        pool = new Pool();
        platform = new Platform();
        mouseMovement = new MouseMovement(parameterAccessor, pool);
    }
    
    @Override
    public void nextStep()
    {
        if (isSimulationInProgress())
        {
            mouseMovement.move(pool, platform);
        } else if (isNotFinished())
        {
            decrementRemainingNumberOfSimulations();
            double lastSearchTime = mouseMovement.getTotalDurationOfCurrentSimulation();
            searchTimes.push(lastSearchTime);
            notifyAboutEndOfSimulationStep();
            if (isCurrentSimulationStepLastStep())
            {
                notifyAboutEndOfSimulation();
            }
        }
    }
    
    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel)
    {
        mouseMovement.setTrainingLevel(mouseTrainingLevel);
    }
    

    @Override
    public boolean isNotFinished()
    {
        return !isCurrentSimulationStepLastStep();
    }

    
    /** searchTimes start **/
    // TODO evtl. eigene Klasse einführen, könnten die zugreifenden Klassen vielleicht eine Instanz dieser Klasse (immutable!) bekommen?
    
    @Override
    public double getLastSearchTime()
    {
        return searchTimes.peek();
    }
    
    @Override
    public double getAverageSearchTime()
    {
        return calculateSumOfSearchTimes()/getTotalNumberOfSimulations();
    }
    
    @Override
    public void forEachSearchTime(Consumer<String> action)
    {
        searchTimes.stream()
                   .map(String::valueOf)
                   .forEach(action);
    }
    
    private double calculateSumOfSearchTimes()
    {
        return searchTimes.stream()
                          .mapToDouble(Double::doubleValue)
                          .sum();
    }
    
    @Override
    public void clearSearchTime()
    {
        searchTimes.clear();
    }
    
    /** searchTimes end **/
    
    
    @Override
    public void reset()
    {
        mouseMovement.resetForNextEscapeRun();
    }
    
    public Paintable getPool()
    {
        return pool;
    }
    
    public Paintable getPlatform()
    {
        return platform;
    }
    
    public Paintable getMouseMovement()
    {
        return mouseMovement;
    }
    
    @Override
    public void registerSimulationStepObservers(SimulationStepObserver simulationStepObserver)
    {
        simulationStepObservers.add(simulationStepObserver);
        simulationStepObserver.setSimulation(this);
    }
    
    @Override
    public void registerSimulationCompletionObservers(SimulationCompletionObserver simulationCompletionObserver)
    {
        simulationCompletionObservers.add(simulationCompletionObserver);
        simulationCompletionObserver.setSimulation(this);
    }
    
    private boolean isSimulationInProgress()
    {
        return mouseMovement.isSwimming();
    }
    
    
    private void notifyAboutEndOfSimulationStep()
    {
        simulationStepObservers.forEach(SimulationStepObserver::beNotifiedAboutEndOfLastSimulationStep);
    }
    
    private void notifyAboutEndOfSimulation()
    {
        simulationCompletionObservers.forEach(SimulationCompletionObserver::beNotifiedAboutEndOfSimulation);
    }
}
