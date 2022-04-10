import java.awt.geom.Point2D;


public class MyMath {
	
	static Point2D fSchnittLineCircle(Point2D p0, double r, Point2D p1,
			Point2D p2)
			{
			double rr = r*r;
			double x21 = p2.getX()-p1.getX(), y21 = p2.getY()-p1.getY();
			double x10 = p1.getX()-p0.getX(), y10 = p1.getY()-p0.getY();
			double a = (x21*x21+y21*y21)/rr;
			double b = (x21*x10+y21*y10)/rr;
			double c = (x10*x10+y10*y10)/rr;
			double d = b*b-a*(c-1);
			Point2D intersect = new Point2D.Double(0.0, 0.0);
			if (d>=0)
			{
			double e = Math.sqrt(d);
			double u1 = (-b-e)/a, u2 = (-b+e)/a;
			if(0<=u1 && u1<=1)
			{
			intersect = new Point2D.Double(p1.getX()+x21*u1,
			p1.getY()+y21*u1);
			}
			else
			{
			intersect = new Point2D.Double(p1.getX()+x21*u2,
			p1.getY()+y21*u2);
			}
			}
			return intersect;
			}

}
