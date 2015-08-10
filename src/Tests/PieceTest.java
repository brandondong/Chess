package Tests;

import Model.Piece;
import Model.Type;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Brandon on 2015-07-21.
 */
public class PieceTest {

    private Piece test;

    @Before
    public void setup() {
        test = new Piece(0, 0, Type.KING, true);
    }

    @Test
    public void testMoveTo() {
        test.moveTo(new Point(3, 4));
        assertTrue(test.getCoordinate().equals(new Point(3, 4)));
    }

    @Test
    public void testAtLocation() {
        assertTrue(test.atLocation(new Point(0, 0)));
        assertFalse(test.atLocation(new Point(2, 4)));
    }

    @Test
    public void testSetType() {
        test.setType(Type.QUEEN);
        assertEquals(Type.QUEEN, test.getType());
    }
}