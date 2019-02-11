package co.microparcel.microparcel;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new CreateOrderFragment());

        BottomNavigationView bottom_nav_Bar = findViewById(R.id.bottom_nav_Bar);
        bottom_nav_Bar.setOnNavigationItemSelectedListener(MainActivity.this);




    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.create_order_menu_item :
                fragment = new CreateOrderFragment();
                break;

            case R.id.your_orders_menu_item :
                fragment = new YourOrdersFragment();
                break;

            case R.id.more_menu_item :
                fragment = new SettingsFragment();
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



