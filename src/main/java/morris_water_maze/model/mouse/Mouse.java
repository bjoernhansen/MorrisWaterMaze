package morris_water_maze.model.mouse;

import morris_water_maze.model.Platform;
import morris_water_maze.model.Pool;
import morris_water_maze.model.StartingSide;
import morris_water_maze.parameter.MouseParameterAccessor;
import morris_water_maze.util.Point;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Random;

import static morris_water_maze.model.mouse.Calculations.angle;


public class Mouse
{
    private static final double
        RADIUS = 3;					// Radius des die Maus repräsentierenden Kreises
    
    private static final double
        FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
        
    private double
        trainingLevel;				// Trainingslevel der Maus; [0, 1]; default: 0.5
    
    private final double
        speed;     					// Geschwindigkeit der Maus; default: 5
    
    private final Pool
        pool;
    
    private final Platform
        platform;
    
    private final Ellipse2D
        movementBoundaries;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
        
    private final Point
        startingCoordinates;  // Startposition von der Maus
        
    private Point
        coordinates;		// aktueller Aufenthaltsort der Maus
    
    private double
        polarAngle;					// bestimmt in welche Richtung die Maus schwimmt
    
    private boolean
        isSwimming;				// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
    private final Random
        random = new Random();
        
    
    public Mouse(MouseParameterAccessor parameterAccessor, Pool pool, Platform platform)
    {
        coordinates = startingCoordinates = getStartPosition(parameterAccessor.getStartingSide(), pool.getBorder());
        movementBoundaries = calculateMovementBoundariesThrough(pool);
        
        trainingLevel = parameterAccessor.getMouseTrainingLevel();
        speed = parameterAccessor.mouseSpeed();
        
        this.platform = platform;
        this.pool = pool;
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        coordinates = calculateNewCoordinates(durationOfCurrentSimulationStep);
    }
    
    // TODO Methode verbessern
    private Point calculateNewCoordinates(double durationOfNextSimulationStep)
    {
        double stepLength = speed * durationOfNextSimulationStep;
        Point proposedCoordinates = Point.newInstance(
            coordinates.getX() + stepLength * Math.cos(polarAngle),
            coordinates.getY() + stepLength * Math.sin(polarAngle));
        
        
        
        if(movementBoundaries.contains(proposedCoordinates.asPoint2D()))
        {
            double meanAngle;
            Point movementVector = VectorBuilder.from(coordinates).to(proposedCoordinates);
            Point mouseToPlatformVector = VectorBuilder.from(proposedCoordinates).to(platform.getCenter());
            
            if(trainingLevel > Math.random() && Mouse.FIELD_OF_VIEW /2 >= angle(movementVector, mouseToPlatformVector))
            {
                meanAngle = calculatePolarAngle(mouseToPlatformVector);
            }
            else
            {
                meanAngle = calculatePolarAngle(movementVector);
            }
            polarAngle = gaussian(meanAngle, calculateSigma());  // for a more trained mouse the standard deviation is smaller
        }
        else // Schnittstelle von Pool und Mausschritt
        {
            proposedCoordinates = circleLineIntersection(pool.getCenter(), Pool.RADIUS - Mouse.RADIUS, coordinates, proposedCoordinates);
            Point newPosCenterVector = VectorBuilder.from(proposedCoordinates).to(pool.getCenter());
            double direction = Line2D.relativeCCW(
                                        pool.getCenter().getX(),
                                        pool.getCenter().getY(),
                                        proposedCoordinates.getX(),
                                        proposedCoordinates.getY(),
                                        coordinates.getX(),
                                        coordinates.getY());
            polarAngle = calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + gaussian(0, Math.PI/12, Math.PI/6));
            startSwimming();
        }
     
        Line2D lastMove = new Line2D.Double(coordinates.asPoint2D(), proposedCoordinates.asPoint2D());
        if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
        {
            proposedCoordinates = Point.of(Objects.requireNonNull(clipLine(lastMove, platform.getBounds())).getP1());
            stopSwimming();
        }
        return proposedCoordinates;
    }
    
    private double gaussian(double mu, double sigma)
    {
        return sigma*random.nextGaussian()+mu;
    }
    
    private double gaussian(double mu, double sigma, double max)
    {
        // TODO schlechte und unklare Implementierung (rekursiver Aufruf bis Random-Ausgabe stimmig?)
        double gaussian = sigma*random.nextGaussian();
        if(Math.abs(gaussian) < max)return gaussian+mu;
        return gaussian(mu, sigma, max);
    }
    

    
    private double calculatePolarAngle(Point vector)
    {
        return Math.atan2(vector.getY(), vector.getX());
    }
    
    private Point circleLineIntersection(Point circleCenter, double radius, Point lineStart, Point lineEnd)
    {
        // Source: http://www.seibsprogrammladen.de/frame1.html?Prgm/Algorithmen/Schnittpunkte
        double rr = radius * radius;
        double x21 = lineEnd.getX() - lineStart.getX(), y21 = lineEnd.getY() - lineStart.getY();
        double x10 = lineStart.getX() - circleCenter.getX(), y10 = lineStart.getY() - circleCenter.getY();
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
    
    private Line2D clipLine(Line2D line, Rectangle2D rect)
    {
        // Source: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Clipsthespecifiedlinetothegivenrectangle.htm
        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();
        
        double minX = rect.getMinX();
        double maxX = rect.getMaxX();
        double minY = rect.getMinY();
        double maxY = rect.getMaxY();
        
        int f1 = rect.outcode(x1, y1);
        int f2 = rect.outcode(x2, y2);
        
        while ((f1 | f2) != 0)
        {
            if ((f1 & f2) != 0)
            {
                return null; // Linie liegt komplett außerhalb des Rechtecks
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
                f1 = rect.outcode(x1, y1);
            } else if (f2 != 0)
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
                f2 = rect.outcode(x2, y2);
            }
        }
        return new Line2D.Double(x1, y1, x2, y2);
    }
    
    private double calculateSigma()
    {
        return (1 - trainingLevel) * 22.5 * Math.PI / 180;
    }
    
    private Point getStartPosition(StartingSide startingSide, Ellipse2D poolBorder)
    {
        if(startingSide == StartingSide.LEFT)
        {
            return Point.newInstance(poolBorder.getCenterX()- Pool.RADIUS + Mouse.RADIUS, poolBorder.getCenterY());
        }
        return Point.newInstance(poolBorder.getCenterX() + Pool.RADIUS - Mouse.RADIUS, poolBorder.getCenterY());
    }
    
    private Ellipse2D.Double calculateMovementBoundariesThrough(Pool pool)
    {
        double radius = Pool.RADIUS - Mouse.RADIUS;
        return new Ellipse2D.Double(
            pool.getCenter().getX() - radius,
            pool.getCenter().getY() - radius,
            2 * radius,
            2 * radius);
    }
    
    public Point getCoordinates()
    {
        return coordinates;
    }
    
    public void reset()
    {
        coordinates = startingCoordinates;
        resetPolarAngleRandomly();
    }
    
    private void resetPolarAngleRandomly()
    {
        polarAngle = Math.PI*(Math.random()-0.5) + calculatePolarAngle(VectorBuilder.from(startingCoordinates).to(pool.getCenter()));
    }
    
    public boolean isSwimming()
    {
        return isSwimming;
    }
    
    public void stopSwimming()
    {
        isSwimming = false;
    }
    
    public void startSwimming()
    {
        isSwimming = true;
    }
    
    public void setTrainingLevel(double trainingLevel)
    {
        this.trainingLevel = trainingLevel;
    }
}
