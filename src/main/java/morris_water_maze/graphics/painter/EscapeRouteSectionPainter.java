package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.EscapeRouteSection;
import morris_water_maze.util.Calculations;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


final class EscapeRouteSectionPainter extends Painter<EscapeRouteSection>
{
    private final double
        DELIMITER_CIRCLE_RADIUS = 0.5;
    
    
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, EscapeRouteSection escapeRouteSection)
    {
        Line2D.Double escapeRouteSectionLine = getEscapeRouteSectionLine(escapeRouteSection);
        graphicsAdapter.setColor(Color.BLACK);
        graphicsAdapter.draw(escapeRouteSectionLine);
        paintCircleOnTopOfAPoint(graphicsAdapter, escapeRouteSection.getEnd().asPoint2D());
    }
    
    private Line2D.Double getEscapeRouteSectionLine(EscapeRouteSection escapeRouteSection)
    {
        return new Line2D.Double(
            Calculations.scalePoint(escapeRouteSection.getStart(), ZOOM_FACTOR)
                        .asPoint2D(),
            Calculations.scalePoint(escapeRouteSection.getEnd(), ZOOM_FACTOR)
                        .asPoint2D());
    }
    
    private void paintCircleOnTopOfAPoint(GraphicsAdapter graphicsAdapter, Point2D point)
    {
        graphicsAdapter.fillOval(
            (int)(ZOOM_FACTOR * (point.getX() - DELIMITER_CIRCLE_RADIUS)),
            (int)(ZOOM_FACTOR * (point.getY() - DELIMITER_CIRCLE_RADIUS)),
            (int)(ZOOM_FACTOR * DELIMITER_CIRCLE_RADIUS * 2),
            (int)(ZOOM_FACTOR * DELIMITER_CIRCLE_RADIUS * 2));
    }
}
