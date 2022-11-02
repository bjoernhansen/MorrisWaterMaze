package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.Calculations;
import MorrisWaterMaze.SimulationController;
import MorrisWaterMaze.model.EscapeRouteSection;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.util.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


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
            double zoomFactor = SimulationController.ZOOM_FACTOR;
            double radius = MouseMovement.RADIUS;
            Point currentMousePosition = mouseMovement.getCurrentPosition();
            
            graphicsAdapter.setColor(LIGHT_GREY);
            graphicsAdapter.fillOval(
                (int)(zoomFactor * currentMousePosition.getX()-radius),
                (int)(zoomFactor * currentMousePosition.getY()-radius),
                (int)(zoomFactor * radius * 2),
                (int)(zoomFactor * radius * 2));
        }
        
    }
    
    private void paintEscapeRouteSection(EscapeRouteSection escapeRouteSection)
    {
        double zoomFactor = SimulationController.ZOOM_FACTOR;
        Point sectionStart = escapeRouteSection.getStart();
        Point sectionEnd = escapeRouteSection.getEnd();
        
        Line2D.Double escapeRouteSectionLine = new Line2D.Double(
            Calculations.scalePoint(sectionStart, zoomFactor).asPoint2D(),
            Calculations.scalePoint(sectionEnd, zoomFactor).asPoint2D());
        
        graphicsAdapter.setColor(Color.BLACK);
        graphicsAdapter.draw(escapeRouteSectionLine);
        
        graphicsAdapter.fillOval(
            (int) (zoomFactor * (sectionEnd.getX() - 0.5)),
            (int) (zoomFactor * (sectionEnd.getY() - 0.5)),
            (int)  zoomFactor,
            (int)  zoomFactor);
    }
}
