package zna.online.compass.LeadersTab;

public class Profile {
    public String id;
    public String name;
    public String lastName;
    public int points;

    public Profile(String id, String name, String lastName, int points) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.points = points;
    }

    public Profile() {

    }
}
