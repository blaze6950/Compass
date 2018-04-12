package zna.online.compass.PlacesTab;

public class Coordinates {
    public String id;
    public double coordinateLat, coordinateLon;

    public Coordinates() {
    }

    public Coordinates(String id, double coordinateLat, double coordinateLon) {
        this.id = id;
        this.coordinateLat = coordinateLat;
        this.coordinateLon = coordinateLon;
    }
}
