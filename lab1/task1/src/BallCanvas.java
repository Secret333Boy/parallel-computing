import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private final ArrayList<Ball> balls = new ArrayList<>();

    public void addBall(Ball b) {
        this.balls.add(b);
    }

    public void removeBall(Ball ball) {
        this.balls.remove(ball);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = this.balls.get(i);
            ball.draw(g2);
        }
    }
}