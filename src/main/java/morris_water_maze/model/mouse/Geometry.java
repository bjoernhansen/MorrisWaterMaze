package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

import static morris_water_maze.model.mouse.Calculations.dotProduct;
import static morris_water_maze.model.mouse.Calculations.square;


public final class Geometry
{
    public static Optional<LineSegment> clipLine(LineSegment lineSegment, Square square)
    {
        if(lineSegment == null || square == null)
        {
            return Optional.empty();
        }
        
        // Source: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Clipsthespecifiedlinetothegivenrectangle.htm
        double x1 = lineSegment.getStart().getX();
        double y1 = lineSegment.getStart().getY();
        double x2 = lineSegment.getEnd().getX();
        double y2 = lineSegment.getEnd().getY();
        
        double minX = square.getMinX();
        double maxX = square.getMaxX();
        double minY = square.getMinY();
        double maxY = square.getMaxY();
        
        int f1 = square.outcode(x1, y1);
        int f2 = square.outcode(x2, y2);
        
        while ((f1 | f2) != 0)
        {
            if ((f1 & f2) != 0)
            {
                return Optional.empty(); // Linie liegt komplett außerhalb des Rechtecks
            }
            double dx = (x2 - x1);
            double dy = (y2 - y1);
            // update (x1, y1), (x2, y2) and f1 and f2 using intersections
            // then recheck
            if (f1 != 0)
            {
                // first point is outside, so we update it against one of the
                // four sides then continue
                if ((f1 & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT
                    && dx != 0.0)
                {
                    y1 = y1 + (minX - x1) * dy / dx;
                    x1 = minX;
                } else if ((f1 & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT
                    && dx != 0.0)
                {
                    y1 = y1 + (maxX - x1) * dy / dx;
                    x1 = maxX;
                } else if ((f1 & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM
                    && dy != 0.0)
                {
                    x1 = x1 + (maxY - y1) * dx / dy;
                    y1 = maxY;
                } else if ((f1 & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP
                    && dy != 0.0)
                {
                    x1 = x1 + (minY - y1) * dx / dy;
                    y1 = minY;
                }
                f1 = square.outcode(x1, y1);
            }
            else if (f2 != 0)
            {
                // second point is outside, so we update it against one of the
                // four sides then continue
                if ((f2 & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT
                    && dx != 0.0)
                {
                    y2 = y2 + (minX - x2) * dy / dx;
                    x2 = minX;
                } else if ((f2 & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT
                    && dx != 0.0)
                {
                    y2 = y2 + (maxX - x2) * dy / dx;
                    x2 = maxX;
                } else if ((f2 & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM
                    && dy != 0.0)
                {
                    x2 = x2 + (maxY - y2) * dx / dy;
                    y2 = maxY;
                } else if ((f2 & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP
                    && dy != 0.0)
                {
                    x2 = x2 + (minY - y2) * dx / dy;
                    y2 = minY;
                }
                f2 = square.outcode(x2, y2);
            }
        }
        
        Point start = Point.newInstance(x1, y1);
        Point end = Point.newInstance(x2, y2);
        
        return Optional.of(LineSegment.from(start).to(end));
    }
    
    public static Optional<Point> lineSegmentExitPointOutOfCircle(LineSegment lineSegment, Circle circle)
    {
        if(lineSegment == null || circle == null)
        {
            return Optional.empty();
        }
        
        // inspired by: http://www.seibsprogrammladen.de/frame1.html?Prgm/Algorithmen/Schnittpunkte
        Point lineStartToEndVector = VectorBuilder.from(lineSegment.getStart())
                                                  .to(lineSegment.getEnd());
        Point circleCenterToLineStartVector = VectorBuilder.from(circle.getCenter())
                                                           .to(lineSegment.getStart());
        double rr = square(circle.getRadius());
        double a = square(lineStartToEndVector) / rr;
        double b = dotProduct(lineStartToEndVector, circleCenterToLineStartVector) / rr;
        double c = square(circleCenterToLineStartVector) / rr;
        double d = square(b) - a * (c - 1);
        
        if (d >= 0)
        {
            double e = Math.sqrt(d);
            double u = (-b + e) / a;
    
            return Optional.of(Point.newInstance(
                                    lineSegment.getStart().getX() + lineStartToEndVector.getX() * u,
                                    lineSegment.getStart().getY() + lineStartToEndVector.getY() * u));
        }
        return Optional.empty();
    }
    
    private Geometry()
    {
        throw new UnsupportedOperationException();
    }
}
