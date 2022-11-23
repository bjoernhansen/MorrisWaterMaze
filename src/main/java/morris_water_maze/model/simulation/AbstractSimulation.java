package morris_water_maze.model.simulation;

import morris_water_maze.parameter.SimulationParameterAccessor;


abstract class AbstractSimulation implements Simulation
{
    private int
        remainingNumberOfSimulations;
    
    private int
        totalNumberOfSimulations;
    
    
    AbstractSimulation(SimulationParameterAccessor parameterAccessor)
    {
        int numberOfSimulations = parameterAccessor.getNumberOfSimulations();
        setRemainingAndTotalNumberOfSimulationRuns(numberOfSimulations);
    }
    
    @Override
    public final void setRemainingAndTotalNumberOfSimulationRuns(int numberOfSimulations) {
        totalNumberOfSimulations = numberOfSimulations;
        resetRemainingNumberOfSimulationRuns();
    }
    
    @Override
    public void resetRemainingNumberOfSimulationRuns() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }
    
    @Override
    public int getNumberOfCompletedSimulationRuns()
    {
        return totalNumberOfSimulations - remainingNumberOfSimulations;
    }
    
    @Override
    public int getTotalNumberOfSimulationRuns()
    {
        return totalNumberOfSimulations;
    }
    
    void decrementRemainingNumberOfSimulationRuns()
    {
        remainingNumberOfSimulations--;
    }
    
    @Override
    public boolean areAllSimulationRunsCompleted()
    {
        return remainingNumberOfSimulations <= 0;
    }
}
