package MorrisWaterMaze.model;

import MorrisWaterMaze.control.observer.SimulationCompletionObserver;
import MorrisWaterMaze.control.observer.SimulationStepObserver;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class WaterMorrisMazeSimulation implements SettingModifier, Paintable
{
    private final Pool
        pool;
    
    private final Platform
        platform;
    
    private final MouseMovement
        mouseMovement;
    
    private int
        remainingNumberOfSimulations;
    
    private int
        totalNumberOfSimulations;
    
    private double
        lastSearchTime;
    
    private final List<Double>
        searchTimes = new ArrayList<>();
        
    private final List<SimulationCompletionObserver>
        simulationCompletionObservers = new ArrayList<>();
    
    private final List<SimulationStepObserver>
        simulationStepObservers = new ArrayList<>();
    
    
    public WaterMorrisMazeSimulation(SimulationParameterAccessor parameterAccessor)
    {
        pool = new Pool();
        platform = new Platform();
        mouseMovement = new MouseMovement(parameterAccessor, pool);
        totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }

    public void nextStep()
    {
        if(isSimulationInProgress())
        {
            mouseMovement.move(pool, platform);
        }
        else if(isNotFinished())
        {
            remainingNumberOfSimulations--;
            lastSearchTime = mouseMovement.getTotalDurationOfCurrentSimulation();
            searchTimes.add(lastSearchTime);
            if(isCurrentSimulationStepLastStep())
            {
                notifyAboutEndOfSimulation();
            }
            notifyAboutEndOfSimulationStep();
        }
    }
    
    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel) {
        mouseMovement.setTrainingLevel(mouseTrainingLevel);
    }
    
    @Override
    public void setRemainingAndTotalNumberOfSimulations(int numberOfSimulations) {
        totalNumberOfSimulations = numberOfSimulations;
        resetRemainingNumberOfSimulations();
    }
    
    @Override
    public void clearSearchTime()
    {
        searchTimes.clear();
    }
    
    @Override
    public void resetRemainingNumberOfSimulations() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }
    
    public boolean isNotFinished() {
        return !isCurrentSimulationStepLastStep();
    }
    
    public double calculateSumOfSearchTimes()
    {
        return searchTimes.stream()
                          .mapToDouble(Double::doubleValue)
                          .sum();
    }
    
    public void reset() {
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
    
    public void registerSimulationStepObserver(SimulationStepObserver simulationStepObserver)
    {
        simulationStepObservers.add(simulationStepObserver);
    }
    
    public void registerSimulationCompletionObserver(SimulationCompletionObserver simulationCompletionObserver)
    {
        simulationCompletionObservers.add(simulationCompletionObserver);
    }
    
    public int getNumberOfCompletedSimulations()
    {
        return totalNumberOfSimulations - remainingNumberOfSimulations;
    }
    
    public int getTotalNumberOfSimulations()
    {
        return totalNumberOfSimulations;
    }
    
    public double getLastSearchTime()
    {
        return lastSearchTime;
    }
    
    public List<Double> getSearchTimes()
    {
        return Collections.unmodifiableList(searchTimes);
    }
    
    private void notifyAboutEndOfSimulationStep()
    {
        simulationStepObservers.forEach(SimulationStepObserver::beNotifiedAboutEndOfLastSimulationStep);
    }
    
    private void notifyAboutEndOfSimulation()
    {
        simulationCompletionObservers.forEach(SimulationCompletionObserver::beNotifiedAboutEndOfSimulation);
    }
    
    private boolean isSimulationInProgress()
    {
        return mouseMovement.isSwimming();
    }
    
    private boolean isCurrentSimulationStepLastStep()
    {
        return remainingNumberOfSimulations <= 0;
    }
    

}
