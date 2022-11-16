package MorrisWaterMaze.model;

import MorrisWaterMaze.util.Point;
import MorrisWaterMaze.util.Stack;

import java.util.Optional;
import java.util.function.Consumer;


final class EscapeRoute
{
    private final Stack<EscapeRouteSection>
        escapeRouteSections = new Stack<>();
    
    private final Point
        startPosition;
        
    
    EscapeRoute(Point startPosition)
    {
        this.startPosition = startPosition;
    }
    
    public void addNextSectionToward(Point nextPosition)
    {
        Point previousPosition = getLastPosition();
        EscapeRouteSection nextSection = new EscapeRouteSection(previousPosition, nextPosition);
        escapeRouteSections.push(nextSection);
    }
    
    public Point getLastPosition()
    {
        return Optional.ofNullable(escapeRouteSections.peek())
                       .map(EscapeRouteSection::getEnd)
                       .orElse(startPosition);
    }
    
    public void reset()
    {
        escapeRouteSections.clear();
    }
    
    public void forEachSection(Consumer<? super EscapeRouteSection> action)
    {
        escapeRouteSections.forEach(action);
    }
}
