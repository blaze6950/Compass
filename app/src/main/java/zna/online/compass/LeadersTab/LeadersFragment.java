package zna.online.compass.LeadersTab;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import zna.online.compass.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeadersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeadersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeadersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /////////////////////////////////////////////////////////////

    private RecyclerView recyclerView;
    private LeadersAdapter leadersAdapter;
    private List<Profile> profilesList;

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;

    public LeadersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeadersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeadersFragment newInstance(String param1, String param2) {
        LeadersFragment fragment = new LeadersFragment();
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
        return inflater.inflate(R.layout.fragment_leaders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeFragment();
    }

    private void InitializeFragment() {
        profilesList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Profiles");
        storage = FirebaseStorage.getInstance();
        GetProfileList();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_leaders);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        leadersAdapter = new LeadersAdapter(profilesList);
        recyclerView.setAdapter(leadersAdapter);
    }

    private void GetProfileList() {
        databaseReference.orderByChild("points");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                profilesList.add(dataSnapshot.getValue(Profile.class));
                leadersAdapter.notifyDataSetChanged();
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

    public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.LeadersHolder> {

        private List<Profile> list;

        public LeadersAdapter(List<Profile> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public LeadersAdapter.LeadersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LeadersAdapter.LeadersHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_leaders_profile, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull LeadersAdapter.LeadersHolder holder, int position) {
            Profile profile = list.get(position);

            StorageReference profilePhotoRef = storage.getReference().child("ProfilesPhotos").child(profile.id).child("1.jpg");
            Glide.with(holder.itemView.getContext())
                    .load(profilePhotoRef)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profilePhoto);

            holder.name.setText(profile.name + " " + profile.lastName);
            holder.position.setText(String.valueOf(position + 1));
        }

        @Override
        public int getItemCount() {
            if (list != null)
                return list.size();
            return -1;
        }

        class LeadersHolder extends RecyclerView.ViewHolder {

            ImageView profilePhoto;
            TextView name, position;
            View itemView;

            public LeadersHolder(View itemView) {
                super(itemView);

                profilePhoto = (ImageView) itemView.findViewById(R.id.leaders_profile_photo);
                name = (TextView) itemView.findViewById(R.id.leaders_profile_name);
                position = (TextView) itemView.findViewById(R.id.leaders_profile_position);
                this.itemView = itemView;
            }
        }
    }
}
