package co.microparcel.microparcel;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
public class CreateOrderFragment extends Fragment {
    View view;
    private Button proceed_Button;
    private LinearLayout v_xs_LinearLayout, v_xm_LinearLayout, v_xl_LinearLayout, v_x2l_LinearLayout;
    private TextView v_name_TextView, v_cap_TextView, v_dim_TextView, v_desc_TextView, v_ob_TextView, v_cb_TextView;
    private TextView loc_pickoff_TextView, loc_dropoff_TextView;
    private ImageView v_ImageView;
    private Animation aniSlide;
    private ProgressBar loc_progressBar;
    private String vehicle_type_switch;
    private Integer location_switch, vehicle_switch;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    private String pickup, drop;
    public CreateOrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_create_order, container, false);

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

                if(dropoff_add.equals("Select Pickoff Address") || dropoff_add.equals("")){
                    Toast.makeText(getContext(), "Select dropoff location", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProceedFragment pfData = new ProceedFragment ();
                Bundle args = new Bundle();
                args.putString("pickoff", pickup);
                args.putString("dropoff", drop);
                args.putInt("vehicle", vehicle_switch);
                args.putString("vehicle_type", vehicle_type_switch);
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
        v_name_TextView.setText("XS");
        v_cap_TextView.setText("Upto 30kgs");
        v_dim_TextView.setText("L = 15, W = 15, H = 15");
        v_desc_TextView.setText("Best for urgent deliveries of documents, parcels like books, gifts, cakes, small electronics and other stuffs upto 30 kg of weight.");
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
        v_name_TextView.setText("XM");
        v_cap_TextView.setText("Upto 750kgs");
        v_dim_TextView.setText("L = 15, W = 15, H = 15");
        v_desc_TextView.setText("Best for urgent deliveries of documents, parcels like books, gifts, cakes, small electronics and other stuffs upto 30 kg of weight.");
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
        v_name_TextView.setText("XL");
        v_cap_TextView.setText("Upto 1200kgs");
        v_dim_TextView.setText("L = 15, W = 15, H = 15");
        v_desc_TextView.setText("Best for urgent deliveries of documents, parcels like books, gifts, cakes, small electronics and other stuffs upto 30 kg of weight.");
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
        v_name_TextView.setText("X2L");
        v_cap_TextView.setText("Upto 2000kgs");
        v_dim_TextView.setText("L = 15, W = 15, H = 15");
        v_desc_TextView.setText("Best for urgent deliveries of documents, parcels like books, gifts, cakes, small electronics and other stuffs upto 30 kg of weight.");
    }
    private void ob_veh(){
        vehicle_type_switch = "1";
        v_ob_TextView.setBackground(getResources().getDrawable(R.drawable.btn_left));
        v_cb_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_right));
    }
    private void cb_veh(){
        vehicle_type_switch = "2";
        v_ob_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_left));
        v_cb_TextView.setBackground(getResources().getDrawable(R.drawable.btn_right));
    }
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working
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
        }
        if (location_switch == 2) {
            loc_dropoff_TextView.setText(name);
            drop = address;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

}
