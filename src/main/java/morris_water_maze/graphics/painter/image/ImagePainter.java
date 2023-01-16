package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.Paintable;

import java.awt.Image;


public interface ImagePainter
{
    void paint(Paintable paintableEntity);
    
    Image getImage();
    
    void setBackground(Paintable paintable);
    
    void initializeImage();
    
    String getSvgString();
}
