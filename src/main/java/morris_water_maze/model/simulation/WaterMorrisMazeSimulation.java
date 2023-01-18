package morris_water_maze.model.simulation;

import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.graphics.Paintable;
import morris_water_maze.model.Platform;
import morris_water_maze.model.Pool;
import morris_water_maze.model.mouse.Mouse;
import morris_water_maze.model.mouse.MouseMovement;
import morris_water_maze.model.mouse.MouseParameter;
import morris_water_maze.parameter.ParameterProvider;

import java.util.ArrayList;
import java.util.List;


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
    
    
    public WaterMorrisMazeSimulation(ParameterProvider parameterProvider)
    {
        super(parameterProvider.getSimulationParameterProvider());
        
        pool = new Pool();
        platform = new Platform();
        
        MouseParameter mouseParameter = parameterProvider.getMouseParameterProvider();
        Mouse mouse = new Mouse(mouseParameter, pool, platform);
        mouseMovement = new MouseMovement(mouseParameter, mouse);
    }
    
    @Override
    public void nextStep()
    {
        if(isSimulationRunInProgress())
        {
            mouseMovement.performNextStep();
        }
        else if(!areAllSimulationRunsCompleted())
        {
            completeCurrentSimulationRun();
            if(areAllSimulationRunsCompleted())
            {
                notifyAboutEndOfAllSimulationRuns();
            }
        }
    }
    
    private void completeCurrentSimulationRun()
    {
        double lastSearchTime = mouseMovement.getSumOfAllPreviousSimulationsSteps();
        searchTimeContainer.add(lastSearchTime);
        decrementRemainingNumberOfSimulationRuns();
        notifyAboutEndOfCurrentSimulationRun();
        reset();
    }
    
    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel)
    {
        mouseMovement.setMouseTrainingLevel(mouseTrainingLevel);
    }
    
    @Override
    public double getAverageSearchTime()
    {
        return searchTimeContainer.calculateSumOfSearchTimes() / getTotalNumberOfSimulationRuns();
    }
    
    @Override
    public double getLastSearchTime()
    {
        return searchTimeContainer.getLastSearchTime();
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
        return mouseMovement.isMouseSwimming();
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
