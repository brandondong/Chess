package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Brandon on 2015-07-19.
 */
public class Board {

    private Piece currentPiece;
    private Set<Piece> pieces;
    private Set<Point> possibleMoves;
    private boolean isWhiteTurn;

    public Board() {
        currentPiece = null;
        pieces = new HashSet<>();
        possibleMoves = new HashSet<>();
        isWhiteTurn = true;
        init();
    }

    // Modifies: this
    // Effects: initializes the board by adding all the pieces
    private void init() {
        for (int i = 0; i < 8; i++) {
            pieces.add(new Piece(i, 1, Type.PAWN, false));
            pieces.add(new Piece(i, 6, Type.PAWN, true));
        }

        pieces.add(new Piece(0, 0, Type.ROOK, false));
        pieces.add(new Piece(7, 0, Type.ROOK, false));
        pieces.add(new Piece(0, 7, Type.ROOK, true));
        pieces.add(new Piece(7, 7, Type.ROOK, true));

        pieces.add(new Piece(1, 0, Type.KNIGHT, false));
        pieces.add(new Piece(6, 0, Type.KNIGHT, false));
        pieces.add(new Piece(1, 7, Type.KNIGHT, true));
        pieces.add(new Piece(6, 7, Type.KNIGHT, true));

        pieces.add(new Piece(2, 0, Type.BISHOP, false));
        pieces.add(new Piece(5, 0, Type.BISHOP, false));
        pieces.add(new Piece(2, 7, Type.BISHOP, true));
        pieces.add(new Piece(5, 7, Type.BISHOP, true));

        pieces.add(new Piece(3, 0, Type.QUEEN, false));
        pieces.add(new Piece(4, 0, Type.KING, false));
        pieces.add(new Piece(3, 7, Type.QUEEN, true));
        pieces.add(new Piece(4, 7, Type.KING, true));
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: sets the current piece to the piece at a given point and updates the possible moves
    public void setCurrentPiece(Point p) {
        currentPiece = getPieceAt(p);
        if (currentPiece != null && hasPieceOfColor(p, isWhiteTurn)) {
            updatePossibleMoves();
        } else {
            currentPiece = null;
            possibleMoves.clear();
        }
    }

    // Requires: point is within board, currentPiece is not null and is the same color as turn
    // Modifies: this
    // Effects: removes the piece that is at the given point if there is any
    // and moves the current piece to that point, ending the turn
    public void moveCurrent(Point destination) {
        removePieceAt(destination);
        currentPiece.moveTo(destination);

        isWhiteTurn = !isWhiteTurn;
        currentPiece = null;
        possibleMoves.clear();
        updateForQueen();
        if (isOver(true)) {
            removeColor(true);
        } else if (isOver(false)) {
            removeColor(false);
        }
    }

    // Modifies: this
    // Effects: changes any pawns that reach the end of the board into queens
    private void updateForQueen() {
        for (Piece next : pieces) {
            if ((next.getCoordinate().y == 0 || next.getCoordinate().y == 7) &&
                    next.getType().equals(Type.PAWN)) {
                next.setType(Type.QUEEN);
            }
        }
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: removes the piece that is at the given point if there is any
    private void removePieceAt(Point destination) {
        Piece remove = getPieceAt(destination);
        if (remove != null) {
            pieces.remove(remove);
        }
    }

    // Requires: currentPiece is not null
    // Modifies: this
    // Effects: updates possibleMoves by finding the possible moves of the current piece
    private void updatePossibleMoves() {
        possibleMoves.clear();
        Point p = currentPiece.getCoordinate();

        if (currentPiece.getType().equals(Type.KING)) {
            nextMoveKing(p);
        } else if (currentPiece.getType().equals(Type.KNIGHT)) {
            nextMoveKnight(p);
        } else if (currentPiece.getType().equals(Type.BISHOP)) {
            nextMoveDiagonal(p);
        } else if (currentPiece.getType().equals(Type.ROOK)) {
            nextMoveStraight(p);
        } else if (currentPiece.getType().equals(Type.QUEEN)) {
            nextMoveStraight(p);
            nextMoveDiagonal(p);
        } else {
            nextMovePawn(p);
        }
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: updates possibleMoves by finding the moves for a pawn
    private void nextMovePawn(Point p) {
        int x = p.x;
        int y = p.y;

        if (isWhiteTurn) {
            if (isEmpty(new Point(x, y - 1))) {
                possibleMoves.add(new Point(x, y - 1));
            }
            if (y == 6 && (isEmpty(new Point(x, y - 2)))) {
                possibleMoves.add(new Point(x, y - 2));
            }
            if (hasPieceOfColor(new Point(x - 1, y - 1), false)) {
                possibleMoves.add(new Point(x - 1, y - 1));
            }
            if (hasPieceOfColor(new Point(x + 1, y - 1), false)) {
                possibleMoves.add(new Point(x + 1, y - 1));
            }
        } else {
            if (isEmpty(new Point(x, y + 1))) {
                possibleMoves.add(new Point(x, y + 1));
            }
            if (y == 1 && (isEmpty(new Point(x, y + 2)))) {
                possibleMoves.add(new Point(x, y + 2));
            }
            if (hasPieceOfColor(new Point(x - 1, y + 1), true)) {
                possibleMoves.add(new Point(x - 1, y + 1));
            }
            if (hasPieceOfColor(new Point(x + 1, y + 1), true)) {
                possibleMoves.add(new Point(x + 1, y + 1));
            }
        }
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: updates possibleMoves by finding diagonal paths
    private void nextMoveDiagonal(Point p) {
        Point temp = new Point(p.x - 1, p.y - 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x - 1, temp.y - 1);
        }

        temp = new Point(p.x - 1, p.y + 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x - 1, temp.y + 1);
        }

        temp = new Point(p.x + 1, p.y + 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x + 1, temp.y + 1);
        }

        temp = new Point(p.x + 1, p.y - 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x + 1, temp.y - 1);
        }
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: updates possibleMoves by finding straight paths
    private void nextMoveStraight(Point p) {
        Point temp = new Point(p.x - 1, p.y);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x - 1, temp.y);
        }

        temp = new Point(p.x + 1, p.y);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x + 1, temp.y);
        }

        temp = new Point(p.x, p.y + 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x, temp.y + 1);
        }

        temp = new Point(p.x, p.y - 1);
        while (isValidPoint(temp)) {
            possibleMoves.add(temp);
            if (hasPieceOfColor(temp, !isWhiteTurn)) {
                break;
            }
            temp = new Point(temp.x, temp.y - 1);
        }
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: updates possibleMoves by finding the next moves for the knight
    private void nextMoveKnight(Point p) {
        int x = p.x;
        int y = p.y;
        possibleMoves.add(new Point(x + 1, y + 2));
        possibleMoves.add(new Point(x + 1, y - 2));
        possibleMoves.add(new Point(x - 1, y + 2));
        possibleMoves.add(new Point(x - 1, y - 2));
        possibleMoves.add(new Point(x + 2, y + 1));
        possibleMoves.add(new Point(x + 2, y - 1));
        possibleMoves.add(new Point(x - 2, y + 1));
        possibleMoves.add(new Point(x - 2, y - 1));

        filterPossibleMoves();
    }

    // Requires: point is within board
    // Modifies: this
    // Effects: updates possibleMoves by finding the next moves for the king
    private void nextMoveKing(Point p) {
        int x = p.x;
        int y = p.y;
        possibleMoves.add(new Point(x + 1, y));
        possibleMoves.add(new Point(x + 1, y + 1));
        possibleMoves.add(new Point(x + 1, y - 1));
        possibleMoves.add(new Point(x - 1, y));
        possibleMoves.add(new Point(x - 1, y + 1));
        possibleMoves.add(new Point(x - 1, y - 1));
        possibleMoves.add(new Point(x, y + 1));
        possibleMoves.add(new Point(x, y - 1));

        filterPossibleMoves();
    }

    // Modifies: this
    // Effects: filters all points in possibleMoves that are not valid
    private void filterPossibleMoves() {
        Set<Point> toRemove = new HashSet<>();
        for (Point p: possibleMoves) {
            if (!isValidPoint(p)) {
                toRemove.add(p);
            }
        }
        possibleMoves.removeAll(toRemove);
    }

    // Effects: returns true if point is within board and is not taken by a piece of the same color
    private boolean isValidPoint(Point p) {
        return (p.x >= 0 && p.x <= 7 && p.y >= 0 && p.y <= 7 &&
        !hasPieceOfColor(p, isWhiteTurn));
    }

    // Effects: returns true if point is within board and is not taken by any piece
    private boolean isEmpty(Point p) {
        return (getPieceAt(p) == null);
    }

    // Effects: returns true if there is a piece of a certain color at the specified location
    private boolean hasPieceOfColor(Point p, boolean color) {
        Piece next = getPieceAt(p);
        return (next != null && next.getColor() == color);
    }

    // Effects: returns the piece at the specified location, null if there is none
    public Piece getPieceAt(Point destination) {
        for (Piece next : pieces) {
            if (next.atLocation(destination)) {
                return next;
            }
        }
        return null;
    }

    // Effects: returns true if the game is over for a certain color
    private boolean isOver(boolean color) {
        for (Piece next : pieces) {
            if (next.getType().equals(Type.KING)) {
                if (next.getColor() == color) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeColor(boolean color) {
        Set<Piece> toRemove = new HashSet<>();
        for (Piece next: pieces) {
            if (next.getColor() == color) {
                toRemove.add(next);
            }
        }
        pieces.removeAll(toRemove);
    }

    public Set<Point> getPossibleMoves() {
        return possibleMoves;
    }

    public Piece getCurrentPiece() { return currentPiece; }

    public Set<Piece> getPieces() { return pieces; }

    public boolean getColor() { return isWhiteTurn; }
}
