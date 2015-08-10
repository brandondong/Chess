package Tests;

import Model.Board;
import Model.Type;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Brandon on 2015-07-21.
 */
public class BoardTest {

    private Board test;

    @Before
    public void setup() {
        test = new Board();
    }

    @Test
    public void testMove() {
        test.setCurrentPiece(new Point(0, 6));
        test.moveCurrent(new Point(0, 5));
        assertEquals(null, test.getPieceAt(new Point(0, 6)));
        assertEquals(test.getPieceAt(new Point(0, 5)).getType(), Type.PAWN);
    }

    @Test
    public void testSetCurrent() {
        test.setCurrentPiece(new Point(0, 0));
        assertEquals(null, test.getCurrentPiece());
        test.setCurrentPiece(new Point(0, 5));
        assertEquals(null, test.getCurrentPiece());
    }

    @Test
    public void testNext() {
        test.setCurrentPiece(new Point(1, 7));
        assertEquals(2, test.getPossibleMoves().size());
        test.setCurrentPiece(new Point(1, 6));
        assertEquals(2, test.getPossibleMoves().size());
        test.moveCurrent(new Point(1, 4));
        test.setCurrentPiece(new Point(0, 1));
        assertEquals(2, test.getPossibleMoves().size());
    }

    @Test
    public void testNextStraight() {
        test.setCurrentPiece(new Point(0, 6));
        test.moveCurrent(new Point(0, 4));
        test.setCurrentPiece(new Point(0, 1));
        test.moveCurrent(new Point(0, 2));
        test.setCurrentPiece(new Point(0, 7));
        for (Point p : test.getPossibleMoves()) {
            System.out.println("x: " + p.x);
            System.out.println("y: " + p.y);
        }
    }
}