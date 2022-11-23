package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.EscapeRouteSection;
import morris_water_maze.model.MouseMovement;


class MouseMovementPainter extends Painter<MouseMovement>
{
    private GraphicsAdapter
        graphicsAdapter;
    
    
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, MouseMovement mouseMovement)
    {
        this.graphicsAdapter = graphicsAdapter;
        mouseMovement.forEachEscapeRouteSection(this::paintEscapeRouteSection);
    }
    
    private void paintEscapeRouteSection(EscapeRouteSection escapeRouteSection)
    {
        paintEntity(graphicsAdapter, escapeRouteSection);
    }
}
