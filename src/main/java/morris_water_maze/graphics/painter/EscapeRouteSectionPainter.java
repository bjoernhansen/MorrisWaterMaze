package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.EscapeRouteSection;
import morris_water_maze.util.Point;

import static morris_water_maze.Main.ZOOM_FACTOR;


final class EscapeRouteSectionPainter extends Painter<EscapeRouteSection>
{
    private final double
        DELIMITER_CIRCLE_RADIUS = 0.5;
        
    
    @Override
    public void paint(GraphicsAdapter graphics, EscapeRouteSection escapeRouteSection)
    {
        paintEscapeRouteSectionLine(graphics, escapeRouteSection);        
        paintCircleOnTopOfAPoint(graphics, escapeRouteSection.getEnd());
    }
    
    private void paintEscapeRouteSectionLine(GraphicsAdapter graphics, EscapeRouteSection escapeRouteSection)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(escapeRouteSection.getLine());
    }
    
    private void paintCircleOnTopOfAPoint(GraphicsAdapter graphics, Point point)
    {
        graphics.fillOval(
            (int)((point.getX() - DELIMITER_CIRCLE_RADIUS)),
            (int)((point.getY() - DELIMITER_CIRCLE_RADIUS)),
            (int)(DELIMITER_CIRCLE_RADIUS * 2.0),
            (int)(DELIMITER_CIRCLE_RADIUS * 2.0));
        
        graphics.setColor(Color.of(java.awt.Color.RED));
        graphics.drawPoint(point);
    }
}
