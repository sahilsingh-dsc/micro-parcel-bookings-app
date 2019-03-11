package co.microparcel.microparcel.Ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import co.microparcel.microparcel.R;
import co.microparcel.microparcel.Util.httpConnect;
import co.microparcel.microparcel.Models.ActiveData;
import co.microparcel.microparcel.Models.LeadData;
import co.microparcel.microparcel.Models.OrderData;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class ProceedFragment extends Fragment {

    View view;

    private DatabaseReference leadRef;

    private String pickoff, dropoff, vehicle, vehicle_type;

    private String DistanceResult, DurationResult;

    private Button est_fare_book_now_Button, track_driver_Button;

    private AlertDialog.Builder builder, del;

    private static int SPLASH_TIME_OUT = 5000;
    private static int SPLASH_TIME_OUT_REDIRECT = 3000;

    private TextView est_fare_TextView, goods_to_ship_TextView, opt_loading_TextView, opt_unloading_TextView, delivery_inst_or_cod_amt_TextView, opt_physical_pod_TextView, opt_insurance_TextView;

    private double km, carrierfare_round, est_fare;

    private EditText goods_weight_EditText;

    private int loading_switch = 0, unloading_switch = 0, pod_switch = 0, insurance_switch = 0, contact_switch = 0;

    private android.app.AlertDialog loadingDialog;

    private TextView pickoff_contact_TextView, dropoff_contact_TextView;

    private static final int REQUEST_CODE = 1;

    private String ad_pickoff_address, ad_dropoff_address, ad_pickoff_contact_name, ad_pickoff_contact_mobile, ad_dropoff_contact_name, ad_dropoff_contact_mobile, ad_goodstoship, ad_fare, ad_km;
    private Integer ad_vehicle, ad_vehicle_type, ad_loading, ad_unloading, ad_pod;
    String od_date_time_of_order, od_service_type, od_fare, od_order_status;
    private String child_no;
    private String mp_order_no;
    private double pickoff_lat, pickoff_lng, dropoff_lat, dropoff_lng;
    private LatLng pickoff_latlng, dropoff_latlng;
    String pickoff_location, dropoff_location;
    public ProceedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_proceed, container, false);
        Reset();

        pickoff = getArguments().getString("pickoff");
        dropoff = getArguments().getString("dropoff");
        pickoff_lat = getArguments().getDouble("pickoff_lat");
        pickoff_lng = getArguments().getDouble("pickoff_lng");
        dropoff_lat = getArguments().getDouble("dropoff_lat");
        dropoff_lng = getArguments().getDouble("dropoff_lng");
        pickoff_latlng = new LatLng(pickoff_lat, pickoff_lng);
        dropoff_latlng = new LatLng(dropoff_lat, dropoff_lng);
        vehicle = String.valueOf(getArguments().getInt("vehicle"));
        vehicle_type = getArguments().getString("vehicle_type");


        ad_pickoff_address = pickoff;
        ad_dropoff_address = dropoff;
        ad_vehicle = Integer.valueOf(vehicle);
        ad_vehicle_type = Integer.valueOf(vehicle_type);



        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("ODR");
        dbRef.child("odr-state").setValue("-1");

        callFareLoading();

        SearchDistanceCommand(view);



        est_fare_TextView = view.findViewById(R.id.est_fare_TextView);
        est_fare_book_now_Button = view.findViewById(R.id.est_fare_book_now_Button);
        est_fare_book_now_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String goods_name = goods_to_ship_TextView.getText().toString();
                if (goods_name.equals("Goods to ship")){
                    Toast.makeText(getContext(), "Select goods to ship", Toast.LENGTH_SHORT).show();
                    return;
                }

                String pickoff_contact = pickoff_contact_TextView.getText().toString();
                String dropoff_contact = dropoff_contact_TextView.getText().toString();
                if (pickoff_contact.equals("Select Pickoff Contact")){
                    Toast.makeText(getContext(), "Select Pickoff Contact", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dropoff_contact.equals("Select Dropoff Contact")){
                    Toast.makeText(getContext(), "Select Dropoff Contact", Toast.LENGTH_SHORT).show();
                    return;
                }

                ad_fare = est_fare_TextView.getText().toString().trim();
                ad_km = String.valueOf(km);

                callLoading();
            }
        });


        goods_to_ship_TextView = view.findViewById(R.id.goods_to_ship_TextView);
        goods_to_ship_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGoodsList();
            }
        });


        opt_loading_TextView = view.findViewById(R.id.opt_loading_TextView);
        opt_unloading_TextView = view.findViewById(R.id.opt_unloading_TextView);

        opt_loading_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vehicle.equals("1")) {
                    opt_loading_TextView.setEnabled(false);
                    opt_unloading_TextView.setEnabled(false);
                    Toast.makeText(getContext(), "No loading Service is allowed in Bike.", Toast.LENGTH_LONG).show();
                    opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                    opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                } else {

                    if (loading_switch == 0) {
                        opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.button_background));
                        loading_switch = 1;
                    } else {
                        opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                        loading_switch = 0;

                    }

                    ad_loading = loading_switch;

                    callFare();

                }
            }
        });


        opt_unloading_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vehicle.equals("1")) {
                    opt_loading_TextView.setEnabled(false);
                    opt_unloading_TextView.setEnabled(false);
                    Toast.makeText(getContext(), "No Unloading Service is allowed in Bike.", Toast.LENGTH_LONG).show();
                    opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                    opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                } else {

                    if (unloading_switch == 0) {
                        opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.button_background));
                        unloading_switch = 1;
                    } else {
                        opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                        unloading_switch = 0;
                    }

                    ad_unloading = unloading_switch;

                    callFare();

                }

            }
        });

        opt_physical_pod_TextView = view.findViewById(R.id.opt_physical_pod_TextView);
        opt_physical_pod_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pod_switch == 0){
                    opt_physical_pod_TextView.setBackground(getResources().getDrawable(R.drawable.button_background));
                    pod_switch = 1;
                }else {
                    opt_physical_pod_TextView.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                    pod_switch = 0;
                }
                Toast.makeText(getContext(), "POD : "+pod_switch, Toast.LENGTH_SHORT).show();

                ad_pod = pod_switch;

                callFare();

            }
        });


     /*   opt_insurance_TextView = view.findViewById(R.id.opt_insurance_TextView);
        opt_insurance_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insurance_switch == 0){
                    opt_insurance_TextView.setBackground(getResources().getDrawable(R.drawable.loc_tv_from_btn));
                    insurance_switch = 1;
                }else {
                    opt_insurance_TextView.setBackground(getResources().getDrawable(R.drawable.loc_tv_from));
                    insurance_switch = 0;
                }
                Toast.makeText(getContext(), "Insurance : "+insurance_switch, Toast.LENGTH_SHORT).show();

                callFare();

            }
        });*/

        pickoff_contact_TextView = view.findViewById(R.id.pickoff_contact_TextView);
        pickoff_contact_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_switch = 1;
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        dropoff_contact_TextView = view.findViewById(R.id.dropoff_contact_TextView);
        dropoff_contact_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_switch = 2;
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });








        return view;

        // onCreate ends here...
    }

    public void Reset() {

        DistanceResult = "";
        DurationResult = "";

    }

    public void SearchDistanceCommand(View view) {

        Reset();

        if (pickoff.isEmpty() || dropoff.isEmpty()) {
            Toast MissingTextErrorHandle = Toast.makeText(getContext(), "You need to input data into both fields!", Toast.LENGTH_SHORT);
            MissingTextErrorHandle.show();
        } else {
            new AsyncTaskParseJson().execute();
        }
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        String FormattedStartLocation = pickoff.replaceAll(" ", "+");
        String FormattedGoalLocation = dropoff.replaceAll(" ", "+");

        String yourServiceUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + FormattedStartLocation + "&destinations=" + FormattedGoalLocation
                + "&key=AIzaSyA609lXN9_qcwJiHpo02CTuB0oXGxDDumo";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                httpConnect jParser = new httpConnect();
                String json = jParser.getJSONFromUrl(yourServiceUrl);
                JSONObject object = new JSONObject(json);


                JSONArray array = object.getJSONArray("rows");


                JSONObject route = array.getJSONObject(0);


                JSONArray elements = route.getJSONArray("elements");


                JSONObject element = elements.getJSONObject(0);


                JSONObject durationObject = element.getJSONObject("duration");
                String duration = durationObject.getString("text");
                DurationResult = duration;


                JSONObject distanceObject = element.getJSONObject("distance");
                String distance = distanceObject.getString("text");
                DistanceResult = distance;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if (DistanceResult == null || DurationResult == null) {
                Toast ResultErrorHandle = Toast.makeText(getContext(), "We could not find any results! Sorry!", Toast.LENGTH_SHORT);
                ResultErrorHandle.show();
            }

                      if (DistanceResult.indexOf("km") != -1) {
                DistanceResult = DistanceResult.replaceAll("[^\\d.]", "");
                km = Double.parseDouble(DistanceResult);
                callFare();
                callLead();
                loadingDialog.dismiss();

            } else {
                Toast.makeText(getContext(), "Very Small Distance!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

                          CreateOrderFragment createOrderFragment = new CreateOrderFragment();
                          FragmentManager fragmentManager = getFragmentManager();
                          assert fragmentManager != null;
                          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                          fragmentTransaction.add(R.id.fragmant_holder_FrameLayout, createOrderFragment);
                          fragmentTransaction.hide(ProceedFragment.this);
                          fragmentTransaction.addToBackStack(null);
                          fragmentTransaction.commit();
            }
        }
    }

    private void callGoodsList(){

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View mView = layoutInflater.inflate(R.layout.item_layout_alert, null);
        builder = new AlertDialog.Builder(getContext());
        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        final String[] items = new String[]{
                "Furniture", "Food & Beverages", "House Shifting", "Machines/Equip./SpareParts", "Wood/Timber/Plywood",
                "Courier/Packers and Movers", "Vehicles/Automotive Parts", "Chemicals/Paints/Oils", "Tiles/Ceramics/Sanitaryware",
                "Glassware", "Pipes/Metal Rods > 7ft", "Pipes/Metal Rods < 7ft", "Metal Sheets",
                "Gas/Commercial Cylinder", "Construction Materials", "Garments/Apparel/Textile", "Electrical/Electronics", "Other Items"
        };

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 18; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("goods_title", items[i]);
            aList.add(hm);
        }

        String[] from = {"goods_title"};
        int[] to = {R.id.item_title};


        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), aList, R.layout.item_list_layout, from, to);
        final ListView androidListView = (ListView) mView.findViewById(R.id.listView);
        androidListView.setAdapter(simpleAdapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap<String,String>)androidListView.getItemAtPosition(position);
                String itemname = map.get("goods_title");
                Toast.makeText(getContext(), ""+itemname, Toast.LENGTH_SHORT).show();
                goods_to_ship_TextView.setText(itemname);
                ad_goodstoship = itemname;
                dialog.dismiss();
            }

        });
    }

    private void callFare(){

        double loading_charge = 0.00, unloading_charge = 0.00, pod_charge = 0.00;

        if (loading_switch == 0){
           loading_charge =  0.00;
        }

        if (loading_switch == 1){
            loading_charge =  60.0;
        }

        if (unloading_switch == 0){
            unloading_charge =  0.00;
        }

        if (unloading_switch == 1){
            unloading_charge =  60.0;
        }

        if (pod_switch == 0){
            pod_charge = 0.00;
        }

        if (pod_switch ==1){
            pod_charge = 30.0;
        }

        if (vehicle.equals("1")) {
            float carrierfare = (float) (((km-1.0)*7)+30);
            double carrierfare_round = Math.round(carrierfare);
            est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("2")) {
            float carrierfare = (float) ((km-3.0)*22)+180;
            double carrierfare_round = Math.round(carrierfare);
            est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("3")) {
            float carrierfare = (float) ((km-3.0)*29)+250;
            double carrierfare_round = Math.round(carrierfare);
            est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("4")) {
            float carrierfare = (float) ((km-3.0)*34)+300;
            double carrierfare_round = Math.round(carrierfare);
            est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }



    }

    private void callLead(){
        leadRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
        String PICKOFF_LOCATION = pickoff;
        String DROPOFF_LOCATION = dropoff;
        String VEHICLE = vehicle;
        String VEHICLE_TYPE = vehicle_type;
        String BASE_FARE = String.valueOf(est_fare);
        String KILOMETER = DistanceResult;


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());

        LeadData leadData = new LeadData(PICKOFF_LOCATION, DROPOFF_LOCATION, VEHICLE, VEHICLE_TYPE, BASE_FARE, KILOMETER);

        leadRef.child("LEADS_DATA").child(username).setValue(leadData);

        Toast.makeText(getContext(), "New Lead", Toast.LENGTH_SHORT).show();
    }


    private void callLoading(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("ODR");
        dbRef.child("odr-state").setValue("3");
        loadingDialog = new SpotsDialog.Builder().setContext(getContext())
                .setTheme(R.style.loading)
                .setMessage("Requesting Order")
                .build();
        loadingDialog.show();
        callSendVehicleRequest();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("ODR");
                dbRef.child("odr-state").setValue("0");
                dbRef = FirebaseDatabase.getInstance().getReference("ODR");
                dbRef.child("odr-state").setValue("-1");
                loadingDialog.dismiss();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View mView = layoutInflater.inflate(R.layout.request_vehicle_alert, null);
                builder = new AlertDialog.Builder(getContext());
                builder.setView(mView);
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                        BottomNavigationView bottom_nav_Bar;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            bottom_nav_Bar = (BottomNavigationView) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_Bar);
                           bottom_nav_Bar.setSelectedItemId(R.id.orders_item);
                        }

                    }

                }, SPLASH_TIME_OUT_REDIRECT);



            }

        }, SPLASH_TIME_OUT);
    }

    private void callFareLoading(){
        loadingDialog = new SpotsDialog.Builder().setContext(getContext())
                .setTheme(R.style.loading)
                .setMessage("Calculating Fare")
                .setCancelable(false)
                .build();
        loadingDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContext().getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);
                String only_number = number.replaceAll("[^a-zA-Z0-9]", "");
                if (only_number.length() == 12){
                    only_number = only_number.substring(2);
                }

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                if (contact_switch == 1){
                    pickoff_contact_TextView.setText(name+ " :: " +only_number);
                    ad_pickoff_contact_name = name;
                    ad_pickoff_contact_mobile = only_number;
                }

                if (contact_switch == 2){
                    dropoff_contact_TextView.setText(name + " :: " + only_number);
                    ad_dropoff_contact_name = name;
                    ad_dropoff_contact_mobile = only_number;
                }



            }
        }
    };

    private void callSendVehicleRequest(){

        callOrderNo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pickoff_location = String.valueOf(pickoff_latlng);
                dropoff_location = String.valueOf(dropoff_latlng);

                String micro_parcel = "MP";
                String doc_type = "ODR-";
                String gst_code_mp = "23-";
                Date odr_date = new Date( );
                SimpleDateFormat odr_simpleDateFormat = new SimpleDateFormat ("y");
                int sequence = Integer.parseInt(child_no);
                int order_sequence = sequence+1;
                String firebase_order_no = String.valueOf(order_sequence);

                DatabaseReference order_noRef = FirebaseDatabase.getInstance().getReference("APP_DATA");
                order_noRef.child("VARIABLES").child("order_no").setValue(firebase_order_no);

                mp_order_no = micro_parcel+doc_type+gst_code_mp+odr_simpleDateFormat.format(odr_date)+"-"+order_sequence;

                DatabaseReference active_orderRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
                ActiveData activeData = new ActiveData(ad_pickoff_address, ad_dropoff_address, ad_pickoff_contact_name, ad_pickoff_contact_mobile, ad_dropoff_contact_name, ad_dropoff_contact_mobile, ad_goodstoship, ad_fare, ad_km, mp_order_no,pickoff_location, dropoff_location, ad_vehicle, ad_vehicle_type, ad_loading, ad_unloading, ad_pod);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String username = firebaseUser.getUid();



                active_orderRef.child("ACTIVE_DATA").child(username).child(mp_order_no).setValue(activeData);

                DatabaseReference orderdataRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
                Date date = new Date( );
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("E dd:MM:y hh:mm a");

                od_date_time_of_order = simpleDateFormat.format(date);
                od_order_status = "3";

                if (ad_vehicle == 1){
                    od_service_type = "XS";
                }

                if (ad_vehicle == 2){
                    od_service_type = "XM";
                }

                if (ad_vehicle == 3){
                    od_service_type = "XL";
                }

                if (ad_vehicle == 4){
                    od_service_type = "X2L";
                }

                od_fare = ad_fare;

                OrderData orderData = new OrderData(od_date_time_of_order, od_service_type, od_fare, od_order_status, mp_order_no);
                orderdataRef.child("ORDERS").child("ONGOING_ORDERS").child(username).child(mp_order_no).setValue(orderData);




            }

        }, SPLASH_TIME_OUT_REDIRECT);

    }

    private void callOrderNo(){
        DatabaseReference order_noRef = FirebaseDatabase.getInstance().getReference("APP_DATA");
        order_noRef.child("VARIABLES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                child_no = (String) dataSnapshot.child("order_no").getValue();
                Toast.makeText(getContext(),child_no, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
