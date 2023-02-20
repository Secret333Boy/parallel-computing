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


        Hole leftTopHole = new Hole(canvas,HolePosition.LEFT_TOP, HOLE_SIZE);
        Hole rightTopHole = new Hole(canvas, HolePosition.RIGHT_TOP, HOLE_SIZE);
        Hole leftBottomHole = new Hole(canvas,HolePosition.LEFT_BOTTOM, HOLE_SIZE);
        Hole rightBottomHole = new Hole(canvas,HolePosition.RIGHT_BOTTOM, HOLE_SIZE);

        Hole[] holes = {leftTopHole, rightTopHole, leftBottomHole, rightBottomHole};
        canvas.addHoles(holes);

        buttonStart.addActionListener(e -> {
            BallPriority priority = Math.random() < 0.5 ? BallPriority.HIGH : BallPriority.STANDARD;

            Ball ball = new Ball(canvas, priority);
            canvas.addBall(ball);

            BallThread thread = new BallThread(ball, () -> {
                ballsInAHole++;
                textField.setText(String.valueOf(ballsInAHole));
                canvas.removeBall(ball);
            }, holes);
            thread.setPriority(priority == BallPriority.HIGH ? 10 : 5);
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