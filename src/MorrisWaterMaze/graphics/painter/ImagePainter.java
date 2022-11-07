package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.Image;


public interface ImagePainter
{
    void paint(Paintable paintableEntity);
    
    Image getImage();
    
    void setBackground(Paintable paintable);
    
    void initializeImage();
    
    ImagePainter makeCopy();
}
