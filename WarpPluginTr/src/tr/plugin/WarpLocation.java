package tr.moon.warp;

import org.bukkit.Location;

public class WarpLocation {
    private String name;
    private Location location;

    public WarpLocation(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}