package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.control.observer.SimulationSeriesCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationRunCompletionObserver;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;

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
        
    private final SearchTimeContainer
        searchTimeContainer = new SearchTimeContainer();
    
    private final List<SimulationSeriesCompletionObserver>
        simulationSeriesCompletionObservers = new ArrayList<>();
    
    private final List<SimulationRunCompletionObserver>
        simulationRunCompletionObservers = new ArrayList<>();
    
    
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
        if (isSimulationRunInProgress())
        {
            mouseMovement.move(pool, platform);
        }
        else if (!areAllSimulationRunsCompleted())
        {
            completeCurrentSimulationsRun();
            if (areAllSimulationRunsCompleted())
            {
                notifyAboutEndOfAllSimulationRuns();
            }
        }
    }
    
    private void completeCurrentSimulationsRun()
    {
        double lastSearchTime = mouseMovement.getTotalDurationOfCurrentSimulationRun();
        searchTimeContainer.add(lastSearchTime);
        decrementRemainingNumberOfSimulationRuns();
        notifyAboutEndOfCurrentSimulationRun();
    }
    
    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel)
    {
        mouseMovement.setTrainingLevel(mouseTrainingLevel);
    }
    
    @Override
    public double getAverageSearchTime()
    {
        return searchTimeContainer.calculateSumOfSearchTimes() / getTotalNumberOfSimulationRuns();
    }
    
    @Override
    public void clearSearchTime()
    {
        searchTimeContainer.clear();
    }
    
    @Override
    public SearchTimeProvider getSearchTimeProvider()
    {
        return searchTimeContainer;
    }
    
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
    public void registerSimulationStepObservers(SimulationRunCompletionObserver simulationRunCompletionObserver)
    {
        simulationRunCompletionObservers.add(simulationRunCompletionObserver);
        simulationRunCompletionObserver.setSimulation(this);
    }
    
    @Override
    public void registerSimulationSeriesCompletionObservers(SimulationSeriesCompletionObserver simulationSeriesCompletionObserver)
    {
        simulationSeriesCompletionObservers.add(simulationSeriesCompletionObserver);
        simulationSeriesCompletionObserver.setSimulation(this);
    }
    
    private boolean isSimulationRunInProgress()
    {
        return mouseMovement.isSwimming();
    }
    
    private void notifyAboutEndOfCurrentSimulationRun()
    {
        simulationRunCompletionObservers.forEach(SimulationRunCompletionObserver::beNotifiedAboutCompletionOfCurrentSimulationRun);
    }
    
    private void notifyAboutEndOfAllSimulationRuns()
    {
        simulationSeriesCompletionObservers.forEach(SimulationSeriesCompletionObserver::beNotifiedAboutEndOfAllSimulations);
    }
}
