package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.Calculations;
import MorrisWaterMaze.SimulationController;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Pool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


class MouseMovementPainter extends Painter<MouseMovement>
{
    private static final Color
        LIGHT_GREY = new Color(150, 150, 150);
    
    
    @Override
    public void paint(Graphics2D g2d, MouseMovement mouseMovement)
    {
        ArrayList<Point2D> escapeRoutePoints = mouseMovement.escapeRoutePoints;
        if(!escapeRoutePoints.isEmpty())
        {
            for (int i = 0; i< escapeRoutePoints.size()-1; i++)
            {
                g2d.setColor(Color.BLACK);
                g2d.draw(	new Line2D.Double(Calculations.scalePoint(escapeRoutePoints.get(i),
                    SimulationController.ZOOM_FACTOR),
                    Calculations.scalePoint(escapeRoutePoints.get(i+1),
                        SimulationController.ZOOM_FACTOR)));
                g2d.fillOval(	(int)(SimulationController.ZOOM_FACTOR *(escapeRoutePoints.get(i+1).getX()-0.5)),
                    (int)(SimulationController.ZOOM_FACTOR *(escapeRoutePoints.get(i+1).getY()-0.5)),
                    (int) SimulationController.ZOOM_FACTOR, (int) SimulationController.ZOOM_FACTOR);
            }
            if(mouseMovement.isBodyToBeDrawn())
            {
                g2d.setColor(LIGHT_GREY);
                g2d.fillOval(	(int)(SimulationController.ZOOM_FACTOR *(escapeRoutePoints.get(escapeRoutePoints.size()-1).getX()-MouseMovement.RADIUS)),
                    (int)(SimulationController.ZOOM_FACTOR *(escapeRoutePoints.get(escapeRoutePoints.size()-1).getY()-MouseMovement.RADIUS)),
                    (int)(SimulationController.ZOOM_FACTOR *MouseMovement.RADIUS*2), (int)(SimulationController.ZOOM_FACTOR *MouseMovement.RADIUS*2));
            }
        }
        
        
        /*
        
        
        if(!escapeRoutePoints.isEmpty())
        {
            Point2D lastEscapeRoutePoint = null;
            for(Point2D currentEscapeRoutePoint : escapeRoutePoints)
            {
                if (lastEscapeRoutePoint != null)
                {
                    paintEscapeRouteSection(graphicsAdapter, lastEscapeRoutePoint, currentEscapeRoutePoint);
                }
                lastEscapeRoutePoint = currentEscapeRoutePoint;
            }
   
            if(mouseMovement.isBodyToBeDrawn())
            {
                double zoomFactor = SimulationController.ZOOM_FACTOR;
                double radius = MouseMovement.RADIUS;
                graphicsAdapter.setColor(LIGHT_GREY);
                graphicsAdapter.fillOval(
                    (int)(zoomFactor *(escapeRoutePoints.get(escapeRoutePoints.size()-1).getX()-radius)),
                    (int)(zoomFactor *(escapeRoutePoints.get(escapeRoutePoints.size()-1).getY()-radius)),
                    (int)(zoomFactor *radius*2),
                    (int)(zoomFactor *radius*2));
            }
        }
        
        */
    }
    
    private void paintEscapeRouteSection(Graphics2D graphicsAdapter, Point2D lastEscapeRoutePoint, Point2D currentEscapeRoutePoint)
    {
        double zoomFactor = SimulationController.ZOOM_FACTOR;
        
        graphicsAdapter.setColor(Color.BLACK);
        graphicsAdapter.draw(
            new Line2D.Double(Calculations.scalePoint(lastEscapeRoutePoint, zoomFactor),
                Calculations.scalePoint(currentEscapeRoutePoint, zoomFactor)));
        
        graphicsAdapter.fillOval(
            (int) (zoomFactor * (currentEscapeRoutePoint.getX() - 0.5)),
            (int) (zoomFactor * (currentEscapeRoutePoint.getY() - 0.5)),
            (int)  zoomFactor,
            (int)  zoomFactor);
    }
}
