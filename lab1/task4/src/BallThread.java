import java.util.Arrays;

import static java.util.Objects.isNull;

public class BallThread extends Thread {
    private final Ball ball;
    private final TextFieldSetter ballsInAHoleUpdater;
    private final Hole[] holes;
    private final Thread threadToWait;

    public BallThread(Ball ball, TextFieldSetter ballsInAHoleUpdater, Hole[] holes) {
        this.ball = ball;
        this.ballsInAHoleUpdater = ballsInAHoleUpdater;
        this.holes = holes;
        threadToWait = null;
    }

    public BallThread(Ball ball, TextFieldSetter ballsInAHoleUpdater, Hole[] holes, Thread threadToWait) {
        this.ball = ball;
        this.ballsInAHoleUpdater = ballsInAHoleUpdater;
        this.holes = holes;
        this.threadToWait = threadToWait;
    }

    @Override
    public void run() {
        try {
            if (!isNull(threadToWait)) {
                threadToWait.join();
            }

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