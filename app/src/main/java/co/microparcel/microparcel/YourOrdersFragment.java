package co.microparcel.microparcel;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.microparcel.microparcel.Adapters.OrderAdapter;
import co.microparcel.microparcel.models.OrderData;

public class YourOrdersFragment extends Fragment {

    View view;
    private RecyclerView orders_RecyclerView;
    private RecyclerView.Adapter orderAdapter;
    private List<OrderData> orderData;
    private ProgressBar loading_orders_ProgressBar;

    public YourOrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_your_orders, container, false);

        loading_orders_ProgressBar = view.findViewById(R.id.loading_orders_ProgressBar);
        loading_orders_ProgressBar.setVisibility(View.INVISIBLE);

        orders_RecyclerView = view.findViewById(R.id.orders_RecyclerView);

        orders_RecyclerView = view.findViewById(R.id.orders_RecyclerView);
        orders_RecyclerView.hasFixedSize();
        orders_RecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        orderData = new ArrayList<>();
        orderData.clear();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();
        loading_orders_ProgressBar.setVisibility(View.VISIBLE);
        DatabaseReference orderdataRef = FirebaseDatabase.getInstance().getReference("BOOKINGS_DATA");
        orderdataRef.child("ORDERS").child("ONGOING_ORDERS").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderData.clear();
                for (DataSnapshot orderSnap : dataSnapshot.getChildren()){

                    String od_date_time_of_order, od_service_type, od_fare, od_order_status;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                        od_date_time_of_order = (String) orderSnap.child("od_date_time_of_order").getValue();
                        od_service_type = (String) orderSnap.child("od_service_type").getValue();
                        od_fare = (String) orderSnap.child("od_fare").getValue();
                        od_order_status = (String) orderSnap.child("od_order_status").getValue();

                        OrderData od = new OrderData(od_date_time_of_order, od_service_type, od_fare, od_order_status);
                        orderData.add(od);

                    }

                }

                orderAdapter = new OrderAdapter(orderData,view.getContext());
                orders_RecyclerView.setAdapter(orderAdapter);
                loading_orders_ProgressBar.setVisibility(View.INVISIBLE);
                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }


}
