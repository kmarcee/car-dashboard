package dashboard;

import java.util.Arrays;

public enum Gear {
    P("P", true, false, 0, 0, 0, 8000),
    R("R", true, true, -20, 0, 850, 8000),
    N("N", true, true, 0, 0, 800, 8000),
    D("D", true, false, 0, 350, 800, 8000),
    G1("1", true, true, 0, 70, 850, 8000),
    G2("2", true, true, 0, 110, 900, 8000),
    G3("3", false, true, 20, 140, 950, 8000),
    G4("4", false, true, 35, 170, 1000, 8000),
    G5("5", false, true, 45, 240, 1050, 8000),
    G6("6", false, true, 60, 350, 1100, 8000);

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

    /**
     * Parses a gear based on its value.
     * @param value the gear in textual format
     * @return the associated gear
     */
    public static Gear byValue(String value) {
        return Arrays.stream(values())
                .filter(gear -> gear.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isApplicableToAutomatic() {
        return applicableToAutomatic;
    }

    public boolean isApplicableToManual() {
        return applicableToManual;
    }

    public int getLowestSpeed() {
        return lowestSpeed;
    }

    public int getHighestSpeed() {
        return highestSpeed;
    }

    public int getLowestRpm() {
        return lowestRpm;
    }

    public int getHighestRpm() {
        return highestRpm;
    }
}
