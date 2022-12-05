package morris_water_maze.graphics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ProtoTypeForHeliDefence extends Application
{
    private double
        x = 0;
    
    
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

        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                x++;
                
                GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
                
                graphicsContext2D.clearRect(0, 0, primaryStage.getWidth(), primaryStage.getHeight());
                
                graphicsContext2D.setFill(Color.GREEN);
                graphicsContext2D.fillRect(0,0,200, 200);
                graphicsContext2D.setFill(Color.GOLD);
                graphicsContext2D.fillOval(10+ x,10,50,50);
                
                graphicsContext2D.setFill(Color.BLACK);
                graphicsContext2D.setFont(Font.getDefault());
                graphicsContext2D.fillText("hello   world!", 15, 50);
    
                graphicsContext2D.setLineWidth(5);
                graphicsContext2D.setStroke(Color.PURPLE);
    
                graphicsContext2D.strokeOval(10, 60, 30, 30);
                graphicsContext2D.strokeOval(60, 60, 30, 30);
                graphicsContext2D.strokeRect(30, 100, 40, 40);
                
                graphicsContext2D.restore();
         
            }
        }.start();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}