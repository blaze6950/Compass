package zna.online.compass.PlacesTab.PlacesSelectedItem;

import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.GlideApp;
import zna.online.compass.PlacesTab.PlacesModel;
import zna.online.compass.R;

public class PlacesSelectedItem extends AppCompatActivity {

    private PlacesModel placesModel;
    //private EventsModel eventsModel;

    private ImageView mainPhoto;
    private TextView name;
    private TextView type;
    private TextView itemInfo;
    private Button subscribeBtn;
    private TextView notes;
    private EditText comment;
    private ImageButton commentSendBtn;


    private RecyclerView recyclerViewPhotos;
    private PlacesPhotosAdapter placesPhotosAdapter;
    private List<StorageReference> photosList;

    private RecyclerView recyclerViewComments;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference mainPhotoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_selected_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        placesModel = getIntent().getParcelableExtra("selectedItem");

        ConfigRecyclerViewPhotos();

        InitialVariables();

        LoadMainPhoto();

        LoadData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void LoadData() {
        name.setText(placesModel.name);
        type.setText(placesModel.type);
        itemInfo.setText(placesModel.address + " • " + placesModel.workingHours);
        notes.setText(placesModel.notes);
        LoadAdditionalPhotos();
    }

    private void InitialVariables() {
        mainPhoto = (ImageView) findViewById(R.id.toolbar_selected_item_image);
        name = (TextView) findViewById(R.id.name_item_selected_activity);
        type = (TextView) findViewById(R.id.type_item_selected_activity);
        itemInfo = (TextView) findViewById(R.id.iteminfo_item_selected_activity);
        subscribeBtn = (Button) findViewById(R.id.subscribebtn_item_selected_activity);
        notes = (TextView) findViewById(R.id.notes_item_selected_activity);
        comment = (EditText) findViewById(R.id.comment_item_selected_activity);
        commentSendBtn = (ImageButton) findViewById(R.id.send_comment_item_selected_activity);
        storage = FirebaseStorage.getInstance();
    }

    private void LoadMainPhoto() {
        // Create a storage reference from our app
        mainPhotoRef = storage.getReference().child("PlacesPhotos").child(placesModel.id).child("MainPhoto").child("1.jpg");
        mainPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (mainPhotoRef != null){
                    GlideApp.with(getApplicationContext())
                            .load(mainPhotoRef)
                            .into(mainPhoto);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.error_loading_photo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadAdditionalPhotos() {
        storageRef = storage.getReference().child("PlacesPhotos").child(placesModel.id);
        int i = 1;
        LoadAdditionalPhoto(i);
    }

    private void LoadAdditionalPhoto(final int i) {
        storageRef = storageRef.child(i + ".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (storageRef != null) {
                    photosList.add(storageRef);
                }
                storageRef = storageRef.getParent();
                placesPhotosAdapter.notifyDataSetChanged();
                LoadAdditionalPhoto(i + 1);
            }
        });
    }

    private void ConfigRecyclerViewPhotos() {
        recyclerViewPhotos = findViewById(R.id.recycler_view_photos);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPhotos.setLayoutManager(horizontalLayoutManager);

        photosList = new ArrayList<>();
        placesPhotosAdapter = new PlacesPhotosAdapter(photosList);
        recyclerViewPhotos.setAdapter(placesPhotosAdapter);
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
            GlideApp.with(getApplicationContext())
                    .load(list.get(position))
                    .into(holder.additionalPhoto);
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            return -1;
        }

        class PlacesPhotosHolder extends RecyclerView.ViewHolder {

            ImageView additionalPhoto;

            public PlacesPhotosHolder(View itemView) {
                super(itemView);

                additionalPhoto = (ImageView) itemView.findViewById(R.id.imageView_photo);
            }

        }
    }

    public class PlacesCommentsAdapter extends RecyclerView.Adapter<PlacesCommentsAdapter.PlacesCommentsHolder> {

        private List<Comment> list;

        public PlacesCommentsAdapter(List<Comment> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public PlacesCommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PlacesCommentsAdapter.PlacesCommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_selected_item_comments, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PlacesCommentsHolder holder, int position) {
            Comment comment = list.get(position);

            holder.name.setText(comment.name);
            holder.comment.setText(comment.comment);
//            holder.likeCounter.setText("");
//            holder.like.setImageBitmap(R.drawable.ic_unlike);

            StorageReference profilePhotoRef = storage.getReference().child("ProfilesPhotos").child(comment.idProfile).child("1.jpg");
            GlideApp.with(getApplicationContext())
                    .load(profilePhotoRef)
                    .into(holder.profilePhoto);
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            return -1;
        }

        class PlacesCommentsHolder extends RecyclerView.ViewHolder {

            ImageView profilePhoto, like;
            TextView name, comment, likeCounter;

            public PlacesCommentsHolder(View itemView) {
                super(itemView);

                profilePhoto = (ImageView) itemView.findViewById(R.id.comment_image_profile);
                like = (ImageView) itemView.findViewById(R.id.comment_like);
                name = (TextView) itemView.findViewById(R.id.comment_name);
                comment = (TextView) itemView.findViewById(R.id.comment_content);
                likeCounter = (TextView) itemView.findViewById(R.id.comment_like_counter);
            }
        }
    }
}
