package zna.online.compass.PlacesTab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import zna.online.compass.GlideApp;
import zna.online.compass.PlacesTab.PlacesSelectedItem.PlacesSelectedItem;
import zna.online.compass.R;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesHolder>{

    private List<PlacesModel> list;
    private PlacesHolder placeHolder;

    public PlacesAdapter(List<PlacesModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PlacesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlacesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_places_and_events, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlacesHolder holder, int position) {
        PlacesModel placesModel = list.get(position);

        holder.nameTextView.setText(placesModel.getName());
        holder.averageCheckTextView.setText(placesModel.getAverageCheck());
        holder.distanceTextView.setText(placesModel.getDistance());
        holder.notesTextView.setText(placesModel.getNotes());
        holder.typeTextView.setText(placesModel.type);
        holder.workingHoursTextView.setText(placesModel.workingHours);
        holder.rateButton.setText(placesModel.rate + "");
        //holder.rateButton.setBackgroundColor(holder.itemView.getResources().getColor(placesModel.getColorRate()));

        //holder.rateButton.setBackgroundColor(placesModel.getColorRate());

        holder.rateButton.setBackgroundResource(placesModel.getColorRate());


        placeHolder = holder;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference().child("PlacesPhotos").child(placesModel.id).child("MainPhoto").child("1.jpg");
        if (storageRef != null){
            GlideApp.with(holder.itemView.getContext())
                    .load(storageRef)
                    .into(holder.mainPhotoImageView);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (!isLongClick){
                    PlacesModel item = list.get(position);
                    Intent intent = new Intent(v.getContext(), PlacesSelectedItem.class);
                    intent.putExtra("selectedItem", item);
                    v.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return -1;
    }

    class PlacesHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView nameTextView, typeTextView, workingHoursTextView, distanceTextView, averageCheckTextView, notesTextView;
        Button rateButton;
        ImageView mainPhotoImageView;
        RecyclerView recyclerView;

        private ItemClickListener itemClickListener;

        public PlacesHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
    }
}
