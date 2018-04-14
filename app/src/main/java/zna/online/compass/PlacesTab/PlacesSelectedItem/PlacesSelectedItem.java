package zna.online.compass.PlacesTab.PlacesSelectedItem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

import zna.online.compass.PlacesTab.PlacesModel;
import zna.online.compass.R;

public class PlacesSelectedItem extends AppCompatActivity {

    private PlacesModel placesModel;
    //private EventsModel eventsModel;

    private ImageView imageView;
    private RecyclerView recyclerView;
    private PlacesPhotosAdapter placesPhotosAdapter;
    private List<StorageReference> photosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_selected_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        placesModel = getIntent().getParcelableExtra("selectedItem");
        imageView = (ImageView) findViewById(R.id.toolbar_selected_item_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_photos);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        placesPhotosAdapter = new PlacesPhotosAdapter(photosList);
        recyclerView.setAdapter(placesPhotosAdapter);

    }

    public class PlacesPhotosAdapter extends RecyclerView.Adapter<PlacesPhotosAdapter.PlacesPhotosHolder>{

        private List<StorageReference> list;

        public PlacesPhotosAdapter(List<StorageReference> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public PlacesPhotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PlacesPhotosHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_selected_item_photos, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PlacesPhotosHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PlacesPhotosHolder extends RecyclerView.ViewHolder {

            public PlacesPhotosHolder(View itemView) {
                super(itemView);
            }

        }
    }
}
