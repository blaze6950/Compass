package zna.online.compass.Test;

import android.content.Context;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.BestLocation;
import zna.online.compass.EventsTab.EventsModel;
import zna.online.compass.PlaceAndEvent;
import zna.online.compass.PlacesTab.PlacesModel;
import zna.online.compass.R;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class TestResultList {

    public TestResultList(Context context, TestResultActivity testResultActivity, TestResult sourceTestResult) {
        this.context = context;
        this.testResultActivity = testResultActivity;
        this.sourceTestResult = sourceTestResult;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Places");
        placeAndEventList = new ArrayList<>();
        //swipeRefreshLayout = (SwipeRefreshLayout)placesFragment.getActivity().findViewById(R.id.swipe_refresh_layout);
        UpdateList();
    }

    private BestLocation bestLocation;

    private Context context;
    private List<PlaceAndEvent> placeAndEventList;
    private TestResultActivity testResultActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TestResult sourceTestResult;

    private DatabaseReference databaseReference;

    private void UpdateList()
    {
        placeAndEventList.clear();
        LoadDataWithTestResult();
    }

    private TestResultList GetThis(){
        return this;
    }

    private void LoadDataWithTestResult() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Places");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlacesModel placeModel = dataSnapshot.getValue(PlacesModel.class);
                placeModel.LoadTestResult(GetThis());
                placeAndEventList.add(placeModel);
                testResultActivity.UpdateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                EventsModel eventModel = dataSnapshot.getValue(EventsModel.class);
                eventModel.LoadTestResult(GetThis());
                placeAndEventList.add(eventModel);
                testResultActivity.UpdateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean getList(List<PlaceAndEvent> newPlacesModelList)
    {
        newPlacesModelList.clear();

        //первый способ получение локации с помощью GPS и Internet провайдеров
        bestLocation = new BestLocation(context);
        Location currentLoc = bestLocation.getLastBestLocation();
        if  (currentLoc == null){
            Toast.makeText(context, context.getString(R.string.error_get_location), Toast.LENGTH_LONG).show();
            return false;
        }

        for (PlaceAndEvent placeAndEventModel:placeAndEventList) {
            placeAndEventModel.setDistance(getDistance(String.valueOf(currentLoc.getLongitude()), String.valueOf(currentLoc.getLatitude()), placeAndEventModel.getCoordinatesLon(), placeAndEventModel.getCoordinatesLat()));
        }
        doSort(placeAndEventList, 0, placeAndEventList.size() - 1, currentLoc);
        getMoreList(0, newPlacesModelList);

        return true;
    }

    public void checkConfigTest() {
        for (PlaceAndEvent placeAndEventModel:placeAndEventList) {
            if (placeAndEventModel instanceof PlacesModel){
                PlacesModel placesModel = (PlacesModel) placeAndEventModel;
                TestResult testResult = placesModel.getTestResult();
                if (testResult == null)
                    break;
                if (!sourceTestResult.CompareTestResults(testResult)){
                    placeAndEventList.remove(placeAndEventModel);
                }
            }else{
                EventsModel eventsModel = (EventsModel) placeAndEventModel;
                TestResult testResult = eventsModel.getTestResult();
                if (testResult == null)
                    break;
                if (!sourceTestResult.CompareTestResults(testResult)){
                    placeAndEventList.remove(placeAndEventModel);
                }
            }
        }
    }


    public boolean getMoreList(int lastIndex, List<PlaceAndEvent> newPlacesModelList)
    {
        if (placeAndEventList.size() > 0 && placeAndEventList.size() > newPlacesModelList.size()){

            for (int i = lastIndex; i < lastIndex + 10; i++)
            {
                if (placeAndEventList.size() == i)
                {
                    break;
                }
                newPlacesModelList.add(placeAndEventList.get(i));
            }
            return true;
        }
        return false;
    }

    private double getDistance(String lon, String lat, String lon2, String lat2)
    {
        LatLng locationA = new LatLng(Double.parseDouble(lon), Double.parseDouble(lat));
        LatLng locationB = new LatLng(Double.parseDouble(lon2), Double.parseDouble(lat2));
        double res = computeDistanceBetween(locationA, locationB);
        return res;
    }

    private void doSort(List<PlaceAndEvent> array, int start, int end, Location location) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && ((array.get(i).getDistanceDouble()) <= (array.get(cur).getDistanceDouble()))) {
                i++;
            }
            while (j > cur && ((array.get(cur).getDistanceDouble()) <= (array.get(j).getDistanceDouble()))) {
                j--;
            }
            if (i < j) {
                PlaceAndEvent temp = array.get(i);
                PlaceAndEvent temp2 = array.get(j);
                array.remove(i);
                array.add(i, temp2);
                array.remove(j);
                array.add(j, temp);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort(array, start, cur, location);
        doSort(array, cur+1, end, location);
    }
}
