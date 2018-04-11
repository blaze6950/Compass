package zna.online.compass.PlacesTab;

import zna.online.compass.R;

public class PlacesModel {

    public String id;

    public String getName() {
        if (name.length() > 11)
        {
            String str = name.substring(0, 11);
            str += "...";
            return str;
        }
        return name;
    }

    public String name;

    public String getNotes() {
        if (notes.length() > 22)
        {
            String str = notes.substring(0, 22);
            str += "...";
            return str;
        }
        return notes;
    }

    public String notes;
    public String address;
    public String type;
    public String workingHours;
    public String coordinates;

    public String getAverageCheck() {
        return averageCheck + " UAH";
    }

    public int averageCheck;
    public double rate;

    public PlacesModel() {
    }

    public PlacesModel(String id, String name, int averageCheck, String notes, String address, String type, String workingHours, String coordinates, double rate) {
        this.id = id;
        this.name = name;
        this.averageCheck = averageCheck;
        this.notes = notes;
        this.address = address;
        this.type = type;
        this.workingHours = workingHours;
        this.coordinates = coordinates;
        this.rate = rate;
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
