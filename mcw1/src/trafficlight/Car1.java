package trafficlight;

public class Car1 extends Thread {
    private final TrafficLight trafficLight;

    public Car1(TrafficLight trafficLight) {
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
