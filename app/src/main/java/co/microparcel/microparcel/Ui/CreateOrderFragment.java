package co.microparcel.microparcel.Ui;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import co.microparcel.microparcel.R;

import static android.app.Activity.RESULT_OK;
public class CreateOrderFragment extends Fragment {
    View view;

    private Button proceed_Button;
    private LinearLayout v_xs_LinearLayout, v_xm_LinearLayout, v_xl_LinearLayout, v_x2l_LinearLayout;
    private TextView v_name_TextView, v_cap_TextView, v_dim_TextView, v_desc_TextView, v_ob_TextView, v_cb_TextView, vehicles_category_TextView;
    private TextView loc_pickoff_TextView, loc_dropoff_TextView;
    private ImageView v_ImageView;
    private Animation aniSlide;
    private ProgressBar loc_progressBar;
    private String vehicle_type_switch;
    private Integer location_switch, vehicle_switch;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    private String pickup, drop;
    private double pickoff_lat, pickoff_lng, dropoff_lat, dropoff_lng;
    LatLng pickoff_latlng, dropoff_latlng;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    public CreateOrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_create_order, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permission already granted.
        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACT);
        } else {
            // Permission already granted.
        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Permission already granted.
        }

        aniSlide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        loc_progressBar = view.findViewById(R.id.loc_progressBar);
        loc_progressBar.setVisibility(View.INVISIBLE);



        v_xs_LinearLayout = view.findViewById(R.id.v_xs_LinearLayout);
        v_xm_LinearLayout = view.findViewById(R.id.v_xm_LinearLayout);
        v_xl_LinearLayout = view.findViewById(R.id.v_xl_LinearLayout);
        v_x2l_LinearLayout = view.findViewById(R.id.v_x2l_LinearLayout);
        v_ImageView = view.findViewById(R.id.v_ImageView);
        v_name_TextView = view.findViewById(R.id.v_name_TextView);
        v_cap_TextView = view.findViewById(R.id.v_cap_TextView);
        v_dim_TextView = view.findViewById(R.id.v_dim_TextView);
        v_desc_TextView = view.findViewById(R.id.v_desc_TextView);
        v_ob_TextView = view.findViewById(R.id.v_ob_TextView);
        v_cb_TextView = view.findViewById(R.id.v_cb_TextView);

        vehicles_category_TextView = view.findViewById(R.id.vehicles_category_TextView);

        v_cb_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_ob_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));

        loc_pickoff_TextView = view.findViewById(R.id.loc_pickoff_TextView);
        loc_pickoff_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc_progressBar.setVisibility(View.VISIBLE);
                location_switch = 1;
                startPlacePickerActivity();

            }
        });

        loc_dropoff_TextView = view.findViewById(R.id.loc_dropoff_TextView);
        loc_dropoff_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc_progressBar.setVisibility(View.VISIBLE);
                location_switch = 2;
                startPlacePickerActivity();
            }
        });

        v_xs();

        v_xs_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_xs();
            }
        });

        v_xm_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_xm();
            }
        });

        v_xl_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_xl();
            }
        });

        v_x2l_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_x2l();
            }
        });

        v_ob_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ob_veh();
            }
        });

        v_cb_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_veh();
            }
        });

        vehicle_type_switch = "0";



        proceed_Button = view.findViewById(R.id.proceed_Button);

        proceed_Button = view.findViewById(R.id.proceed_Button);
        proceed_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pickoff_add = loc_pickoff_TextView.getText().toString();
                String dropoff_add = loc_dropoff_TextView.getText().toString();

                if(pickoff_add.equals("Select Pickoff Address") || pickoff_add.equals("")){
                    Toast.makeText(getContext(), "Select pickoff location.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dropoff_add.equals("Select Dropoff Address") || dropoff_add.equals("")){
                    Toast.makeText(getContext(), "Select dropoff location.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (vehicle_type_switch.equals("0") && !(vehicle_switch == 1)){
                    callNoVehicleType();
                    return;
                }


                ProceedFragment pfData = new ProceedFragment ();
                Bundle args = new Bundle();
                args.putString("pickoff", pickup);
                args.putString("dropoff", drop);
                args.putInt("vehicle", vehicle_switch);
                args.putString("vehicle_type", vehicle_type_switch);
                args.putDouble("pickoff_lat", pickoff_lat);
                args.putDouble("pickoff_lng", pickoff_lng);
                args.putDouble("dropoff_lat", dropoff_lat);
                args.putDouble("dropoff_lng", dropoff_lng);
                pfData.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmant_holder_FrameLayout, pfData);
                fragmentTransaction.hide(CreateOrderFragment.this);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;

    }
    private void v_xs(){
        vehicle_switch = 1;
        v_ob_TextView.setVisibility(View.INVISIBLE);
        v_cb_TextView.setVisibility(View.INVISIBLE);
        v_xs_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        v_xm_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xl_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_x2l_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_ImageView.setImageResource(R.drawable.ic_motorcycle);
        v_name_TextView.setText(getString(R.string.xs_veh));
        vehicles_category_TextView.setText(getString(R.string.del_bike));
        v_cap_TextView.setText(getString(R.string._15));
        v_dim_TextView.setText(getString(R.string._l40));
        v_desc_TextView.setText(getString(R.string.bike_desc));
    }
    private void v_xm(){
        vehicle_switch = 2;
        v_ob_TextView.setVisibility(View.VISIBLE);
        v_cb_TextView.setVisibility(View.VISIBLE);
        v_xs_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xm_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        v_xl_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_x2l_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_ImageView.setImageResource(R.drawable.ic_truck);
        v_name_TextView.setText(getString(R.string.xm_veh));
        vehicles_category_TextView.setText(getString(R.string.ape));
        v_cap_TextView.setText(getString(R.string.up_500));
        v_dim_TextView.setText(getString(R.string.l6));
        v_desc_TextView.setText("Best for urgent deliveries of small furnitures, manufactured goods and other commercial & non-commercial stuffs upto 500 Kgs of weight.");
    }
    private void v_xl(){
        vehicle_switch = 3;
        v_ob_TextView.setVisibility(View.VISIBLE);
        v_cb_TextView.setVisibility(View.VISIBLE);
        v_xs_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xm_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xl_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        v_x2l_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_ImageView.setImageResource(R.drawable.ic_truck);
        v_name_TextView.setText(getString(R.string.xl_veh));
        vehicles_category_TextView.setText(getString(R.string.ace));
        v_cap_TextView.setText(getString(R.string.up750));
        v_dim_TextView.setText(getString(R.string.l7));
        v_desc_TextView.setText("Best for urgent deliveries of 1 BHK House Shifting, furnitures, manufactured goods and other commercial & non-commercial stuffs upto 750 Kgs of weight.");
    }
    private void v_x2l(){
        vehicle_switch = 4;
        v_ob_TextView.setVisibility(View.VISIBLE);
        v_cb_TextView.setVisibility(View.VISIBLE);
        v_xs_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xm_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_xl_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_x2l_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        v_ImageView.setImageResource(R.drawable.ic_truck);
        v_name_TextView.setText(getString(R.string.x2l_veh));
        vehicles_category_TextView.setText(getString(R.string.dost));
        v_cap_TextView.setText(getString(R.string.up1500));
        v_dim_TextView.setText(getString(R.string.l8));
        v_desc_TextView.setText("Best for urgent deliveries of 1-2 BHK House Shifting, furnitures, manufactured goods and other commercial & non-commercial stuffs upto 1500 Kgs of weight.");
    }
    private void ob_veh(){
        vehicle_type_switch = "1";
        v_ob_TextView.setBackground(getResources().getDrawable(R.drawable.button_background));
        v_cb_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
    }
    private void cb_veh(){
        vehicle_type_switch = "2";
        v_ob_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        v_cb_TextView.setBackground(getResources().getDrawable(R.drawable.button_background));
    }
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intent = intentBuilder.build(Objects.requireNonNull(getActivity()));
            }
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            placeSelected = PlacePicker.getPlace(data, Objects.requireNonNull(getContext()));
        }

        assert placeSelected != null;
        String name = placeSelected.getName().toString();
        String address = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            address = Objects.requireNonNull(placeSelected.getAddress()).toString();

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            address = Objects.requireNonNull(placeSelected.getAddress()).toString();
        }
        loc_progressBar.setVisibility(View.INVISIBLE);
        if (location_switch == 1) {
            loc_pickoff_TextView.setText(name);
            pickup = address;
            pickoff_lat = placeSelected.getLatLng().latitude;
            pickoff_lng = placeSelected.getLatLng().longitude;
        }
        if (location_switch == 2) {
            loc_dropoff_TextView.setText(name);
            drop = address;
            dropoff_lat = placeSelected.getLatLng().latitude;
            dropoff_lng = placeSelected.getLatLng().longitude;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    private void callNoVehicleType(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("You have not selected preferred vehicle body type, Would you still like to proceed.");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ProceedFragment pfData = new ProceedFragment ();
                Bundle args = new Bundle();
                args.putString("pickoff", pickup);
                args.putString("dropoff", drop);
                args.putInt("vehicle", vehicle_switch);
                args.putString("vehicle_type", vehicle_type_switch);
                args.putDouble("pickoff_lat", pickoff_lat);
                args.putDouble("pickoff_lng", pickoff_lng);
                args.putDouble("dropoff_lat", dropoff_lat);
                args.putDouble("dropoff_lng", dropoff_lng);
                pfData.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmant_holder_FrameLayout, pfData);
                fragmentTransaction.hide(CreateOrderFragment.this);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}
