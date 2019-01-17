package com.example.alexandrucristea.memories;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ExampleDialog.ExampleDialogListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail, titluMesaj, bodyMesaj;
    private Button fapButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Boolean canAdd;

    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FabSpeedDial fabSpeedDial = (FabSpeedDial)findViewById(R.id.fapbutton);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {

                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_name) {
                    ExampleDialog exampleDialog = new ExampleDialog();
                    exampleDialog.show(getSupportFragmentManager(), "example dialog");


                    return true;
                }
                else if (id == R.id.action_logout){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    return true;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        titluMesaj = (TextView) findViewById(R.id.TitluMesaj);
        bodyMesaj = (TextView) findViewById(R.id.BodyMesaj);

        textViewUserEmail.setText("Welcome " + user.getEmail());
        //buttonLogOut = (Button) findViewById(R.id.buttonLogout);

        //buttonLogOut.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Binaca " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                canAdd = false;
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 20, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    canAdd = true;
        }
    }

    @Override
    public void onClick(View view) {
//    if(view == buttonLogOut) {
//        firebaseAuth.signOut();
//        finish();
//        startActivity(new Intent(this, LoginActivity.class));
//    }
    }

    @Override
    public void applyTexts(String title, String description) {
        titluMesaj.setText(title);
        bodyMesaj.setText(description);
    }
}
