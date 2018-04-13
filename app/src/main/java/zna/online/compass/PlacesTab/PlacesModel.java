package zna.online.compass.PlacesTab;

import android.os.Parcel;
import android.os.Parcelable;

import zna.online.compass.R;

public class PlacesModel implements Parcelable{

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
    public float distance;

    public void setDistance(float distance) {
        int buf = (int)(distance + 50) / 100;
        this.distance = buf / 10f;
    }

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

    public PlacesModel(Parcel in) {
        String[] data = new String[11];
        in.readStringArray(data);
        this.id = data[0];
        this.notes = data[1];
        this.address = data[2];
        this.type = data[3];
        this.workingHours = data[4];
        this.coordinatesLat = Float.parseFloat(data[5]);
        this.coordinatesLon = Float.parseFloat(data[6]);
        this.name = data[7];
        this.averageCheck = Integer.parseInt(data[8]);
        this.rate = Double.parseDouble(data[9]);
        this.distance = Float.parseFloat(data[10]);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { id, notes, address, type, workingHours, String.valueOf(coordinatesLat), String.valueOf(coordinatesLon), name, String.valueOf(averageCheck), String.valueOf(rate), String.valueOf(distance)});
    }

    public static final Parcelable.Creator<PlacesModel> CREATOR = new Parcelable.Creator<PlacesModel>() {

        @Override
        public PlacesModel createFromParcel(Parcel source) {
            return new PlacesModel(source);
        }

        @Override
        public PlacesModel[] newArray(int size) {
            return new PlacesModel[size];
        }
    };
}
