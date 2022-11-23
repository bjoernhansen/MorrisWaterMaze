package morris_water_maze.model;

import morris_water_maze.parameter.MouseParameterAccessor;
import morris_water_maze.util.Calculations;
import morris_water_maze.util.Point;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Objects;

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
            Point movementVector = Calculations.calculateVector(coordinates, proposedCoordinates);
            Point mouseToPlatformVector = Calculations.calculateVector(proposedCoordinates, platform.getCenter());
            
            if(trainingLevel > Math.random() && Mouse.FIELD_OF_VIEW /2 >= Calculations.angle(movementVector, mouseToPlatformVector))
            {
                meanAngle = Calculations.calculatePolarAngle(mouseToPlatformVector);
            }
            else
            {
                meanAngle = Calculations.calculatePolarAngle(movementVector);
            }
            polarAngle = Calculations.gaussian(meanAngle, calculateSigma());  // for a more trained mouse the standard deviation is smaller
        }
        else // Schnittstelle von Pool und Mausschritt
        {
            proposedCoordinates = Calculations.circleLineIntersection(pool.getCenter(), Pool.RADIUS - Mouse.RADIUS, coordinates, proposedCoordinates);
            Point newPosCenterVector = Calculations.calculateVector(proposedCoordinates, pool.getCenter());
            double direction = Line2D.relativeCCW(
                                        pool.getCenter().getX(),
                                        pool.getCenter().getY(),
                                        proposedCoordinates.getX(),
                                        proposedCoordinates.getY(),
                                        coordinates.getX(),
                                        coordinates.getY());
            polarAngle = Calculations.calculatePolarAngle(newPosCenterVector) - direction * (Math.PI/3 + Calculations.gaussian(0, Math.PI/12, Math.PI/6));
            startSwimming();
        }
     
        Line2D lastMove = new Line2D.Double(coordinates.asPoint2D(), proposedCoordinates.asPoint2D());
        if(lastMove.intersects(platform.getBounds())) // Schnittstelle von Maus und Plattform
        {
            proposedCoordinates = Point.of(Objects.requireNonNull(Calculations.clipLine(lastMove, platform.getBounds())).getP1());
            stopSwimming();
        }
        return proposedCoordinates;
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
        polarAngle = Math.PI*(Math.random()-0.5) + Calculations.calculatePolarAngle(Calculations.calculateVector(startingCoordinates, pool.getCenter()));
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
