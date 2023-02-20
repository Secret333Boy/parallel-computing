import java.util.Arrays;

public class BallThread extends Thread {
    private final Ball ball;

    public BallThread(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ball.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);
            }
        } catch (InterruptedException ignored) {

        }
    }
}