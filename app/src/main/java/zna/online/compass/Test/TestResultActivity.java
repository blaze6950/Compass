package zna.online.compass.Test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.EventsTab.EventsModel;
import zna.online.compass.GlideApp;
import zna.online.compass.PlaceAndEvent;
import zna.online.compass.PlacesTab.PlacesModel;
import zna.online.compass.R;

public class TestResultActivity extends AppCompatActivity {

    private TestResult testResult;
    private TestResultList testResultList;
    private List<PlaceAndEvent> smallResultsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PlaceAndEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Результаты");
        setContentView(R.layout.activity_test_result);
        smallResultsList = new ArrayList<>();
        testResult = getIntent().getParcelableExtra("testResult");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_test_result);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                UpdateList();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_result);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PlaceAndEventAdapter(smallResultsList);

        recyclerView.setAdapter(adapter);

        testResultList = new TestResultList(this, this, testResult);

        UpdateList();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void UpdateList()
    {
        if (testResultList.getList(smallResultsList)){
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public class PlaceAndEventAdapter extends RecyclerView.Adapter<PlaceAndEventAdapter.PlaceAndEventHolder>{

        private List<PlaceAndEvent> list;
        private PlaceAndEventAdapter.PlaceAndEventHolder placeAndEventHolder;

        public PlaceAndEventAdapter(List<PlaceAndEvent> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public PlaceAndEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PlaceAndEventAdapter.PlaceAndEventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_places_and_events, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceAndEventHolder holder, int position) {
            PlaceAndEvent placeAndEvent = list.get(position);

            if (placeAndEvent instanceof PlacesModel){
                PlacesModel placesModel = (PlacesModel) placeAndEvent;
                holder.nameTextView.setText(placesModel.getName());
                holder.averageCheckTextView.setText(placesModel.getAverageCheck());
                holder.distanceTextView.setText(placesModel.getDistance());
                holder.notesTextView.setText(placesModel.getNotes());
                holder.typeTextView.setText(placesModel.type);
                holder.workingHoursTextView.setText(placesModel.workingHours);
                holder.rateButton.setText(placesModel.rate + "");

                holder.rateButton.setBackgroundResource(placesModel.getColorRate());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference().child("PlacesPhotos").child(placesModel.id).child("MainPhoto").child("1.jpg");
                if (storageRef != null){
                    GlideApp.with(holder.itemView.getContext())
                            .load(storageRef)
                            .into(holder.mainPhotoImageView);
                }
            }else{
                EventsModel eventsModel = (EventsModel) placeAndEvent;
                holder.nameTextView.setText(eventsModel.getName());
                holder.averageCheckTextView.setText(eventsModel.getAverageCheck());
                holder.distanceTextView.setText(eventsModel.getDistance());
                holder.notesTextView.setText(eventsModel.getNotes());
                holder.typeTextView.setText(eventsModel.type);
                holder.workingHoursTextView.setText(eventsModel.workingHours);
                holder.rateButton.setText(eventsModel.rate + "");

                holder.rateButton.setBackgroundResource(eventsModel.getColorRate());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference().child("PlacesPhotos").child(eventsModel.id).child("MainPhoto").child("1.jpg");
                if (storageRef != null){
                    GlideApp.with(holder.itemView.getContext())
                            .load(storageRef)
                            .into(holder.mainPhotoImageView);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            return -1;
        }

        public class PlaceAndEventHolder extends RecyclerView.ViewHolder{

            TextView nameTextView, typeTextView, workingHoursTextView, distanceTextView, averageCheckTextView, notesTextView;
            Button rateButton;
            ImageView mainPhotoImageView;
            RecyclerView recyclerView;

            public PlaceAndEventHolder(View itemView) {
                super(itemView);

                recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_places);
                nameTextView = (TextView) itemView.findViewById(R.id.textView_name);
                typeTextView = (TextView) itemView.findViewById(R.id.textView_type);
                workingHoursTextView = (TextView) itemView.findViewById(R.id.textView_working_hours);
                distanceTextView = (TextView) itemView.findViewById(R.id.textView_distance);
                averageCheckTextView = (TextView) itemView.findViewById(R.id.textView_average_check);
                notesTextView = (TextView) itemView.findViewById(R.id.textView_notes);

                rateButton = (Button) itemView.findViewById(R.id.button_rate);

                mainPhotoImageView = (ImageView) itemView.findViewById(R.id.imageView_photo);

            }
        }
    }

}
