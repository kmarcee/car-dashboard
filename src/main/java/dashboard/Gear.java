package dashboard;

import java.util.Arrays;

public enum Gear {
    P("P", true, false, 0, 0, 0, 3000),
    R("R", true, true, -20, 0, 800, 4000),
    N("N", true, false, 0, 0, 800, 3000),
    D("D", true, false, 0, 350, 800, 8000),
    G1("1", true, true, 0, 70, 800, 8000),
    G2("2", true, true, 0, 110, 800, 8000),
    G3("3", false, true, 20, 140, 800, 8000),
    G4("4", false, true, 35, 170, 800, 8000),
    G5("5", false, true, 45, 240, 800, 8000),
    G6("6", false, true, 60, 350, 800, 8000);

    private final String value;
    private final boolean applicableToAutomatic;
    private final boolean applicableToManual;
    private final int lowestSpeed;
    private final int highestSpeed;
    private final int lowestRpm;
    private final int highestRpm;

    Gear(String value,
         boolean applicableToAutomatic,
         boolean applicableToManual,
         int lowestSpeed,
         int highestSpeed,
         int lowestRpm,
         int highestRpm)
    {
        this.value = value;
        this.applicableToAutomatic = applicableToAutomatic;
        this.applicableToManual = applicableToManual;
        this.lowestSpeed = lowestSpeed;
        this.highestSpeed = highestSpeed;
        this.lowestRpm = lowestRpm;
        this.highestRpm = highestRpm;
    }

    public static Gear byValue(String value) {
        return Arrays.stream(values())
                .filter(gear -> gear.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void checkIfAcceptable(boolean automatic, int speed, int rpm) {
        if ((automatic && !applicableToAutomatic) || (!automatic && !applicableToManual)) {
            throw new IllegalStateException("Invalid gear for the selected transmission type.");
        }

        if (speed < lowestSpeed || speed > highestSpeed) {
            throw new IllegalStateException("Speed does not correlate to the current gear.");
        }

        if (rpm < lowestRpm || rpm > highestRpm) {
            throw new IllegalStateException("RPM does not correlate to the current gear.");
        }
    }
}
