package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.Image;


public interface ImagePainter
{
    Image paintImageOf(Paintable paintableEntity);
}