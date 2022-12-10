package morris_water_maze.graphics.adapter;

import morris_water_maze.graphics.Color;
import morris_water_maze.util.Point;

import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.ImageObserver;


public interface GraphicsAdapter
{
    void setColor(Color color);
    
    void drawLine(Line2D line);
    
    void drawOval(Ellipse2D ellipse);
    
    void fillOval(int x, int y, int width, int height);
    
    void fillRect(int x, int y, int width, int height);
    
    void drawImage(Image img, int x, int y, ImageObserver observer);

    String getSvgString();
    
    void drawPoint(Point point);
}
