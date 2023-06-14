package morris_water_maze.util.geometry;

import morris_water_maze.util.calculations.DoubleComparison;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class SquareTest
{
    private final static double
        X = 10.0;
    
    private final static double
        Y = 20.0;
    
    private final static double
        SIDE_LENGTH = 30.0;
    
    private static final Square
        SQUARE = Square.newInstance(X, Y, SIDE_LENGTH);
    
    
    @Test
    void shouldInstantiateCircleCorrectly()
    {
        assertThat(SQUARE.getSideLength()).isEqualTo(SIDE_LENGTH);
        assertThat(SQUARE.getCenter().getX()).isCloseTo(X + SIDE_LENGTH/2.0, within(DoubleComparison.EPSILON));
        assertThat(SQUARE.getCenter().getY()).isCloseTo(Y + SIDE_LENGTH/2.0, within(DoubleComparison.EPSILON));
        assertThat(SQUARE.getMaxX()).isCloseTo(X + SIDE_LENGTH, within(DoubleComparison.EPSILON));
        assertThat(SQUARE.getMaxY()).isCloseTo(Y + SIDE_LENGTH, within(DoubleComparison.EPSILON));
    }
    
    @Test
    void shouldIntersectionsCorrectly()
    {
        LineSegment verticalLineSegmentLeftOfSquare
            = LineSegment.from(Point.newInstance(0.5 * X, 0.5 * Y))
                         .to(Point.newInstance(0.5 * X, 2.0 * Y + SIDE_LENGTH));
        LineSegment verticalLineSegmentIntersectingSquare
            = LineSegment.from(Point.newInstance(X + SIDE_LENGTH / 2.0, 0.5 * Y))
                         .to(Point.newInstance(X + SIDE_LENGTH / 2.0, 2.0 * Y + SIDE_LENGTH));
        LineSegment verticalLineSegmentRightOfSquare
            = LineSegment.from(Point.newInstance(2.0 * X + SIDE_LENGTH, 0.5 * Y))
                         .to(Point.newInstance(2.0 * X + SIDE_LENGTH, 2.0 * Y + SIDE_LENGTH));
    
        LineSegment horizontalLineSegmentLeftOfSquare
            = LineSegment.from(Point.newInstance(0.5 * X, 0.5 * Y))
                         .to(Point.newInstance(2.0 * X + SIDE_LENGTH, 0.5 * Y));
        LineSegment horizontalLineSegmentIntersectingSquare
            = LineSegment.from(Point.newInstance(0.5 * X, Y + SIDE_LENGTH / 2.0))
                         .to(Point.newInstance(2.0 * X + SIDE_LENGTH, Y + SIDE_LENGTH / 2.0));
        LineSegment horizontalLineSegmentRightOfSquare
            = LineSegment.from(Point.newInstance(0.5 * X, 2.0 * Y + SIDE_LENGTH))
                         .to(Point.newInstance(2.0 * X + SIDE_LENGTH, 2.0 * Y + SIDE_LENGTH));
        
        assertThat(SQUARE.intersects(verticalLineSegmentLeftOfSquare)).isFalse();
        assertThat(SQUARE.intersects(verticalLineSegmentIntersectingSquare)).isTrue();
        assertThat(SQUARE.intersects(verticalLineSegmentRightOfSquare)).isFalse();
        assertThat(SQUARE.intersects(horizontalLineSegmentLeftOfSquare)).isFalse();
        assertThat(SQUARE.intersects(horizontalLineSegmentIntersectingSquare)).isTrue();
        assertThat(SQUARE.intersects(horizontalLineSegmentRightOfSquare)).isFalse();
    }
}
