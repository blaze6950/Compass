package zna.online.compass;

public class PlacesAndEventsModel {
    String id, idCategory, name, mainPhoto, averageCheck, notes, dateStart, dateEnd, address, type, workingHours, Coordinates;

    public PlacesAndEventsModel(String id, String idCategory, String name, String mainPhoto, String averageCheck, String notes, String dateStart, String dateEnd, String address, String type, String workingHours, String coordinates) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
        this.mainPhoto = mainPhoto;
        this.averageCheck = averageCheck;
        this.notes = notes;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.address = address;
        this.type = type;
        this.workingHours = workingHours;
        Coordinates = coordinates;
    }

    public PlacesAndEventsModel() {
    }
}
