import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private static final int X_SIZE = 20;
    private static final int Y_SIZE = 20;
    private final Component canvas;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;
    private final Color color;

    public Ball(Component c, Color color) {
        this.canvas = c;
        this.color = color;

        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = this.canvas.getHeight() / 2 - Y_SIZE;
        } else {
            x = this.canvas.getWidth() / 2 - X_SIZE;
            ;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }

    public static void f() {
        int a = 0;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
            dx = -dx;
        }
        if (x + X_SIZE >= this.canvas.getWidth()) {
            x = this.canvas.getWidth() - X_SIZE;
            dx = -dx;
        }
        if (y < 0) {
            y = 0;
            dy = -dy;
        }
        if (y + Y_SIZE >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - Y_SIZE;
            dy = -dy;
        }

        this.canvas.repaint();
    }

    public int getXCentered() {
        return x + X_SIZE;
    }

    public int getYCentered() {
        return y + Y_SIZE;
    }
}