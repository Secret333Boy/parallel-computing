package trafficlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TrafficLight trafficLight = new TrafficLight();

        trafficLight.start();

        List<Car1> carList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Car1 car = new Car1(trafficLight);
            carList.add(car);
            car.start();
        }
        try {
            trafficLight.join();
            for (Car1 car : carList) {
                car.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class TrafficLight extends Thread {
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

enum TrafficLightColor {
    GREEN,
    YELLOW,
    RED
}

class Car extends Thread {
    private final TrafficLight trafficLight;

    public Car(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public void run() {
        while (trafficLight.getCarsGone() <= 1000) {
            try {
                synchronized (trafficLight) {
                    while (trafficLight.getTrafficLightColor() != TrafficLightColor.GREEN) {
                        trafficLight.wait();
                    }

                    go();
                    trafficLight.increaseCarsGone();
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void go() throws InterruptedException {
        Thread.sleep(2);
        System.out.println("Car " + Thread.currentThread().threadId() + " went through traffic light. " + "Cars gone: " + trafficLight.getCarsGone());
    }
}
