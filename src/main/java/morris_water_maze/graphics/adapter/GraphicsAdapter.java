package morris_water_maze.graphics.adapter;

import morris_water_maze.graphics.Color;
import morris_water_maze.util.geometry.Line;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;

import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;


public interface GraphicsAdapter
{
    void setColor(Color color);
    
    void drawLine(Line line);
   
    void drawEllipse(Ellipse2D ellipse);
    
    void fillCircleOnTopOfAPoint(Point center, double radius);

    void fillSquare(Square square);
    
    void drawImage(Image img, int x, int y, ImageObserver observer);

    String getSvgString();
}
