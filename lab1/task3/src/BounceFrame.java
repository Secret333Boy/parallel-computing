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

        Hole[] holes = {};

        buttonStart.addActionListener(e -> {
            for (int i = 0; i < 1000; i++) {
                Ball ball = new Ball(canvas, BallPriority.HIGH);
                canvas.addBall(ball);

                BallThread thread = new BallThread(ball, () -> {
                    ballsInAHole++;
                    textField.setText(String.valueOf(ballsInAHole));
                    canvas.removeBall(ball);
                }, holes);
                thread.setPriority(5);
                thread.start();
            }

            Ball ball = new Ball(canvas, BallPriority.STANDARD);
            canvas.addBall(ball);

            BallThread thread = new BallThread(ball, () -> {
                ballsInAHole++;
                textField.setText(String.valueOf(ballsInAHole));
                canvas.removeBall(ball);
            }, holes);
            thread.setPriority(10);
            thread.start();
            System.out.println("Thread name = " + thread.getName());
        });
        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(textField);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}