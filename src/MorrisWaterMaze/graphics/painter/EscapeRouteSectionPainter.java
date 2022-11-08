package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.EscapeRouteSection;
import MorrisWaterMaze.util.Calculations;

import java.awt.Color;
import java.awt.geom.Line2D;


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
        graphicsAdapter.paintCircleOnTopOfAPoint(escapeRouteSection.getEnd().asPoint2D(), DELIMITER_CIRCLE_RADIUS, ZOOM_FACTOR);
    }
    
    private Line2D.Double getEscapeRouteSectionLine(EscapeRouteSection escapeRouteSection)
    {
        return new Line2D.Double(
            Calculations.scalePoint(escapeRouteSection.getStart(), ZOOM_FACTOR)
                        .asPoint2D(),
            Calculations.scalePoint(escapeRouteSection.getEnd(), ZOOM_FACTOR)
                        .asPoint2D());
    }
}
