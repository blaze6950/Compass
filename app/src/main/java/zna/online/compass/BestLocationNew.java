package zna.online.compass;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class BestLocationNew {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentBestLocation = null;
    private Context context;

    public BestLocationNew(Context context) {
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Location getLastLocation() {
        currentBestLocation = null;
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        if (task.isSuccessful() && task.getResult() != null) {
            currentBestLocation = task.getResult();
        } else {
            Log.w(TAG, "getLastLocation:exception", task.getException());
            Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
        }
        return currentBestLocation;
    }
}
