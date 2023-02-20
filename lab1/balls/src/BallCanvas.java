import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class BallCanvas extends JPanel {
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final ArrayList<Hole> holes = new ArrayList<>();

    public void addBall(Ball b) {
        this.balls.add(b);
    }

    public void addHole(Hole hole) {
        this.holes.add(hole);
    }

    public void addHoles(Hole[] holes) {
        Collections.addAll(this.holes, holes);
    }

    public void removeBall(Ball ball) {
        this.balls.remove(ball);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < holes.size(); i++) {
            Hole hole = this.holes.get(i);
            hole.draw(g2);
        }

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = this.balls.get(i);
            ball.draw(g2);
        }
    }
}