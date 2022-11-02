package MorrisWaterMaze.model;

import MorrisWaterMaze.util.Point;
import MorrisWaterMaze.util.Stack;

import java.util.Optional;
import java.util.function.Consumer;


public final class EscapeRoute
{
    private final Stack<EscapeRouteSection>
        escapeRouteSections = new Stack<>();
    
    private Point
        startPosition;
        
    
    public void addNextSectionTo(Point nextPosition)
    {
        Point previousPosition = getPreviousPosition();
        EscapeRouteSection nextSection = new EscapeRouteSection(previousPosition, nextPosition);
        escapeRouteSections.push(nextSection);
    }
    
    private Point getPreviousPosition()
    {
        return Optional.ofNullable(escapeRouteSections.peek())
                       .map(EscapeRouteSection::getEnd)
                       .orElse(startPosition);
    }
    
    public EscapeRouteSection getLastSection()
    {
        return escapeRouteSections.peek();
    }
    
    public void resetTo(Point startPosition)
    {
        escapeRouteSections.clear();
        this.startPosition = startPosition;
    }
    
    public void forEachSection(Consumer<? super EscapeRouteSection> action)
    {
        escapeRouteSections.forEach(action);
    }
}
