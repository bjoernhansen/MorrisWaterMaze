package morris_water_maze.graphics;

import morris_water_maze.util.DoubleComparison;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class ColorTest
{
    private static final java.awt.Color
        AWT_RED = java.awt.Color.RED;
    
    private static final javafx.scene.paint.Color
        JAVAFX_RED = javafx.scene.paint.Color.RED;
    
    
    @Test
    void shouldReturnInputColorFromInstantiation()
    {
        assertThat(AWT_RED).isEqualTo(Color.of(AWT_RED).asAwtColor());
        assertThat(JAVAFX_RED).isEqualTo(Color.of(JAVAFX_RED).asJavaFxColor());
    }
    
    @Test
    void shouldNotReturnNull()
    {
        assertThat(Color.of(AWT_RED).asJavaFxColor()).isNotNull();
        assertThat(Color.of(JAVAFX_RED).asAwtColor()).isNotNull();
    }
    
    @Test
    void shouldCalculateJavaFxColorCorrectly()
    {
        Color red = Color.of(AWT_RED);
        java.awt.Color awtTransparentGrey = new java.awt.Color(127, 127, 127, 70);
        Color transparentGrey = Color.of(awtTransparentGrey);
        
        assertThat(red.asJavaFxColor().getOpacity()).isEqualTo(1.0);
        assertThat(red.asJavaFxColor().getRed()).isCloseTo(AWT_RED.getRed()/255.0, within(DoubleComparison.EPSILON));
        assertThat(transparentGrey.asJavaFxColor().getOpacity()).isCloseTo(awtTransparentGrey.getAlpha()/255.0, within(DoubleComparison.EPSILON));
    }
    
    @Test
    void shouldCalculateAwtColorCorrectly()
    {
        Color red = Color.of(JAVAFX_RED);
        javafx.scene.paint.Color javafxTransparentGrey = new javafx.scene.paint.Color(0.5, 0.5, 0.5, 0.25);
        Color transparentGrey = Color.of(javafxTransparentGrey);
        
        assertThat(red.asAwtColor().getAlpha()).isEqualTo(255);
        assertThat(red.asAwtColor().getRed()).isEqualTo((int)(JAVAFX_RED.getRed()*255));
        assertThat(transparentGrey.asAwtColor().getAlpha()).isEqualTo((int)(javafxTransparentGrey.getOpacity()*255));
    }
}
