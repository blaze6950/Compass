package zna.online.compass.EventsTab;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import zna.online.compass.PlaceAndEvent;
import zna.online.compass.R;
import zna.online.compass.Test.TestResult;
import zna.online.compass.Test.TestResultList;

public class EventsModel extends PlaceAndEvent {

    public String id;
    public String notes;
    public String address;
    public String type;
    public String workingHours;
    public String coordinatesLat;
    public String coordinatesLon;
    public double distance;
    public String name;
    public int averageCheck;
    public double rate;

    private TestResult test;

    public String getAverageCheck() {
        return averageCheck + " UAH";
    }

    public void setAverageCheck(int averageCheck) {
        this.averageCheck = averageCheck;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public double getDistanceDouble() {
        return distance;
    }

    public String getDistance() {
        int buf = (int)((this.distance + 50) / 100);
        String res = String.valueOf((buf / 10.)) + " км";
        return res;
    }

    public EventsModel(String id, String notes, String address, String type, String workingHours, String coordinatesLat, String coordinatesLon, double distance, String name, int averageCheck, double rate) {
        this.id = id;
        this.notes = notes;
        this.address = address;
        this.type = type;
        this.workingHours = workingHours;
        this.coordinatesLat = coordinatesLat;
        this.coordinatesLon = coordinatesLon;
        this.distance = distance;
        this.name = name;
        this.averageCheck = averageCheck;
        this.rate = rate;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public EventsModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String getCoordinatesLat() {
        return coordinatesLat;
    }

    public void setCoordinatesLat(String coordinatesLat) {
        this.coordinatesLat = coordinatesLat;
    }

    @Override
    public String getCoordinatesLon() {
        return coordinatesLon;
    }

    public void setCoordinatesLon(String coordinatesLon) {
        this.coordinatesLon = coordinatesLon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestResult getTest() {
        return test;
    }

    public void setTest(TestResult test) {
        this.test = test;
    }

    @Override
    public void LoadTestResult(final TestResultList testResultList) {
        super.LoadTestResult(testResultList);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(id).child("test");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                test = dataSnapshot.getValue(TestResult.class);
                testResultList.checkConfigTest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getColorRate() {
        int colorRate;
        if (rate < 4){
            colorRate = R.drawable.rate_bad_mark;
        }else if(rate > 7){
            colorRate = R.drawable.rate_best_mark;
        }else{
            colorRate = R.drawable.rate_good_mark;
        }
        return colorRate;
    }

    public TestResult getTestResult() {
        return test;
    }
}
