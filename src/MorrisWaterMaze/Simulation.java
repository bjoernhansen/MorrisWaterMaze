package MorrisWaterMaze;

import MorrisWaterMaze.model.Mouse;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.SimulationParameterAccessor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Simulation implements SettingModifier
{
    static final AffineTransform
        affineTransformation = new AffineTransform(Controller.ZOOM_FACTOR, 0, 0, Controller.ZOOM_FACTOR, 0, 0);
    
    private static final Color
        DARK_GREY = new Color(75, 75, 75);
    
    
    private int remainingNumberOfSimulations;
    
    
    private final ArrayList<Double>
        searchTimes = new ArrayList<>();
    
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


    void nextStep(Controller controller)
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
                searchTimes.add(lastSearchTime);
                
                System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);
        
                if(	controller.numberOfPics > 0 && lastSearchTime >= controller.picTimeFrameLowerBound && lastSearchTime <= controller.picTimeFrameUpperBound)
                {
                    controller.saveImage();
                }
        
                if(remainingNumberOfSimulations == 1)
                {
                    double sumOfSearchTimes = calculateSumOfSearchTimes();
                    System.out.println("\nDurchschnittliche Suchzeit: " + (sumOfSearchTimes/totalNumberOfSimulations) + "\n");
            
                    BufferedWriter bw;
                    String fileName = Controller.getInstance().getFileName();
                    String fileNameTemp = Controller.LOG_DIRECTORY_NAME + fileName + "/" + fileName + ".txt";
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
                controller.reset();
                remainingNumberOfSimulations--;
            }
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
        pool.paint(offGraphics, affineTransformation);
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
    public void clearSearchTime()
    {
        searchTimes.clear();
    }
  
    @Override
    public void resetRemainingNumberOfSimulations() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }
    
    public void paintPlatform(Graphics2D g2d)
    {
        g2d.setColor(DARK_GREY);
        g2d.fill(affineTransformation.createTransformedShape(getPlatformBounds()));
        g2d.setColor(Color.black);
        
        int y;
        int x = y = (int)(Controller.ZOOM_FACTOR * (Pool.CENTER_TO_BORDER_DISTANCE - 1));
        int size = (int) (2.0 * Controller.ZOOM_FACTOR);
        
        g2d.fillOval(x, y, size, size);
    }
    
    public void paintBackground(Graphics2D offGraphics)
    {
        offGraphics.setColor(Color.white);
        offGraphics.fillRect(0, 0, (int) Controller.ZOOM_FACTOR * Controller.IMAGE_SIZE, (int) Controller.ZOOM_FACTOR * Controller.IMAGE_SIZE);
    }
}
