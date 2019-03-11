package co.microparcel.microparcel.Ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import co.microparcel.microparcel.R;
import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private android.app.AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new SpotsDialog.Builder().setContext(MainActivity.this)
                .setTheme(R.style.loading)
                .setMessage("Fetching Details")
                .setCancelable(false)
                .build();
        loadingDialog.show();

        loadFragment(new CreateOrderFragment());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();

        DatabaseReference orderStarusRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
        orderStarusRef
                .child("ORDERS")
                .child("ONGOING_ORDERS")
                .child(username)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot pendingOrderSnap : dataSnapshot.getChildren()) {
                        String order_status = (String) pendingOrderSnap.child("od_order_status").getValue();
                        assert order_status != null;
                        if (order_status.equals("2") || (order_status.equals("3"))) {
                            BottomNavigationView bottom_nav_Bar;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                bottom_nav_Bar = MainActivity.this.findViewById(R.id.bottom_nav_Bar);
                                bottom_nav_Bar.setSelectedItemId(R.id.orders_item);
                                loadingDialog.dismiss();
                            }
                            loadingDialog.dismiss();
                        }
                    }
                    loadingDialog.dismiss();
                }else {
                    BottomNavigationView bottom_nav_Bar;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bottom_nav_Bar = MainActivity.this.findViewById(R.id.bottom_nav_Bar);
                        bottom_nav_Bar.setSelectedItemId(R.id.book_item);
                        loadingDialog.dismiss();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismiss();
            }
        });


        BottomNavigationView bottom_nav_Bar = findViewById(R.id.bottom_nav_Bar);
        bottom_nav_Bar.setOnNavigationItemSelectedListener(MainActivity.this);


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to close Micro Parcel", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.book_item :
                fragment = new CreateOrderFragment();
                break;


            case R.id.orders_item :
                fragment = new YourOrdersFragment();
                break;


            case R.id.account_item :
                fragment = new MoreFragment();
                break;


        }

        return loadFragment(fragment);
  }

    private boolean loadFragment(Fragment fragment) {



        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmant_holder_FrameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    }



