package MorrisWaterMaze.model;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;
import MorrisWaterMaze.util.Calculations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public final class Simulation implements SettingModifier, Paintable
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
    
    private final ArrayList<Double>
        searchTimes = new ArrayList<>();
    



    public Simulation(SimulationParameterAccessor parameterAccessor)
    {
        pool = new Pool();
        platform = new Platform();
        mouseMovement = new MouseMovement(parameterAccessor, pool);
    
        totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }

    public void nextStep(SimulationController simulationController)
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
            
            // TODO Funktionsneid
            if(	simulationController.isAnotherPictureToBePainted() && simulationController.isSearchTimesWithinSpecifiedTimeFrame(lastSearchTime))
            {
                simulationController.saveImage();
            }
    
            remainingNumberOfSimulations--;
            System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);
    
            if(remainingNumberOfSimulations == 0)
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

    public void reset() {
        mouseMovement.resetForNextEscapeRun();
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
