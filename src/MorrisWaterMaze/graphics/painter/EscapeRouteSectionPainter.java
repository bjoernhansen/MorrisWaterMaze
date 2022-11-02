package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.model.EscapeRouteSection;
import MorrisWaterMaze.util.Calculations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class EscapeRouteSectionPainter extends Painter<EscapeRouteSection>
{
    private final double
        DELIMITER_CIRCLE_RADIUS = 0.5;
    
    
    @Override
    public void paint(Graphics2D graphicsAdapter, EscapeRouteSection escapeRouteSection)
    {
        Line2D.Double escapeRouteSectionLine = getEscapeRouteSectionLine(escapeRouteSection);
        graphicsAdapter.setColor(Color.BLACK);
        graphicsAdapter.draw(escapeRouteSectionLine);
        paintCircleOnTopOfAPoint(graphicsAdapter, escapeRouteSection.getEnd(), DELIMITER_CIRCLE_RADIUS);
    }
    
    private Line2D.Double getEscapeRouteSectionLine(EscapeRouteSection escapeRouteSection)
    {
        return new Line2D.Double(
            Calculations.scalePoint(escapeRouteSection.getStart(), SimulationController.ZOOM_FACTOR)
                        .asPoint2D(),
            Calculations.scalePoint(escapeRouteSection.getEnd(), SimulationController.ZOOM_FACTOR)
                        .asPoint2D());
    }
}
