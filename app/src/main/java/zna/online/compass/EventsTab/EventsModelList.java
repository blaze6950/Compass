package zna.online.compass.EventsTab;

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

public class EventsModelList {
    private BestLocation bestLocation;
    private BestLocationNew bestLocationNew;


    private Context context;
    private List<EventsModel> EventsModelList;
    private EventsFragment eventsFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseReference databaseReference;
    private ValueEventListener currentValueEventListener;

    public EventsModelList(Context context, EventsFragment fragment) {
        this.context = context;
        this.eventsFragment = fragment;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Events");
        EventsModelList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout)eventsFragment.getActivity().findViewById(R.id.swipe_refresh_layout_events);
        UpdateList();
    }

    private void UpdateList() {
        EventsModelList.clear();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                swipeRefreshLayout.setRefreshing(true);
                EventsModelList.add(dataSnapshot.getValue(EventsModel.class));
                eventsFragment.UpdateList();
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

    public boolean getList(List<EventsModel> newEventsModelList)
    {
        newEventsModelList.clear();
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

        for (EventsModel eventsModel:EventsModelList) {
            eventsModel.setDistance(getDistance(String.valueOf(currentLoc.getLongitude()), String.valueOf(currentLoc.getLatitude()), eventsModel.coordinatesLon, eventsModel.coordinatesLat));
        }
        doSort(EventsModelList, 0, EventsModelList.size() - 1, currentLoc);
        getMoreList(0, newEventsModelList);

        return true;
    }

    public boolean getMoreList(int lastIndex, List<EventsModel> newEventsModelList)
    {
        if (EventsModelList.size() > 0 && EventsModelList.size() > newEventsModelList.size()){

            for (int i = lastIndex; i < lastIndex + 10; i++)
            {
                if (EventsModelList.size() == i)
                {
                    break;
                }
                newEventsModelList.add(EventsModelList.get(i));
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

    private void doSort(List<EventsModel> array, int start, int end, Location location) {
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
                EventsModel temp = array.get(i);
                EventsModel temp2 = array.get(j);
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

    public List<EventsModel> getList()
    {
        List<EventsModel> newEventsModelList;


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
        for (EventsModel eventsModel:EventsModelList) {
            eventsModel.setDistance(getDistance(String.valueOf(currentLoc.getLongitude()), String.valueOf(currentLoc.getLatitude()), eventsModel.coordinatesLon, eventsModel.coordinatesLat));
        }
        doSort(EventsModelList, 0, EventsModelList.size() - 1, currentLoc);
        newEventsModelList = getMoreList(0);

        return newEventsModelList;
    }

    public List<EventsModel> getMoreList(int lastIndex)
    {
        List<EventsModel> newEventsModelList = null;
        if (EventsModelList.size() > 0){
            newEventsModelList = new ArrayList<>();

            for (int i = 0; i < lastIndex + 10; i++)
            {
                if (EventsModelList.size() == i)
                {
                    break;
                }
                newEventsModelList.add(EventsModelList.get(i));
            }
        }

        return newEventsModelList;
    }
}
