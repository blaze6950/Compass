package zna.online.compass.PlacesTab;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.BestLocation;
import zna.online.compass.BestLocationNew;
import zna.online.compass.R;

public class PlacesModelList {

    private BestLocation bestLocation;
    private BestLocationNew bestLocationNew;


    private Context context;
    private List<PlacesModel> PlacesModelList;
    private PlacesFragment placesFragment;

    private DatabaseReference databaseReference;

    public PlacesModelList(Context context, PlacesFragment fragment) {
        this.context = context;
        this.placesFragment = fragment;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Places");
        PlacesModelList = new ArrayList<>();

        UpdateList();
    }

    private void UpdateList() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlacesModelList.add(dataSnapshot.getValue(PlacesModel.class));
                placesFragment.UpdateList();
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

    public List<PlacesModel> getList()
    {
        List<PlacesModel> newPlacesModelList;


        //первый способ получение локации с помощью GPS и Internet провайдеров
        bestLocation = new BestLocation(context);
        Location currentLoc = bestLocation.getLastBestLocation();
        if  (currentLoc == null){
            Toast.makeText(context, context.getString(R.string.error_get_location), Toast.LENGTH_LONG).show();
            return null;
        }

        //второй способ получить локацию, требуется 19 minsdk
        //bestLocationNew = new BestLocationNew(context);
        //Location currentLoc = bestLocationNew.getLastLocation();


        //UpdateList(PlacesModelList);
        for (PlacesModel placesModel:PlacesModelList) {
            placesModel.setDistance(getDistance((float)currentLoc.getLongitude(), (float)currentLoc.getLatitude(), placesModel.coordinatesLon, placesModel.coordinatesLat));
        }
        doSort(PlacesModelList, 0, PlacesModelList.size() - 1, currentLoc);
        newPlacesModelList = getMoreList(0);

        return newPlacesModelList;
    }

    public List<PlacesModel> getMoreList(int lastIndex)
    {
        List<PlacesModel> newPlacesModelList = null;
        if (PlacesModelList.size() > 0){
            newPlacesModelList = new ArrayList<>();

            for (int i = 0; i < lastIndex + 10; i++)
            {
                if (PlacesModelList.size() == i)
                {
                    break;
                }
                newPlacesModelList.add(PlacesModelList.get(i));
            }
        }

        return newPlacesModelList;
    }

    private float getDistance(float lon, float lat, float lon2, float lat2)
    {
        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(lon);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);
        float res = locationA.distanceTo(locationB);

        return res;
    }

    private void doSort(List<PlacesModel> array, int start, int end, Location location) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (getDistance(array.get(i).coordinatesLon, array.get(i).coordinatesLat, (float)location.getLongitude(), (float)location.getLatitude()) <= getDistance(array.get(cur).coordinatesLon, array.get(cur).coordinatesLat, (float)location.getLongitude(), (float)location.getLatitude()))) {
                i++;
            }
            while (j > cur && (getDistance(array.get(cur).coordinatesLon, array.get(cur).coordinatesLat, (float)location.getLongitude(), (float)location.getLatitude()) <= getDistance(array.get(j).coordinatesLon, array.get(j).coordinatesLat, (float)location.getLongitude(), (float)location.getLatitude()))) {
                j--;
            }
            if (i < j) {
                PlacesModel temp = array.get(i);
                PlacesModel temp2 = array.get(j);
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
