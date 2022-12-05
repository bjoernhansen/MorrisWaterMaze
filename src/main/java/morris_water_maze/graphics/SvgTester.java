package morris_water_maze.graphics;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class SvgTester
{
    public static void main(String[] args) throws IOException
    {
        SVGGraphics2D g2d = new SVGGraphics2D(600, 400);
        Rectangle rectangle = new Rectangle(20, 120, 100, 200);
        Ellipse2D ellipse = new Ellipse2D.Double(20, 30, 40, 50);
        
        g2d.draw(rectangle);
        g2d.draw(ellipse);
        
        File file = new File("SVGBarChartDemo1.svg");
        SVGUtils.writeToSVG(file, g2d.getSVGElement(), false);
    }
}
