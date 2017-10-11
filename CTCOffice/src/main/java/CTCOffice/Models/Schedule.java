package CTCOffice.Models;

import java.util.List;

public class Schedule {
    private List<Stop> stops;

    public List<Stop> getStops() {
        return stops;
    }

    public boolean addStop(Stop stop) {
        return stops.add(stop);
    }

    public boolean removeStop(Stop stop) {
        return stops.remove(stop);
    }
}
