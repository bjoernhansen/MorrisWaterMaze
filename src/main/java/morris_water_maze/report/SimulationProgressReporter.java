package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationRunCompletionObserver;
import morris_water_maze.model.simulation.Simulation;


public final class SimulationProgressReporter implements SimulationRunCompletionObserver
{
    private Simulation
        simulation;
    
    
    @Override
    public void beNotifiedAboutCompletionOfCurrentSimulationRun()
    {
        System.out.println(getSimulationRunCompletionMessage());
    }
    
    private String getSimulationRunCompletionMessage()
    {
        return  "Simulation " + simulation.getNumberOfCompletedSimulationRuns()
              + " of " + simulation.getTotalNumberOfSimulationRuns()
              + ", simulation time: " + simulation.getLastSearchTime();
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
}
