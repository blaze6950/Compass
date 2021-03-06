package zna.online.compass.MainActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import zna.online.compass.EventsTab.EventsFragment;
import zna.online.compass.LeadersTab.LeadersFragment;
import zna.online.compass.PlacesTab.PlacesFragment;
import zna.online.compass.ProfileTab.ProfileFragment;
import zna.online.compass.R;
import zna.online.compass.Test.TestActivity;

public class MainMenuActivity extends AppCompatActivity  implements View.OnClickListener {

    FragmentTransaction fTrans;
    private FloatingActionButton testButton;
    private int developerPathCounter = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_places:
                    loadPlaces();
                    return true;
                case R.id.navigation_events:
                    loadEvents();
                    return true;
                case R.id.navigation_leaders:
                    loadLeaders();
                    return true;
                case R.id.navigation_profile_view:
                    loadProfile();
                    return true;
            }
            return false;
        }

    };

    private void loadProfile() {
        setTitle(getString(R.string.title_profile));
        ProfileFragment fragment = ProfileFragment.newInstance("first test", "profile");
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.content_constraint_layout, fragment);
        fTrans.commit();
    }

    private void loadLeaders() {
        setTitle(getString(R.string.title_leaders));
        LeadersFragment fragment = LeadersFragment.newInstance("first test", "leaders");
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.content_constraint_layout, fragment);
        fTrans.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter){
            //Open dialog for choose filters
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadEvents() {
        setTitle(getString(R.string.title_events));
        EventsFragment fragment = EventsFragment.newInstance("first test", "events");
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.content_constraint_layout, fragment);
        fTrans.commit();
    }

    private void loadPlaces() {
        setTitle(getString(R.string.title_places));
        PlacesFragment fragment = PlacesFragment.newInstance("first test", "places");
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.content_constraint_layout, fragment);
        fTrans.commit();
    }

    @Override
    public void onBackPressed() {
        if (developerPathCounter == 5)
        {
            Toast.makeText(this.getApplicationContext(), R.string.appFunctionsExtended, Toast.LENGTH_SHORT).show();
        }else if (developerPathCounter == 8) {
            Toast.makeText(this.getApplicationContext(), R.string.appFunctionExtendedSuccess, Toast.LENGTH_SHORT).show();
            developerPathCounter = 0;
            //startActivity
        }
        developerPathCounter++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        testButton = (FloatingActionButton) findViewById(R.id.main_menu_test);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {

            loadPlaces();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == testButton){
            startActivity(new Intent(this, TestActivity.class));
        }
    }
}
