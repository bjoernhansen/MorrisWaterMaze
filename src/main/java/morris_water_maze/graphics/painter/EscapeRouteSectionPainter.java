package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.mouse.EscapeRouteSection;


final class EscapeRouteSectionPainter extends Painter<EscapeRouteSection>
{
    private final double
        DELIMITER_CIRCLE_RADIUS = 0.5;
        
    
    @Override
    public void paint(GraphicsAdapter graphics, EscapeRouteSection escapeRouteSection)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(escapeRouteSection.getLine());
        graphics.fillCircleOnTopOfAPoint(escapeRouteSection.getEnd(), DELIMITER_CIRCLE_RADIUS);
    }
}
