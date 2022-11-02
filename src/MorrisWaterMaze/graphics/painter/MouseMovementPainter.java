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
            graphicsAdapter.setColor(LIGHT_GREY);
            paintCircleOnTopOfAPoint(mouseMovement.getCurrentPosition(), MouseMovement.RADIUS);
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
        paintCircleOnTopOfAPoint(sectionEnd, 0.5);
    }
    
    
    private void paintCircleOnTopOfAPoint(Point point, double radius)
    {
        double zoomFactor = SimulationController.ZOOM_FACTOR;
        graphicsAdapter.fillOval(
            (int)(zoomFactor * (point.getX() - radius)),
            (int)(zoomFactor * (point.getY() - radius)),
            (int)(zoomFactor * radius * 2),
            (int)(zoomFactor * radius * 2));
    }
}
