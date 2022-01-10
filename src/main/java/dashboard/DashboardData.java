package dashboard;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DashboardData {

    private static final int NUMBER_OF_PARAMETERS = 7;

    private int speed;
    private int rpm;
    private float acceleration;
    private Gear gear;
    private boolean headlights;
    private boolean lowOilLevel;
    private boolean engineStarted;

    public static DashboardData fromString(@Nullable String values) {
        DashboardData dashboardData = new DashboardData();

        if (values == null) {
            throw new IllegalArgumentException("The input shall not be null.");
        }

        List<String> splitValues = Arrays.asList(values.split("\\|"));

        if (splitValues.size() != NUMBER_OF_PARAMETERS) {
            throw new IllegalArgumentException("Unexpected number of parameters in the input.");
        }

        dashboardData.setSpeed(Integer.parseInt(splitValues.get(0)));
        dashboardData.setRpm(Integer.parseInt(splitValues.get(1)));
        dashboardData.setAcceleration(Float.parseFloat(splitValues.get(2)));
        dashboardData.setGear(Gear.byValue(splitValues.get(3)));
        dashboardData.setHeadlights(Boolean.parseBoolean(splitValues.get(4)));
        dashboardData.setLowOilLevel(Boolean.parseBoolean(splitValues.get(5)));
        dashboardData.setEngineStarted(Boolean.parseBoolean(splitValues.get(6)));

        return dashboardData;
    }

    @Override
    public String toString() {
        return "DashboardData{" +
                "speed=" + speed +
                ", rpm=" + rpm +
                ", acceleration=" + acceleration +
                ", gear='" + gear + '\'' +
                ", headlights=" + headlights +
                ", lowOilLevel=" + lowOilLevel +
                ", engineStarted=" + engineStarted +
                '}';
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public boolean isHeadlights() {
        return headlights;
    }

    public void setHeadlights(boolean headlights) {
        this.headlights = headlights;
    }

    public boolean isLowOilLevel() {
        return lowOilLevel;
    }

    public void setLowOilLevel(boolean lowOilLevel) {
        this.lowOilLevel = lowOilLevel;
    }

    public boolean isEngineStarted() {
        return engineStarted;
    }

    public void setEngineStarted(boolean engineStarted) {
        this.engineStarted = engineStarted;
    }
}
