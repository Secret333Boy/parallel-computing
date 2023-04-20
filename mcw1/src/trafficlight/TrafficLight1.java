package trafficlight;

import java.util.HashMap;
import java.util.Map;

public class TrafficLight1 extends Thread {
    public static Map<TrafficLightColor, Integer> colorToTimeoutMap = new HashMap<>();

    static {
        colorToTimeoutMap.put(TrafficLightColor.GREEN, 60);
        colorToTimeoutMap.put(TrafficLightColor.YELLOW, 10);
        colorToTimeoutMap.put(TrafficLightColor.RED, 40);
    }

    private int carsGone = 0;

    private final TrafficLightColor[] trafficLightColors = {TrafficLightColor.GREEN, TrafficLightColor.YELLOW, TrafficLightColor.RED, TrafficLightColor.YELLOW};
    private int colorIndex = 0;

    @Override
    public void run() {
        while (carsGone <= 1000) {
            TrafficLightColor trafficLightColor = trafficLightColors[colorIndex];
            int msToWait = colorToTimeoutMap.get(trafficLightColor);
            try {
                Thread.sleep(msToWait);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            colorIndex = colorIndex == 3 ? 0 : colorIndex + 1;
            synchronized (this) {
                notify();
            }
        }
    }

    public TrafficLightColor getTrafficLightColor() {
        return trafficLightColors[colorIndex];
    }

    public synchronized void increaseCarsGone() {
        carsGone++;
    }

    public int getCarsGone() {
        return carsGone;
    }
}
