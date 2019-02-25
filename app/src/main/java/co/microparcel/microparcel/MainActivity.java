package co.microparcel.microparcel;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new CreateOrderFragment());
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
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

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
            case R.id.vehicle_order_item :
                fragment = new CreateOrderFragment();
                break;

            case R.id.courier_order_item :
                fragment = new ConnectFragment();
                break;

            case R.id.your_orders_item :
                fragment = new YourOrdersFragment();
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



