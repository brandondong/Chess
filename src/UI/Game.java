package UI;

import Model.Board;
import Model.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Brandon on 2015-07-25.
 */
public class Game extends JFrame {

    public static final int SPACING = 64;
    private static BufferedImage image;
    private static java.util.List<BufferedImage> imageList;

    private Board board;

    public Game() throws IOException {
        super("Chess");
        board = new Board();
        image = ImageIO.read(new File("chesspieces.png"));
        createImageList();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new Dimension(8 * SPACING, 8 * SPACING));
        pack();
        centreOnScreen();
        setVisible(true);
        addMouseListener(new MouseHandler());
    }

    @Override
    public void paint(Graphics g) {
        renderBoard(g);
        renderMoves(g);
        renderPieces(g);
    }

    // Modifies: g
    // Effects: renders the current move set
    private void renderMoves(Graphics g) {
        Set<Point> moves = board.getPossibleMoves();
        if (board.getColor()) {
            g.setColor(new Color(177, 173, 182));
        } else {
            g.setColor(new Color(130, 103, 43));
        }
        for (Point p : moves) {
            g.fillRect(p.x * SPACING, p.y * SPACING, SPACING, SPACING);
        }
    }

    // Effects: creates cropped images of each piece and puts them in a list for ease of access
    private void createImageList() {
        imageList = new LinkedList<>();

        for (int i = 0; i < 6; i++) {
            imageList.add(image.getSubimage(i * SPACING, 0, SPACING, SPACING));
        }
        for (int i = 0; i < 6; i++) {
            imageList.add(image.getSubimage(i * SPACING, SPACING, SPACING, SPACING));
        }
    }

    // Modifies: g
    // Renders the board pattern of squares
    private void renderBoard(Graphics g) {
        for (int i = 0; i < 64; i++) {
            int x = (i % 8);
            int y = (i / 8);
            if ((x + y) % 2 == 0) {
                g.setColor(new Color(208, 138, 71));
            } else {
                g.setColor(new Color(253, 205, 157));
            }
            g.fillRect(x * SPACING, y * SPACING, SPACING, SPACING);
        }
    }

    // Modifies: g
    // Effects: renders the pieces onto the board
    private void renderPieces(Graphics g) {
        Set<Piece> pieces = board.getPieces();

        for (Piece next : pieces) {
            BufferedImage typeImage = getImageOfType(next.getType(), next.getColor());
            g.drawImage(typeImage, next.getCoordinate().x * SPACING, next.getCoordinate().y * SPACING, null);
        }
    }

    // Effects: returns the image of a particular piece type and color
    private BufferedImage getImageOfType(Model.Type t, boolean color) {
        int y;
        if (color) {
            y = 6;
        } else {
            y = 0;
        }

        if (t.equals(Model.Type.KING)) {
            return imageList.get(y);
        } else if (t.equals(Model.Type.QUEEN)) {
            return imageList.get(y + 1);
        } else if (t.equals(Model.Type.ROOK)) {
            return imageList.get(y + 2);
        } else if (t.equals(Model.Type.KNIGHT)) {
            return imageList.get(y + 3);
        } else if (t.equals(Model.Type.BISHOP)) {
            return imageList.get(y + 4);
        }
        return imageList.get(y + 5);

    }

    // Modifies: this
    // Effects: location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    public class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point click = new Point(e.getX() / SPACING, e.getY() / SPACING);
            if (board.getPossibleMoves().contains(click)) {
                board.moveCurrent(click);
            } else {
                board.setCurrentPiece(click);
            }
            repaint();
        }
    }

    public static void main(String[] args) throws IOException {
        new Game();
    }
}
