package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.EscapeRouteSection;
import morris_water_maze.model.MouseMovement;


class MouseMovementPainter extends Painter<MouseMovement>
{
    private GraphicsAdapter
        graphics;
    
    
    @Override
    public void paint(GraphicsAdapter graphics, MouseMovement mouseMovement)
    {
        this.graphics = graphics;
        mouseMovement.forEachEscapeRouteSection(this::paintEscapeRouteSection);
    }
    
    private void paintEscapeRouteSection(EscapeRouteSection escapeRouteSection)
    {
        paintEntity(graphics, escapeRouteSection);
    }
}
