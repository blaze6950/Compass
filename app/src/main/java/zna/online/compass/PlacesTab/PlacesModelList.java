package zna.online.compass.PlacesTab;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.BestLocation;
import zna.online.compass.BestLocationNew;
import zna.online.compass.R;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class PlacesModelList {

    private BestLocation bestLocation;
    private BestLocationNew bestLocationNew;


    private Context context;
    private List<PlacesModel> PlacesModelList;
    private PlacesFragment placesFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseReference databaseReference;
    private ValueEventListener currentValueEventListener;

    public PlacesModelList(Context context, PlacesFragment fragment) {
        this.context = context;
        this.placesFragment = fragment;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Places");
        PlacesModelList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout)placesFragment.getActivity().findViewById(R.id.swipe_refresh_layout);
        UpdateList();
    }

    private void UpdateList() {
        PlacesModelList.clear();
        // Most viewed posts
//        if (currentValueEventListener != null)
//            databaseReference.removeEventListener(currentValueEventListener);
//
//        currentValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                swipeRefreshLayout.setRefreshing(true);
//                PlacesModelList.add(dataSnapshot.getValue(PlacesModel.class));
//                placesFragment.UpdateList();
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addListenerForSingleValueEvent(currentValueEventListener);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                swipeRefreshLayout.setRefreshing(true);
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

    public boolean getList(List<PlacesModel> newPlacesModelList)
    {
        newPlacesModelList.clear();
        //первый способ получение локации с помощью GPS и Internet провайдеров
        bestLocation = new BestLocation(context);
        Location currentLoc = bestLocation.getLastBestLocation();
        if  (currentLoc == null){
            Toast.makeText(context, context.getString(R.string.error_get_location), Toast.LENGTH_LONG).show();
            return false;
        }

        //второй способ получить локацию, требуется 19 minsdk
        //bestLocationNew = new BestLocationNew(context);
        //Location currentLoc = bestLocationNew.getLastLocation();

        for (PlacesModel placesModel:PlacesModelList) {
            placesModel.setDistance(getDistance(String.valueOf(currentLoc.getLongitude()), String.valueOf(currentLoc.getLatitude()), placesModel.coordinatesLon, placesModel.coordinatesLat));
        }
        doSort(PlacesModelList, 0, PlacesModelList.size() - 1, currentLoc);
        getMoreList(0, newPlacesModelList);

        return true;
    }

    public boolean getMoreList(int lastIndex, List<PlacesModel> newPlacesModelList)
    {
        if (PlacesModelList.size() > 0 && PlacesModelList.size() > newPlacesModelList.size()){

            for (int i = lastIndex; i < lastIndex + 10; i++)
            {
                if (PlacesModelList.size() == i)
                {
                    break;
                }
                newPlacesModelList.add(PlacesModelList.get(i));
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
//        Location locationA = new Location("point A");
//        locationA.setLatitude(Double.valueOf(lat));
//        locationA.setLongitude(Double.valueOf(lon));
//        Location locationB = new Location("point B");
//        locationB.setLatitude(Double.valueOf(lat2));
//        locationB.setLongitude(Double.valueOf(lon2));
//        double res = locationA.distanceTo(locationB);
        return res;
    }

    private void doSort(List<PlacesModel> array, int start, int end, Location location) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && ((array.get(i).distance) <= (array.get(cur).distance))) {
                i++;
            }
            while (j > cur && ((array.get(cur).distance) <= (array.get(j).distance))) {
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
            placesModel.setDistance(getDistance(String.valueOf(currentLoc.getLongitude()), String.valueOf(currentLoc.getLatitude()), placesModel.coordinatesLon, placesModel.coordinatesLat));
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
}
