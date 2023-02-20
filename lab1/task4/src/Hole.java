import java.awt.*;

public class Hole {
    private final int size;
    private final Component canvas;
    private final HolePosition position;

    public Hole(Component canvas, HolePosition position, int size) {
        this.canvas = canvas;
        this.position = position;
        this.size = size;
    }

    public boolean isPointInside(int x, int y) {
        Position2D position2D = this.getPosition2D();
        return x > position2D.x && y > position2D.y && x < position2D.x + size && y < position2D.y + size;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        Position2D position2D = this.getPosition2D();
        g2.fill(new Rectangle(position2D.x, position2D.y, size, size));
    }

    public Position2D getPosition2D() {
        return switch (position) {
            case LEFT_TOP -> new Position2D(0, 0);
            case RIGHT_TOP -> new Position2D(canvas.getWidth() - size, 0);
            case LEFT_BOTTOM -> new Position2D(0, canvas.getHeight() - size);
            case RIGHT_BOTTOM -> new Position2D(canvas.getWidth() - size, canvas.getHeight() - size);
        };
    }
}
