package MorrisWaterMaze;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public final class Calculations
{
	private Calculations()
	{
		throw new UnsupportedOperationException();
	}

	private static final Point2D p0 = new Point2D.Float(0,0);
	private static final Random random = new Random();

	public static Point2D circleLineIntersection(Point2D p1, double r, Point2D p2, Point2D p3)
	{
		// Source: http://www.seibsprogrammladen.de/frame1.html?Prgm/Algorithmen/Schnittpunkte
		double rr = r * r;
		double x21 = p3.getX() - p2.getX(), y21 = p3.getY() - p2.getY();
		double x10 = p2.getX() - p1.getX(), y10 = p2.getY() - p1.getY();
		double a = (x21 * x21 + y21 * y21) / rr;
		double b = (x21 * x10 + y21 * y10) / rr;
		double c = (x10 * x10 + y10 * y10) / rr;
		double d = b * b - a * (c - 1);
		Point2D intersect = new Point2D.Double(0.0, 0.0);
		if (d >= 0)
		{
			double e = Math.sqrt(d);
			double u1 = (-b - e) / a, u2 = (-b + e) / a;
			if (0 <= u1 && u1 <= 1)
			{
				intersect = new Point2D.Double(p2.getX() + x21 * u1, p2.getY()
						+ y21 * u1);
			} else
			{
				intersect = new Point2D.Double(p2.getX() + x21 * u2, p2.getY()
						+ y21 * u2);
			}
		}
		return intersect;
	}

	public static Point2D calculateVector(Point2D startPointX, Point2D endPoint)
	{
		return new Point2D.Double(endPoint.getX()-startPointX.getX(), endPoint.getY()- startPointX.getY());
	}

	public static double calculatePolarAngle(Point2D vector)
	{		
		return Math.atan2(vector.getY(), vector.getX());	
	}

	public static Point2D scalePoint(Point2D point, double scalingFactor)
	{		
		return new Point2D.Double(scalingFactor*point.getX(), scalingFactor*point.getY());
	}

	public static Line2D clipLine(Line2D line, Rectangle2D rect)
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
	
	public static double angle(Point2D point1, Point2D point2)
	{
		double scalar = point1.getX() * point2.getX() + point1.getY() * point2.getY();
		return Math.acos(scalar/(point1.distance(p0) * point2.distance(p0)));
	}

	static double signedAngle(Point2D p1, Point2D p2)
	{
		return Math.atan2(p2.getY(), p2.getX()) - Math.atan2(p1.getY(), p1.getX());
	}

	static double vectorLength(Point2D vector){return vector.distance(p0);}
	
	public static double gaussian(double mu, double sigma)
	{
		return sigma*random.nextGaussian()+mu;
	}
	
	public static double gaussian(double mu, double sigma, double max)
	{
		double gaussian = sigma*random.nextGaussian();
		if(Math.abs(gaussian) < max)return gaussian+mu;
		return gaussian(mu, sigma, max);
	}
	
	public static double calculateRandomDuration(double durationBias)
	{
		return Math.log(durationBias / Calculations.nonZeroRandom());
	}
	
	private static double nonZeroRandom()
	{
		return 1.0 - Math.random();
	}
}
