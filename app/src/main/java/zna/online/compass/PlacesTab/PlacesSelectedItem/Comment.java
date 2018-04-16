package zna.online.compass.PlacesTab.PlacesSelectedItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Comment {

    public String id;
    public String idProfile;
    public String name;
    public String comment;

    private int countLikes;
    private boolean isLiked;
    private PlacesSelectedItem.PlacesCommentsAdapter adapter;
    private String token;

    public Comment() {
    }

    public Comment(String id, String idProfile, String name, String comment) {
        this.id = id;
        this.idProfile = idProfile;
        this.name = name;
        this.comment = comment;
    }

    public void StartInit(final PlacesSelectedItem.PlacesCommentsAdapter adapter, String placesOrEvents, String idPlaceOrEvent){
        token = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setAdapter(adapter);
        isLiked = false;
        countLikes = 0;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(placesOrEvents).child(idPlaceOrEvent).child("comments").child(this.id).child("likes");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                countLikes++;
                if (dataSnapshot.getKey().equals(token)){
                    isLiked = true;
                }
                adapter.notifyDataSetChanged();
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
        adapter.notifyDataSetChanged();
    }

    public int getCountLikes() {
        return countLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void NotifyAdapter()
    {
        adapter.notifyDataSetChanged();
    }

    public void setAdapter(PlacesSelectedItem.PlacesCommentsAdapter adapter) {
        this.adapter = adapter;
    }
}
