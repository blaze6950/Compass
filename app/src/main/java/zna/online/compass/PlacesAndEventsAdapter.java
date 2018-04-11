package zna.online.compass;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlacesAndEventsAdapter extends RecyclerView.Adapter<PlacesAndEventsAdapter.PlacesAndEventsHolder>{

    private List<PlacesAndEventsModel> list;

    public PlacesAndEventsAdapter(List<PlacesAndEventsModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PlacesAndEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlacesAndEventsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_places_and_events, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAndEventsHolder holder, int position) {
        PlacesAndEventsModel placesAndEventsModel = list.get(position);

        // надо исправить до рабочего состояния!!!!!!
        holder.nameTextView.setText(placesAndEventsModel.name);
        holder.averageCheckTextView.setText(placesAndEventsModel.averageCheck);
        holder.distanceTextView.setText(placesAndEventsModel.Coordinates);
        holder.rateButton.setText(placesAndEventsModel.id);
        holder.notesTextView.setText(placesAndEventsModel.notes);
        holder.typeTextView.setText(placesAndEventsModel.type);
        holder.workingHoursTextView.setText(placesAndEventsModel.workingHours);
        //holder.mainPhotoImageView.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlacesAndEventsHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, typeTextView, workingHoursTextView, distanceTextView, averageCheckTextView, notesTextView;
        Button rateButton;
        ImageView mainPhotoImageView;

        public PlacesAndEventsHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.textView_name);
            typeTextView = (TextView) itemView.findViewById(R.id.textView_type);
            workingHoursTextView = (TextView) itemView.findViewById(R.id.textView_working_hours);
            distanceTextView = (TextView) itemView.findViewById(R.id.textView_distance);
            averageCheckTextView = (TextView) itemView.findViewById(R.id.textView_average_check);
            notesTextView = (TextView) itemView.findViewById(R.id.textView_notes);

            rateButton = (Button) itemView.findViewById(R.id.button_rate);

            mainPhotoImageView = (ImageView) itemView.findViewById(R.id.imageView_main_photo);

        }
    }
}
