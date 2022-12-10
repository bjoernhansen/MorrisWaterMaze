package morris_water_maze.graphics;

public final class Color
{
    public static final Color
        WHITE = Color.of(java.awt.Color.WHITE);
    
    public static final Color
        DARK_GREY = Color.newInstance(75, 75, 75);
    
    public static final Color
        BLACK = Color.of(java.awt.Color.BLACK);
    
    public static final Color
        RED = Color.of(java.awt.Color.RED);
    
    private final java.awt.Color
        awtColor;
    
    private final javafx.scene.paint.Color
        javaFxColor;
    
    
    public static Color of(java.awt.Color awtColor)
    {
        return new Color(awtColor, calculateJavaFxColor(awtColor));
    }
    
    public static Color of(javafx.scene.paint.Color javaFxColor)
    {
        return new Color(calculateAwtColor(javaFxColor), javaFxColor);
    }
    
    
    public static Color newInstance(int red, int green, int blue)
    {
        java.awt.Color awtColor = new java.awt.Color(red, green, blue);
        return Color.of(awtColor);
    }
    
    public static Color newInstance(int r, int g, int b, int alpha)
    {
        java.awt.Color awtColor = new java.awt.Color(r, g, b, alpha);
        return Color.of(awtColor);
    }
    
    private Color(java.awt.Color awtColor, javafx.scene.paint.Color javaFxColor)
    {
        this.awtColor = awtColor;
        this.javaFxColor = javaFxColor;
    }
    
    private static java.awt.Color calculateAwtColor(javafx.scene.paint.Color javaFxColor)
    {
        return new java.awt.Color(
            (int)(javaFxColor.getRed()*255.0),
            (int)(javaFxColor.getGreen()*255.0),
            (int)(javaFxColor.getRed()*255.0),
            (int)(javaFxColor.getOpacity()*255.0));
    }
    
    private static javafx.scene.paint.Color calculateJavaFxColor(java.awt.Color awtColor)
    {
        return new javafx.scene.paint.Color(
            awtColor.getRed()/255.0,
            awtColor.getGreen()/255.0,
            awtColor.getBlue()/255.0,
            awtColor.getAlpha()/255.0);
    }
    
    public java.awt.Color asAwtColor()
    {
        return awtColor;
    }
    
    public javafx.scene.paint.Color asJavaFxColor()
    {
        return javaFxColor;
    }
}
