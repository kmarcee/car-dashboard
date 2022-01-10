package dashboard;

import org.jetbrains.annotations.Nullable;

public interface DataDisplay {

    /**
     * Displays the measured physical values passed as input on the car's dashboard.
     * @param values the measured physical values delimited by pipe, i.e. '|' characters
     */
    void carDashboard(@Nullable String values);

}
