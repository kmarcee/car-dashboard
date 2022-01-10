package dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CarDisplayTest {

    CarDisplay carDisplay;

    @BeforeEach
    void setUp() {
        carDisplay = new CarDisplay(true);
    }

    @Test
    void carDashboard_inputIsValid_noExceptionIsThrown() {
        assertDoesNotThrow(() -> carDisplay.carDashboard("100|3200|2.3|D|true|false|true"));
    }

    @Test
    void carDashboard_inputIsNull_illegalArgumentExceptionThrown() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> carDisplay.carDashboard(null)
        );
        assertEquals("The input shall not be null.", thrown.getMessage());
    }

    @Test
    void carDashboard_speedIsMissing_illegalArgumentExceptionThrown() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> carDisplay.carDashboard("3200|2.3|D|true|false|true")
        );
        assertEquals("Unexpected number of parameters in the input.", thrown.getMessage());
    }

    @Test
    void carDashboard_speedIsUndefined_numberFormatExceptionThrown() {
        assertThrows(
                NumberFormatException.class,
                () -> carDisplay.carDashboard("|3200|2.3|D|true|false|true")
        );
    }

    @Test
    void carDashboard_speedIsNotNumeric_numberFormatExceptionThrown() {
        assertThrows(
                NumberFormatException.class,
                () -> carDisplay.carDashboard("20ABC|3200|2.3|D|true|false|true")
        );
    }

    @Test
    void carDashboard_speedIsFloat_numberFormatExceptionThrown() {
        assertThrows(
                NumberFormatException.class,
                () -> carDisplay.carDashboard("100.34|3200|2.3|D|true|false|true")
        );
    }

    @Test
    void carDashboard_speedIsHigherThanIntegerMaxValue_numberFormatExceptionThrown() {
        assertThrows(
                NumberFormatException.class,
                () -> carDisplay.carDashboard("2147483648|3200|2.3|D|true|false|true")
        );
    }

    @Test
    void carDashboard_speedHasLowestAllowedValue_noExceptionIsThrown() {
        assertDoesNotThrow(() -> carDisplay.carDashboard("-20|2400|-1.2|R|true|false|true"));
    }

    @Test
    void carDashboard_speedIsOutOfRangeLowerBoundary_illegalArgumentExceptionThrown() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> carDisplay.carDashboard("-21|3200|-2.3|R|true|false|true")
        );
        assertEquals("The speed is out of range.", thrown.getMessage());
    }

    @Test
    void carDashboard_speedHasHighestAllowedValue_noExceptionIsThrown() {
        assertDoesNotThrow(() -> carDisplay.carDashboard("350|7200|0.2|D|true|false|true"));
    }

    @Test
    void carDashboard_speedIsOutOfRangeUpperBoundary_illegalArgumentExceptionThrown() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> carDisplay.carDashboard("351|7200|0.2|D|true|false|true")
        );
        assertEquals("The speed is out of range.", thrown.getMessage());
    }

    @Test
    void carDashboard_speedIsNotZeroWhileEngineIsNotStarted_illegalStateExceptionThrown() {
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> carDisplay.carDashboard("10|0|0|N|true|false|false")
        );
        assertEquals("Engine is not running.", thrown.getMessage());
    }

    @Test
    void carDashboard_speedIsZeroWhileEngineIsNotStarted_noExceptionIsThrown() {
        assertDoesNotThrow(() -> carDisplay.carDashboard("0|0|0|P|false|false|false"));
    }

    @Test
    void carDashboard_speedAndGearAndRpmCombinationIsNotReasonable_illegalStateExceptionThrown() {
        CarDisplay manualTransmissionCarDisplay = new CarDisplay(false);
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> manualTransmissionCarDisplay.carDashboard("200|2000|0|2|true|false|true")
        );
        assertEquals("Speed does not correlate to the current gear.", thrown.getMessage());
    }

    @Test
    void carDashboard_outlierSpeedValue_illegalStateExceptionThrown() {
        carDisplay.carDashboard("10|2000|1.13|D|true|false|true");
        carDisplay.carDashboard("12|2100|1.2|D|true|false|true");
        carDisplay.carDashboard("15|2340|1.4|D|true|false|true");

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> carDisplay.carDashboard("120|2283|1.3|D|true|false|true")
        );
        assertEquals("Outlier speed value based on history.", thrown.getMessage());
    }

    @Test
    void carDashboard_outlierSpeedValueWithLongHistory_illegalStateExceptionThrown() {
        carDisplay.carDashboard("10|2000|1.13|D|true|false|true");
        carDisplay.carDashboard("12|2100|1.2|D|true|false|true");
        carDisplay.carDashboard("15|2340|1.4|D|true|false|true");
        carDisplay.carDashboard("16|2360|1.42|D|true|false|true");
        carDisplay.carDashboard("20|2120|1.25|D|true|false|true");
        carDisplay.carDashboard("22|2068|1.12|D|true|false|true");

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> carDisplay.carDashboard("120|2283|1.3|D|true|false|true")
        );
        assertEquals("Outlier speed value based on history.", thrown.getMessage());
    }

    @Test
    void carDashboard_outlierSpeedValueWithLongHistory_outlierIgnored() {
        carDisplay.carDashboard("10|2000|1.13|D|true|false|true");
        carDisplay.carDashboard("12|2100|1.2|D|true|false|true");
        carDisplay.carDashboard("15|2340|1.4|D|true|false|true");
        assertThrows(
                IllegalStateException.class,
                () -> carDisplay.carDashboard("200|2360|1.42|D|true|false|true")
        );
        carDisplay.carDashboard("20|2120|1.25|D|true|false|true");
        carDisplay.carDashboard("22|2068|1.12|D|true|false|true");

        List<Integer> speedHistory = carDisplay.getHistoricalValues().stream()
                .map(DashboardData::getSpeed)
                .collect(Collectors.toList());
        assertEquals(5, speedHistory.size());
        assertEquals(10, speedHistory.get(0));
        assertEquals(12, speedHistory.get(1));
        assertEquals(15, speedHistory.get(2));
        assertEquals(20, speedHistory.get(3));
        assertEquals(22, speedHistory.get(4));
    }
}
