package MorrisWaterMaze.model;

import MorrisWaterMaze.parameter.MouseParameterAccessor;
import MorrisWaterMaze.util.Calculations;
import MorrisWaterMaze.util.Point;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Objects;

public class Mouse
{
    public static final double
        RADIUS = 3;					// Radius des die Maus repräsentierenden Kreises
    
    private static final double
        MOUSE_FIELD_OF_VIEW = Math.PI/2;	// Sichtfenster der Maus; default: 90° zu beiden Seiten der Blickrichtung --> 180°
    
    private Point
        mouseCoordinates;		// aktueller Aufenthaltsort der Maus
    
    private final Point
        mouseStartingPosition;  // Startposition von der Maus
    
    private boolean
        isSwimming;				// = true: Maus schwimmt; = false, wenn Maus Plattform erreicht hat oder die maximale Zeit überschritten wurde
    
    private double
        trainingLevel;				// Trainingslevel der Maus; [0, 1]; default: 0.5
    
    private final Platform
        platform;
    
    private double
        polarAngle;					// bestimmt in welche Richtung die Maus schwimmt
    
    private final double
        mouseSpeed;     					// Geschwindigkeit der Maus; default: 5
    
    private final Pool
        pool;
    
    private final Ellipse2D
        mouseCenterContainingCircle;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
    
    
    public Mouse(MouseParameterAccessor parameterAccessor, Pool pool, Platform platform)
    {
        mouseStartingPosition = getStartPosition(parameterAccessor.getStartingSide(), pool.getBorder());
        mouseCoordinates = mouseStartingPosition;
        trainingLevel = parameterAccessor.getMouseTrainingLevel();
        mouseSpeed = parameterAccessor.mouseSpeed();
        
        this.platform = platform;
        this.pool = pool;
        this.mouseCenterContainingCircle = calculateMouseCenterContainingCircle(pool);
    }
    
    private Point getStartPosition(StartingSide startingSide, Ellipse2D poolBorder)
    {
        if(startingSide == StartingSide.LEFT)
        {
            return Point.newInstance(poolBorder.getCenterX()- Pool.RADIUS + Mouse.RADIUS, poolBorder.getCenterY());
        }
        return Point.newInstance(poolBorder.getCenterX() + Pool.RADIUS - Mouse.RADIUS, poolBorder.getCenterY());
    }
    
    private Ellipse2D.Double calculateMouseCenterContainingCircle(Pool pool)
    {
        double radius = Pool.RADIUS - Mouse.RADIUS;
        return new Ellipse2D.Double(
            pool.getCenter().getX() - radius,
            pool.getCenter().getY() - radius,
            2 * radius,
            2 * radius);
    }
    
    public void moveFor(double durationOfCurrentSimulationStep)
    {
        mouseCoordinates = calculateCoordinatesAfter(durationOfCurrentSimulationStep);
    }
    
    // TODO Methode verbessern
    private Point calculateCoordinatesAfter(double durationOfNextSimulationStep)
    {
        double stepLength = mouseSpeed * durationOfNextSimulationStep;
        Point newCoordinates = Point.newInstance(
            mouseCoordinates.getX() + stepLength * Math.cos(polarAngle),
            mouseCoordinates.getY() + stepLength * Math.sin(polarAngle));
        
        if(mouseCenterContainingCircle.contains(mouseCoordinates.asPoint2D()))
        {
            double meanAngle;
            Point movementVector = Calculations.calculateVector(mouseCoordinates, newCoordinates);
            Point newPosMousePlatformVector = Calculations.calculateVector(newCoordinates, platform.getCenter());
            
            if(trainingLevel > Math.random() && MOUSE_FIELD_OF_VIEW /2 >= Calculations.angle(movementVector, newPosMousePlatformVector))
            {
                meanAngle = Calculations.calculatePolarAngle(newPosMousePlatformVector);
            }
            else
            {
                meanAngle = Calculations.calculatePolarAngle(movementVector);
            }
            polarAngle = Calculations.gaussian(meanAngle, (1-trainingLevel)*22.5*Math.PI/180);  // for a more trained mouse the standard deviation is smaller
        }
        else // Schnittstelle von Pool und Mausschritt
        {
            newCoordinates = Calculations.circleLineIntersection(pool.getCenter(), Pool.RADIUS - RADIUS, mouseCoordinates, newCoordinates);
            Point newPosCenterVector = Calculations.calculateVector(newCoordinates, pool.getCenter());
            double direction = Line2D.relativeCCW(pool.getCenter().getX(), pool.getCenter().getY(), newCoordinates.getX(), newCoordinates.getY(), mouseCoordinates.getX(), mouseCoordinates.getY());
            polarAngle = Calculations.calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + Calculations.gaussian(0, Math.PI/12, Math.PI/6));
            startSwimming();
        }
        Line2D lastMove = new Line2D.Double(mouseCoordinates.asPoint2D(), newCoordinates.asPoint2D());
        if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
        {
            newCoordinates = Point.of(Objects.requireNonNull(Calculations.clipLine(lastMove, platform.getBounds())).getP1());
            stopSwimming();
        }
        return newCoordinates;
    }
    
    public Point getCoordinates()
    {
        return mouseCoordinates;
    }
    
    public void reset()
    {
        mouseCoordinates = mouseStartingPosition;
        polarAngle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(mouseStartingPosition, pool.getCenter()));
    
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
