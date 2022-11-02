package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.model.EscapeRouteSection;
import MorrisWaterMaze.model.MouseMovement;

import java.awt.Color;
import java.awt.Graphics2D;


class MouseMovementPainter extends Painter<MouseMovement>
{
    private static final Color
        LIGHT_GREY = new Color(150, 150, 150);
    
    private Graphics2D
        graphicsAdapter;
    
    
    @Override
    public void paint(Graphics2D graphicsAdapter, MouseMovement mouseMovement)
    {
        this.graphicsAdapter = graphicsAdapter;
        mouseMovement.forEachEscapeRouteSection(this::paintEscapeRouteSection);
        
        if(mouseMovement.isBodyToBeDrawn())
        {
            graphicsAdapter.setColor(LIGHT_GREY);
            paintCircleOnTopOfAPoint(graphicsAdapter, mouseMovement.getCurrentPosition(), MouseMovement.RADIUS);
        }
    }
    
    private void paintEscapeRouteSection(EscapeRouteSection escapeRouteSection)
    {
        paintEntity(graphicsAdapter, escapeRouteSection);
    }
}
