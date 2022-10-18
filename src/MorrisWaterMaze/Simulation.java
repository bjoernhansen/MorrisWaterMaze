package MorrisWaterMaze;

import MorrisWaterMaze.model.Mouse;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Simulation
{
    private int remainingNumberOfSimulations;

    private int totalNumberOfSimulations;

    private final Mouse mouse;

    private final Pool pool;

    private final Platform platform;




    public Simulation(ParameterAccessor parameterAccessor)
    {
        this.mouse = new Mouse(parameterAccessor);
        this.pool = new Pool();
        this.platform = new Platform();

        totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }


    void nextStep()
    {
        if(mouse.isSwimming())
        {
            double timeStep = Math.log(mouse.stepLengthBias / Calculations.nonZeroRandom());

            mouse.move(pool, platform, timeStep);
        }
        else if (remainingNumberOfSimulations >= 1)
        {
            double lastSearchTime = mouse.timeSteps.get(mouse.timeSteps.size()-1);
            Controller.searchTime.add(lastSearchTime);
            Controller.sumOfSearchTime += lastSearchTime;

            remainingNumberOfSimulations--;
            System.out.println("Simulation " + (totalNumberOfSimulations - remainingNumberOfSimulations) + " of " + totalNumberOfSimulations + ", simulation time: " + lastSearchTime);

            if(	Controller.numberOfPics > 0 && lastSearchTime >= Controller.picTimeFrameLowerBound && lastSearchTime <= Controller.picTimeFrameUpperBound)
            {
                saveImage();
            }

            if(remainingNumberOfSimulations == 0)
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
        }
    }

    public static void saveImage()
    {
        if(!Controller.isStartingWithGui){
            Controller.currentNrOfPicInSeries++;}
        Controller.drawOffImage();
        if(Controller.isStartingWithGui || Controller.currentNrOfPicInSeries == Controller.maxNrOfPicInSeries)
        {
            try
            {
                String fileNameTemp = Controller.LOG_DIRECTORY_NAME + Controller.fileName + "/" + System.currentTimeMillis() + ".png";
                System.out.println("\nSchreibe Datei: " + fileNameTemp + "\n");
                ImageIO.write((RenderedImage)Controller.offImage, "png", new File(fileNameTemp));
                Controller.numberOfPics--;
            }
            catch(Exception exception){System.out.println(exception);}
        }
        if(!Controller.isStartingWithGui && Controller.currentNrOfPicInSeries == Controller.maxNrOfPicInSeries)
        {
            Controller.currentNrOfPicInSeries = 0;
        }
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

    public void setMouseTrainingLevel(double mouseTrainingLevel) {
        mouse.setTrainingLevel(mouseTrainingLevel);
    }

    public void paintPool(Graphics2D offGraphics) {
        pool.paint(offGraphics, Controller.AFFINE_TRANSFORMATION);
    }

    public Shape getPlatformBounds() {
        return platform.getBounds();
    }

    public boolean isNotFinished() {
        return remainingNumberOfSimulations >= 1;
    }

    public void setRemainingAndTotalNumberOfSimulations(int numberOfSimulations) {
        totalNumberOfSimulations = numberOfSimulations;
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }

    public void resetRemainingNumberOfSimulations() {
        remainingNumberOfSimulations = totalNumberOfSimulations;
    }

    public int getTotalNumberOfSimulations() {
        return totalNumberOfSimulations;
    }
}
