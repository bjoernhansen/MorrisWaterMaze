package MorrisWaterMaze.model.simulation;

import MorrisWaterMaze.parameter.SimulationParameterAccessor;


abstract class AbstractSimulation implements Simulation
{
    private int
        remainingNumberOfSimulations;
    
    private int
        totalNumberOfSimulations;
    
    
    AbstractSimulation(SimulationParameterAccessor parameterAccessor)
    {
        int numberOfSimulations = parameterAccessor.getNumberOfSimulations();
        setRemainingAndTotalNumberOfSimulations(numberOfSimulations);
    }
    
    @Override
    public final void setRemainingAndTotalNumberOfSimulations(int numberOfSimulations) {
        totalNumberOfSimulations = numberOfSimulations;
        resetRemainingNumberOfSimulations();
    }
    
    @Override
    public void resetRemainingNumberOfSimulations() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }
    
    @Override
    public int getNumberOfCompletedSimulations()
    {
        return totalNumberOfSimulations - remainingNumberOfSimulations;
    }
    
    @Override
    public int getTotalNumberOfSimulations()
    {
        return totalNumberOfSimulations;
    }
    
    void decrementRemainingNumberOfSimulations()
    {
        remainingNumberOfSimulations--;
    }
    
    boolean isCurrentSimulationStepLastStep()
    {
        return remainingNumberOfSimulations <= 0;
    }
}
