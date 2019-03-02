package co.microparcel.microparcel.Ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.util.ArrayList;
import java.util.Objects;

import co.microparcel.microparcel.R;

import static android.support.constraint.Constraints.TAG;
import static co.microparcel.microparcel.Constants.MAPVIEW_BUNDLE_KEY;

public class OrderInfoFragment extends Fragment implements OnMapReadyCallback, DirectionCallback {


    private static final long SPLASH_TIME_OUT_REDIRECT = 3000;
    View view;
    private MapView mapView;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyBIURL7e19LiNRR05_sULtEXfBB4TP0v_g";
    private GeoApiContext mGeoApiContext = null;
    private String order_no;
    private LatLng pickoff_latlng, dropoff_latlng;
    private String pickoff, dropoff;
    private String dd_vehicle_no, dd_driver_name, dd_driver_rating, dd_vehicle_type, dd_vehicle, dd_driver_mobile;
    private String pickoff_address, dropoff_address;
    private TextView driver_rating_TextView, driver_name_TextView, vehicle_info_TextView, vehicle_no_info_TextView;
    private Button call_driver_Button, cancel_trip_Button;


    public OrderInfoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order_info, container, false);

        driver_rating_TextView = view.findViewById(R.id.driver_rating_TextView);
        driver_name_TextView = view.findViewById(R.id.driver_name_TextView);
        vehicle_info_TextView = view.findViewById(R.id.vehicle_info_TextView);
        vehicle_no_info_TextView = view.findViewById(R.id.vehicle_no_info_TextView);
        call_driver_Button = view.findViewById(R.id.call_driver_Button);
        cancel_trip_Button = view.findViewById(R.id.cancel_trip_Button);

        cancel_trip_Button.setEnabled(false);
        call_driver_Button.setEnabled(false);

        assert getArguments() != null;
        order_no = getArguments().getString("order_no");
        TextView order_no_info_TextView = view.findViewById(R.id.order_no_info_TextView);
        order_no_info_TextView.setText(order_no);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();

        DatabaseReference orderStarusRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
        orderStarusRef
                .child("ORDERS")
                .child("ONGOING_ORDERS")
                .child(username)
                .child(order_no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String order_status = (String) dataSnapshot.child("od_order_status").getValue();
                if (order_status.equals("0")){
                    cancel_trip_Button.setEnabled(false);
                    call_driver_Button.setEnabled(false);
                }
                if (order_status.equals("1")){
                    cancel_trip_Button.setEnabled(false);
                    call_driver_Button.setEnabled(false);
                }
                if (order_status.equals("2")){
                    cancel_trip_Button.setEnabled(false);
                    call_driver_Button.setEnabled(true);
                }
                if (order_status.equals("3")){
                    cancel_trip_Button.setEnabled(true);
                    call_driver_Button.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference order_dataRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
        order_dataRef.child("ACTIVE_DATA").child(username).child(order_no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pickoff = (String) dataSnapshot.child("ad_pickoff_latlng").getValue();
                dropoff = (String) dataSnapshot.child("ad_dropoff_latlng").getValue();
                String[] pickoff_latlngList = pickoff.split(",");
                String[] dropoff_latlngList = dropoff.split(",");
                double pickoff_lat = Double.parseDouble(pickoff_latlngList[0]);
                double pickoff_lng = Double.parseDouble(pickoff_latlngList[1]);
                double dropoff_lat = Double.parseDouble(dropoff_latlngList[0]);
                double dropoff_lng = Double.parseDouble(dropoff_latlngList[1]);
                pickoff_latlng = new LatLng(pickoff_lat, pickoff_lng);
                dropoff_latlng = new LatLng(dropoff_lat, dropoff_lng);
                pickoff_address = (String) dataSnapshot.child("ad_pickoff_address").getValue();
                dropoff_address = (String) dataSnapshot.child("ad_dropoff_address").getValue();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestDirection();
                    }
                }, SPLASH_TIME_OUT_REDIRECT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference driver_dataRef = FirebaseDatabase.getInstance().getReference("DRIVER_DATA");
        driver_dataRef.child("ASSIGNED_DRIVERS").child(order_no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dd_vehicle_no = (String) dataSnapshot.child("dd_vehicle_no").getValue();
                dd_driver_name = (String) dataSnapshot.child("dd_driver_name").getValue();
                dd_driver_rating = (String) dataSnapshot.child("dd_driver_rating").getValue();
                dd_vehicle_type = (String) dataSnapshot.child("dd_vehicle_type").getValue();
                dd_vehicle = (String) dataSnapshot.child("dd_vehicle").getValue();
                dd_driver_mobile = (String) dataSnapshot.child("dd_driver_mobile").getValue();

                vehicle_no_info_TextView.setText(dd_vehicle_no);
                driver_name_TextView.setText(dd_driver_name);
                driver_rating_TextView.setText(dd_driver_rating);
                vehicle_info_TextView.setText(dd_vehicle);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        call_driver_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+dd_driver_mobile));
                startActivity(callIntent);
            }
        });

        cancel_trip_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCancelTrip();
            }
        });


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

        return view;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setBuildingsEnabled(true);

    }

    public void requestDirection() {
        Snackbar.make(view, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(pickoff_latlng)
                .to(dropoff_latlng)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(OrderInfoFragment.this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(view, "SERVER STATUS : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            googleMap.addMarker(new MarkerOptions()
                    .position(pickoff_latlng)
                    .title(pickoff_address)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.addMarker(new MarkerOptions()
                    .position(dropoff_latlng)
                    .title(dropoff_address)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);

        } else {
            Snackbar.make(view, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngBounds(bounds, 200));
    }

    private void callCancelTrip(){

        AlertDialog.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setMessage("Are you sure, you really want to cancel this trip ?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String username = firebaseUser.getUid();
                DatabaseReference cancelOrderRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
                cancelOrderRef
                        .child("ORDERS")
                        .child("ONGOING_ORDERS")
                        .child(username)
                        .child(order_no)
                        .child("od_order_status").setValue("0");

                BottomNavigationView bottom_nav_Bar;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    bottom_nav_Bar = (BottomNavigationView) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_Bar);
                    bottom_nav_Bar.setSelectedItemId(R.id.orders_item);
                }

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }


}
