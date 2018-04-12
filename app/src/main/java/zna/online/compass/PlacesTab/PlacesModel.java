package zna.online.compass.PlacesTab;

import zna.online.compass.R;

public class PlacesModel {

    public String id;
    public String notes;
    public String address;
    public String type;
    public String workingHours;
    public float coordinatesLat;
    public float coordinatesLon;
    public String name;
    public int averageCheck;
    public double rate;

    public void setDistance(float distance) {
        int buf = (int)(distance + 50) / 100;
        this.distance = buf / 10f;
    }

    public float distance;

    public String getName() {
        if (name.length() > 11)
        {
            String str = name.substring(0, 11);
            str += "...";
            return str;
        }
        return name;
    }

    public String getNotes() {
        if (notes.length() > 22)
        {
            String str = notes.substring(0, 22);
            str += "...";
            return str;
        }
        return notes;
    }

    public String getAverageCheck() {
        return averageCheck + " UAH";
    }

    public PlacesModel() {
    }

    public PlacesModel(String id, String notes, String address, String type, String workingHours, float coordinatesLat, float coordinatesLon, String name, int averageCheck, double rate, float distance) {
        this.id = id;
        this.notes = notes;
        this.address = address;
        this.type = type;
        this.workingHours = workingHours;
        this.coordinatesLat = coordinatesLat;
        this.coordinatesLon = coordinatesLon;
        this.name = name;
        this.averageCheck = averageCheck;
        this.rate = rate;
        this.distance = distance;
    }

    public int getColorRate() {
        int colorRate;
        if (rate < 4){
            colorRate = R.color.colorBadMark;
        }else if(rate > 7){
            colorRate = R.color.colorBestMark;
        }else{
            colorRate = R.color.colorGoodMark;
        }
        return colorRate;
    }
}
