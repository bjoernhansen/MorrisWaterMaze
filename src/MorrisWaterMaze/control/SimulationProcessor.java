package MorrisWaterMaze.control;

import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;

public class SimulationProcessor
{
    private final ParameterAccessor
        parameterAccessor;
    private final Simulation
        simulation;
    
    
    public SimulationProcessor(ParameterAccessor parameterAccessor)
    {
        this.parameterAccessor = parameterAccessor;
        this.simulation = new Simulation(parameterAccessor);
    }
    
    public void run()
    {
        if(parameterAccessor.isStartingWithGui())
        {
            new SimulationControllerWithGui(simulation, parameterAccessor);
        }
        else
        {
            new BackgroundSimulationController(simulation, parameterAccessor);
        }
    }
}
