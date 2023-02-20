import java.util.Arrays;

public class BallThread extends Thread {
    private final Ball ball;
    private final TextFieldSetter ballsInAHoleUpdater;
    private final Hole[] holes;

    public BallThread(Ball ball, TextFieldSetter ballsInAHoleUpdater, Hole[] holes) {
        this.ball = ball;
        this.ballsInAHoleUpdater = ballsInAHoleUpdater;
        this.holes = holes;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ball.move();
                if (Arrays.stream(holes).anyMatch((hole -> hole.isPointInside(ball.getXCentered(), ball.getYCentered())))) {
                    this.ballsInAHoleUpdater.updateBallsInAHole();
                    break;
                }
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);
            }
        } catch (InterruptedException ignored) {

        }
    }
}