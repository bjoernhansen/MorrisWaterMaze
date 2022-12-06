package morris_water_maze.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.ImageObserver;


public interface GraphicsAdapter
{
    void setColor(Color c);
    
    void draw(Shape s);
    
    void fillOval(int x, int y, int width, int height);
    
    void fillRect(int x, int y, int width, int height);
    
    void drawImage(Image img, int x, int y, ImageObserver observer);
    
    void fill(Shape s);
   
    void turnAntialiasingOn();
    
    String getSvgString();
}
