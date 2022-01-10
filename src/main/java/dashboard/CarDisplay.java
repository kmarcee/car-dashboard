package dashboard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static dashboard.DashboardData.fromString;

public class CarDisplay implements DataDisplay {

    private static final int HISTORY_MOVING_WINDOW_LENGTH = 5;
    private static final int MAX_ALLOWED_DELTA_SPEED = 5;
    private static final int LOWEST_SPEED = -20;
    private static final int HIGHEST_SPEED = 350;

    private final boolean automaticGear;
    private final DashboardData[] historicalValues;

    CarDisplay(boolean hasAutomaticGear) {
        automaticGear = hasAutomaticGear;
        historicalValues = new DashboardData[HISTORY_MOVING_WINDOW_LENGTH];
    }

    public List<DashboardData> getHistoricalValues() {
        return Arrays.asList(historicalValues);
    }

    @Override
    public void carDashboard(@Nullable String values) {
        DashboardData dashboardData = fromString(values);

        validateSpeed(dashboardData);

        // TODO: validate other values, too

        validateCorrelations(dashboardData);

        validateAgainstHistory(dashboardData);
        updateHistory(dashboardData);

        display(dashboardData);
    }

    /**
     * This method calls the display unit to drive/control actuators, move hands, display digital values, etc.
     * @param dashboardData the parsed and validated values of all measured quantities
     */
    void display(@NotNull DashboardData dashboardData) {
        // call display unit to render data (move hands, display digital values, etc.)
        System.out.println(dashboardData);
    }

    /**
     * Validates the speed value against its constraints. This includes range checks and other integrity checks
     * in correlation of other measured values.
     * @param dashboardData the parsed input data
     */
    private void validateSpeed(@NotNull DashboardData dashboardData) {
        if (dashboardData.getSpeed() < LOWEST_SPEED || dashboardData.getSpeed() > HIGHEST_SPEED) {
            throw new IllegalArgumentException("The speed is out of range.");
        }

        if (dashboardData.getSpeed() != 0 && !dashboardData.isEngineStarted()) {
            throw new IllegalStateException("Engine is not running.");
        }
    }

    /**
     * Validates the correlational constraints between parameters.
     * @param dashboardData the parsed input data
     */
    private void validateCorrelations(@NotNull DashboardData dashboardData) {
        dashboardData.getGear().checkIfAcceptable(automaticGear, dashboardData.getSpeed(), dashboardData.getRpm());
    }

    /**
     * Checks if the current values fit into the series of historical values.
     * If they are outliers, they will be ignored.
     * @param dashboardData the parsed and validated input data
     */
    private void validateAgainstHistory(@NotNull DashboardData dashboardData) {
        if (getEffectiveLengthOfHistory() > 0 &&
                Math.abs(dashboardData.getSpeed() -
                        historicalValues[getEffectiveLengthOfHistory() - 1].getSpeed()) > MAX_ALLOWED_DELTA_SPEED) {
            throw new IllegalStateException("Outlier speed value based on history.");
        }
    }

    /**
     * Stores the current values for permanent access.
     * @param dashboardData the parsed and validated input data
     */
    private void updateHistory(@NotNull DashboardData dashboardData) {
        int effectiveLength = getEffectiveLengthOfHistory();
        if (effectiveLength < HISTORY_MOVING_WINDOW_LENGTH) {
            historicalValues[effectiveLength] = dashboardData;
        } else {
            historicalValues[0] = historicalValues[1];
            historicalValues[1] = historicalValues[2];
            historicalValues[2] = historicalValues[3];
            historicalValues[3] = historicalValues[4];
            historicalValues[4] = dashboardData;
        }
    }

    private int getEffectiveLengthOfHistory() {
        return (int) Arrays.stream(historicalValues).filter(Objects::nonNull).count();
    }
}
