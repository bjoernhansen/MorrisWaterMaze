package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.LineSegmentBuilder;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Optional;


final class Geometry
{
    static Optional<LineSegment> clipLine(LineSegment lineSegment, Square square)
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
        
        return Optional.of(LineSegmentBuilder.from(start).to(end));
    }
    
    static Point circleLineIntersection(Circle circle, Point lineStart, Point lineEnd)
    {
        // Source: http://www.seibsprogrammladen.de/frame1.html?Prgm/Algorithmen/Schnittpunkte
        
        double rr = circle.getRadius() * circle.getRadius();
        double x21 = lineEnd.getX() - lineStart.getX(), y21 = lineEnd.getY() - lineStart.getY();
        double x10 = lineStart.getX() - circle.getCenter().getX(), y10 = lineStart.getY() - circle.getCenter().getY();
        double a = (x21 * x21 + y21 * y21) / rr;
        double b = (x21 * x10 + y21 * y10) / rr;
        double c = (x10 * x10 + y10 * y10) / rr;
        double d = b * b - a * (c - 1);
        
        Point intersect = null;
        if (d >= 0)
        {
            double e = Math.sqrt(d);
            double u1 = (-b - e) / a, u2 = (-b + e) / a;
            if (0 <= u1 && u1 <= 1)
            {
                intersect = Point.newInstance(lineStart.getX() + x21 * u1, lineStart.getY() + y21 * u1);
            }
            else
            {
                intersect = Point.newInstance(lineStart.getX() + x21 * u2, lineStart.getY() + y21 * u2);
            }
        }
        return Objects.requireNonNull(intersect);
    }
    
    private Geometry()
    {
        throw new UnsupportedOperationException();
    }
}
