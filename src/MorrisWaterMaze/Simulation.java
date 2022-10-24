package MorrisWaterMaze;

import MorrisWaterMaze.model.Mouse;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Simulation implements SettingModifier
{
    private int remainingNumberOfSimulations;

    private int totalNumberOfSimulations;

    private final Mouse mouse;

    private final Pool pool;

    private final Platform platform;


    public Simulation(SimulationParameterAccessor parameterAccessor)
    {
        this.mouse = new Mouse(parameterAccessor);
        this.pool = new Pool();
        this.platform = new Platform();

        totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }


    void nextStep()
    {
        if(isSimulationInProgress())
        {
            double maximumDurationOfNextSimulationStep = calculateRandomSimulationStepDuration();
            mouse.move(pool, platform, maximumDurationOfNextSimulationStep);
        }
        else
        {
            if(isAnotherSimulationToBeStarted())
            {
                double lastSearchTime = mouse.getTotalDurationOfCurrentSimulation();
                Controller.searchTime.add(lastSearchTime);
                Controller.sumOfSearchTime += lastSearchTime;
        
        
                System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);
        
                if(	Controller.numberOfPics > 0 && lastSearchTime >= Controller.picTimeFrameLowerBound && lastSearchTime <= Controller.picTimeFrameUpperBound)
                {
                    Controller.saveImage();
                }
        
                if(remainingNumberOfSimulations == 1)
                {
                    System.out.println("\nDurchschnittliche Suchzeit: " + (Controller.sumOfSearchTime / totalNumberOfSimulations) + "\n");
            
                    BufferedWriter bw;
                    String file_name_temp = Controller.LOG_DIRECTORY_NAME + Controller.fileName + "/" + Controller.fileName + ".txt";
                    System.out.println("Schreibe Datei: " + file_name_temp);
                    try
                    {
                        bw = new BufferedWriter(new FileWriter(file_name_temp));
                        for (Double aDouble : Controller.searchTime)
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
                Controller.reset();
                remainingNumberOfSimulations--;
            }
        }
    }
    
    private double calculateRandomSimulationStepDuration()
    {
        return Calculations.calculateRandomDuration(mouse.stepLengthBias);
    }
    
    private boolean isSimulationInProgress()
    {
        return mouse.isSwimming();
    }
    
    
    public double getMouseTrainingLevel()
    {
        return mouse.getTrainingLevel();
    }

    public void paintMouseTrajectory(Graphics2D offGraphics)
    {
        mouse.paintTrajectory(offGraphics);
    }

    public void determineMouseStartingPosition() {
        mouse.determineStartingPosition(pool);
    }

    @Override
    public void setMouseTrainingLevel(double mouseTrainingLevel) {
        mouse.setTrainingLevel(mouseTrainingLevel);
    }

    public void paintPool(Graphics2D offGraphics) {
        pool.paint(offGraphics, Controller.AFFINE_TRANSFORMATION);
    }

    public Shape getPlatformBounds() {
        return platform.getBounds();
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
    public void resetRemainingNumberOfSimulations() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }

    public int getTotalNumberOfSimulations() {
        return totalNumberOfSimulations;
    }
}
