package morris_water_maze.graphics.adapter;

import morris_water_maze.graphics.Color;
import morris_water_maze.util.geometry.Point;

import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;


public interface GraphicsAdapter
{
    void setColor(Color color);
    
    void drawLine(Line2D line);
   
    void drawEllipse(Ellipse2D ellipse);
    
    void fillCircleOnTopOfAPoint(Point center, double radius);

    void fillRect(Rectangle2D rectangle);
    
    void drawImage(Image img, int x, int y, ImageObserver observer);

    String getSvgString();
}
