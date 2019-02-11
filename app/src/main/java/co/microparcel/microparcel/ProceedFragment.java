package co.microparcel.microparcel;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProceedFragment extends Fragment {

    View view;
    private String pickoff, dropoff, vehicle, vehicle_type;

    private String DistanceResult, DurationResult;

    private Button est_fare_book_now_Button;

    private AlertDialog.Builder builder, del;

    private TextView est_fare_TextView, goods_to_ship_TextView, opt_loading_TextView, opt_unloading_TextView, delivery_inst_or_cod_amt_TextView, opt_physical_pod_TextView, opt_insurance_TextView;

    private ProgressBar progressBar;

    private double km, carrierfare_round;

    private EditText goods_weight_EditText;

    private int loading_switch = 0, unloading_switch = 0, pod_switch = 0, insurance_switch = 0;

    public ProceedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_proceed, container, false);
        Reset();

        pickoff = getArguments().getString("pickoff");
        dropoff = getArguments().getString("dropoff");
        vehicle = String.valueOf(getArguments().getInt("vehicle"));
        vehicle_type = getArguments().getString("vehicle_type");

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        SearchDistanceCommand(view);

        est_fare_TextView = view.findViewById(R.id.est_fare_TextView);
        est_fare_book_now_Button = view.findViewById(R.id.est_fare_book_now_Button);
        est_fare_book_now_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDistanceCommand(v);
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
                    opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_right));
                    opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_left));
                } else {

                    if (loading_switch == 0) {
                        opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_left));
                        loading_switch = 1;
                    } else {
                        opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_left));
                        loading_switch = 0;

                    }

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
                    opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_right));
                    opt_loading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_left));
                } else {

                    if (unloading_switch == 0) {
                        opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_right));
                        unloading_switch = 1;
                    } else {
                        opt_unloading_TextView.setBackground(getResources().getDrawable(R.drawable.btn_nogravity_right));
                        unloading_switch = 0;
                    }

                    callFare();

                }

            }
        });


        delivery_inst_or_cod_amt_TextView = view.findViewById(R.id.delivery_inst_or_cod_amt_TextView);
        delivery_inst_or_cod_amt_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View mView = layoutInflater.inflate(R.layout.del_ins_cod_amt_alert, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    del = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                }
                del.setView(mView);
                Button submit_Button = mView.findViewById(R.id.submit_Button);
                final AlertDialog dialog = del.create();
                dialog.show();
                submit_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        opt_physical_pod_TextView = view.findViewById(R.id.opt_physical_pod_TextView);
        opt_physical_pod_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pod_switch == 0){
                    opt_physical_pod_TextView.setBackground(getResources().getDrawable(R.drawable.btn_from_border));
                    pod_switch = 1;
                }else {
                    opt_physical_pod_TextView.setBackground(getResources().getDrawable(R.drawable.loc_tv_to));
                    pod_switch = 0;
                }
                Toast.makeText(getContext(), "POD : "+pod_switch, Toast.LENGTH_SHORT).show();

                callFare();

            }
        });


        opt_insurance_TextView = view.findViewById(R.id.opt_insurance_TextView);
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
        });


        goods_weight_EditText = view.findViewById(R.id.goods_weight_EditText);




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
            progressBar.setVisibility(View.VISIBLE);
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

             progressBar.setVisibility(View.INVISIBLE);

            if (DistanceResult.indexOf("km") != -1) {
                DistanceResult = DistanceResult.replaceAll("[^\\d.]", "");
                km = Double.parseDouble(DistanceResult);
                callFare();

            } else {
                Toast.makeText(getContext(), "Can't Serve...", Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();
            }

        });
    }

    private void callFare(){

        double loading_charge = 0.00, unloading_charge = 0.00, pod_charge = 0.00, insurance_charge = 0.00;

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

        if (insurance_switch == 0){
            insurance_charge = 0.00;
        }

        if (insurance_switch == 1){
            insurance_charge = 40.0;
        }

        if (vehicle.equals("1")) {
            float carrierfare = (float) (((km-1.0)*7)+30);
            double carrierfare_round = Math.round(carrierfare);
            double est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge + insurance_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("2")) {
            float carrierfare = (float) ((km-3.0)*22)+180;
            double carrierfare_round = Math.round(carrierfare);
            double est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge + insurance_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("3")) {
            float carrierfare = (float) ((km-3.0)*29)+250;
            double carrierfare_round = Math.round(carrierfare);
            double est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge + insurance_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }

        if (vehicle.equals("4")) {
            float carrierfare = (float) ((km-3.0)*34)+300;
            double carrierfare_round = Math.round(carrierfare);
            double est_fare = carrierfare_round + loading_charge + unloading_charge + pod_charge + insurance_charge;
            est_fare_TextView.setText(String.format("₹%s", est_fare));
        }
    }





}
