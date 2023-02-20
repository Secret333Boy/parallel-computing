import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private static final int HOLE_SIZE = 50;
    private final BallCanvas canvas;

    private int ballsInAHole = 0;


    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");

        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");

        JTextField textField = new JTextField("0", 2);


        Hole leftTopHole = new Hole(canvas, HolePosition.LEFT_TOP, HOLE_SIZE);
        Hole rightTopHole = new Hole(canvas, HolePosition.RIGHT_TOP, HOLE_SIZE);
        Hole leftBottomHole = new Hole(canvas, HolePosition.LEFT_BOTTOM, HOLE_SIZE);

        Hole[] holes = {leftTopHole, rightTopHole, leftBottomHole};
        canvas.addHoles(holes);

        buttonStart.addActionListener(e -> {
            Ball ball1 = new Ball(canvas, Color.BLUE);
            Ball ball2 = new Ball(canvas, Color.YELLOW);
            canvas.addBall(ball1);
            canvas.addBall(ball2);

            BallThread thread1 = new BallThread(ball1, () -> {
                ballsInAHole++;
                textField.setText(String.valueOf(ballsInAHole));
                canvas.removeBall(ball1);
            }, holes);
            BallThread thread2 = new BallThread(ball2, () -> {
                ballsInAHole++;
                textField.setText(String.valueOf(ballsInAHole));
                canvas.removeBall(ball2);
            }, holes, thread1);
            thread1.start();
            thread2.start();
        });
        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(textField);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}