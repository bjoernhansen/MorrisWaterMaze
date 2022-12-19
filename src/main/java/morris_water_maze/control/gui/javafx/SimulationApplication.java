package morris_water_maze.control.gui.javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.graphics.adapter.JavaFxAdapter;
import morris_water_maze.graphics.painter.PaintManager;
import morris_water_maze.model.simulation.Simulation;


public final class SimulationApplication extends Application
{
    public static Simulation simulation;
    
    @Override
    public void start(final Stage primaryStage)
    {
        Canvas canvas = new Canvas (1024, 768);
        Scene scene = new Scene(new Pane(canvas));
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //primaryStage.setFullScreen(true);
        //primaryStage.setResizable(false);
        //primaryStage.setOpacity(0.5);
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        GraphicsAdapter graphicsAdapter = JavaFxAdapter.of(graphicsContext2D);
        
        
        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                simulation.nextStep();
    
                
    
                
                /*
                super.paintComponent(graphicsContext2D);
                
                imagePainter.initializeImage();
                imagePainter.paint(simulation);
                Image image = imagePainter.getImage();
                graphicsAdapter.drawImage(image, SimulationFrame.IMAGE_BORDER_DISTANCE, SimulationFrame.IMAGE_BORDER_DISTANCE, null);
                lastPainted = System.currentTimeMillis();
                */
                
              
                
                graphicsContext2D.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());
    
    
                PaintManager.getInstance().paint(graphicsAdapter, simulation);
           
                
                graphicsContext2D.restore();
         
            }
        }.start();
    }
}