import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private static final int HOLE_SIZE = 50;
    private final BallCanvas canvas;

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
        JButton button100Start = new JButton("Start 100");
        JButton buttonStop = new JButton("Stop");

        JTextField textField = new JTextField("0", 2);

        buttonStart.addActionListener(e -> {

            Ball ball = new Ball(canvas);
            canvas.addBall(ball);

            BallThread thread = new BallThread(ball);
            thread.start();
            System.out.println("Thread name = " + thread.getName());
        });
        button100Start.addActionListener(e -> {
            for (int i = 0; i < 100; i++) {
                Ball ball = new Ball(canvas);
                canvas.addBall(ball);

                BallThread thread = new BallThread(ball);
                thread.start();
                System.out.println("Thread name = " + thread.getName());
            }
        });
        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(button100Start);
        buttonPanel.add(buttonStop);
        buttonPanel.add(textField);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}