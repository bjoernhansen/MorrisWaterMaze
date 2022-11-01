package MorrisWaterMaze;

import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Simulation implements SettingModifier, Paintable
{
    private int remainingNumberOfSimulations;
    
    
    private final ArrayList<Double>
        searchTimes = new ArrayList<>();
    
    private int totalNumberOfSimulations;

    private final MouseMovement mouseMovement;

    private final Pool pool;

    private final Platform platform;


    public Simulation(SimulationParameterAccessor parameterAccessor)
    {
        this.mouseMovement = new MouseMovement(parameterAccessor);
        this.pool = new Pool();
        this.platform = new Platform();

        totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }


    void nextStep(SimulationController simulationController)
    {
        if(isSimulationInProgress())
        {
            double maximumDurationOfNextSimulationStep = calculateRandomSimulationStepDuration();
            mouseMovement.move(pool, platform, maximumDurationOfNextSimulationStep);
        }
        else if(isAnotherSimulationToBeStarted())
        {
            double lastSearchTime = mouseMovement.getTotalDurationOfCurrentSimulation();
            searchTimes.add(lastSearchTime);
            
            System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);
    
            if(	simulationController.numberOfPics > 0 && lastSearchTime >= simulationController.picTimeFrameLowerBound && lastSearchTime <= simulationController.picTimeFrameUpperBound)
            {
                simulationController.saveImage();
            }
    
            if(remainingNumberOfSimulations == 1)
            {
                double sumOfSearchTimes = calculateSumOfSearchTimes();
                System.out.println("\nDurchschnittliche Suchzeit: " + (sumOfSearchTimes/totalNumberOfSimulations) + "\n");
        
                BufferedWriter bw;
                String fileName = simulationController.getFileName();
                String fileNameTemp = SimulationController.LOG_DIRECTORY_NAME + fileName + "/" + fileName + ".txt";
                System.out.println("Schreibe Datei: " + fileNameTemp);
                try
                {
                    bw = new BufferedWriter(new FileWriter(fileNameTemp));
                    for (Double aDouble : searchTimes)
                    {
                        bw.write(aDouble + System.getProperty("line.separator"));
                    }
                    bw.close();
                }
                catch (IOException ioe)
                {
                    System.out.println("caught error: " + ioe);
                }
            }
            simulationController.reset();
            remainingNumberOfSimulations--;
        }
        
    }
    
    private double calculateSumOfSearchTimes()
    {
        return searchTimes.stream()
                          .mapToDouble(Double::doubleValue)
                          .sum();
    }
    
    private double calculateRandomSimulationStepDuration()
    {
        return Calculations.calculateRandomDuration(mouseMovement.stepLengthBias);
    }
    
    private boolean isSimulationInProgress()
    {
        return mouseMovement.isSwimming();
    }

    public void determineMouseStartingPosition() {
        mouseMovement.determineStartingPosition(pool);
    }

    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel) {
        mouseMovement.setTrainingLevel(mouseTrainingLevel);
    }

    public boolean isAnotherSimulationToBeStarted() {
        return remainingNumberOfSimulations > 0;
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
}
