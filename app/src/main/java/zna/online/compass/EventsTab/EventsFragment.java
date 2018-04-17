package zna.online.compass.EventsTab;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.GlideApp;
import zna.online.compass.R;
import zna.online.compass.Test.TestResultActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<EventsModel> eventsModelListResult;
    private EventsAdapter adapter;
    private EventsModelList eventsModelList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeFragment();
    }

    private void InitializeFragment()
    {
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout_events);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateList();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        eventsModelListResult = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    LoadMoreItemsForList();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        adapter = new EventsAdapter(eventsModelListResult);
        recyclerView.setAdapter(adapter);

        eventsModelList = new EventsModelList(this.getActivity(), this);

        UpdateList();
    }

    public void UpdateList()
    {
        if (eventsModelList.getList(eventsModelListResult)){
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void LoadMoreItemsForList()
    {
        swipeRefreshLayout.setRefreshing(true);
        if (eventsModelList.getMoreList(eventsModelListResult.size() - 1, eventsModelListResult)){
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }else{
            Toast.makeText(this.getActivity(), "Молодец! Ты долистал до конца)", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void GoTop()
    {
        recyclerView.scrollToPosition(0);
    }

    public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder>{

        private List<EventsModel> list;
        private TestResultActivity.PlaceAndEventAdapter.PlaceAndEventHolder placeAndEventHolder;

        public EventsAdapter(List<EventsModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public EventsFragment.EventsAdapter.EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EventsFragment.EventsAdapter.EventsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_places_and_events, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EventsFragment.EventsAdapter.EventsHolder holder, int position) {
            EventsModel eventsModel = list.get(position);
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
            StorageReference storageRef = storage.getReference().child("EventsPhotos").child(eventsModel.id).child("MainPhoto").child("1.jpg");
            if (storageRef != null){
                GlideApp.with(holder.itemView.getContext())
                        .load(storageRef)
                        .into(holder.mainPhotoImageView);
            }
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            return -1;
        }

        public class EventsHolder extends RecyclerView.ViewHolder{

            TextView nameTextView, typeTextView, workingHoursTextView, distanceTextView, averageCheckTextView, notesTextView;
            Button rateButton;
            ImageView mainPhotoImageView;
            RecyclerView recyclerView;

            public EventsHolder(View itemView) {
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
