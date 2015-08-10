package Model;

import java.awt.*;

/**
 * Created by Brandon on 2015-07-19.
 */
public class Piece {

    private Point coordinate;
    private Type type;
    private boolean isWhite;

    // Requires: (x,y) coordinate is within board
    // Effects: Creates a new piece as specified
    public Piece(int x, int y, Type type, boolean isWhite) {
        coordinate = new Point(x, y);
        this.type = type;
        this.isWhite = isWhite;
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: moves the piece to the given position
    public void moveTo(Point p) {
       coordinate.setLocation(p);
    }

    // Modifies: this
    // Effects: sets the type
    public void setType(Type t) {
        type = t;
    }

    public Point getCoordinate() { return coordinate; }

    public Type getType() {
        return type;
    }

    public boolean getColor() {
        return isWhite;
    }

    // Effects: returns true if piece is a location specified
    public boolean atLocation(Point p) {
        return coordinate.equals(p);
    }
}
