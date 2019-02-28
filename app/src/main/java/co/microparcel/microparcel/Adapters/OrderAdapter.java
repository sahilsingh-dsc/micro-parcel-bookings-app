package co.microparcel.microparcel.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import co.microparcel.microparcel.R;
import co.microparcel.microparcel.Models.OrderData;
import co.microparcel.microparcel.Ui.CreateOrderFragment;
import co.microparcel.microparcel.Ui.OrderInfoFragment;
import co.microparcel.microparcel.Ui.ProceedFragment;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderData> orderData;
    private Context context;
    private String rootId;
    String ad_pickoff_address, ad_dropoff_address;
    String od_date_time_of_order, od_service_type, od_fare, od_order_status;
    String order_no;
    private AlertDialog.Builder builder;

    public OrderAdapter(List<OrderData> orderData, Context context) {
        this.orderData = orderData;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.your_orders_items_layout, viewGroup, false);

        return new OrderViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final OrderAdapter.OrderViewHolder orderViewHolder, int i) {

        final OrderData od = orderData.get(i);
        orderViewHolder.date_time_order_TextView.setText(od.getOd_date_time_of_order());
        orderViewHolder.service_type_TextView.setText(od.getOd_service_type());
        if (od.getOd_order_status().equals("3")){
            orderViewHolder.order_status_TextView.setText("Driver Allotment Pending");
            orderViewHolder.order_status_TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_local_shipping_black_24dp, 0, 0, 0);
        }
        if (od.getOd_order_status().equals("2")){
            orderViewHolder.order_status_TextView.setText("Trip Ongoing");
            orderViewHolder.order_status_TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_timeline_black_24dp, 0, 0, 0);
        }
        if (od.getOd_order_status().equals("1")){
            orderViewHolder.order_status_TextView.setText("Trip Completed");
            orderViewHolder.order_status_TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all_black_24dp, 0, 0, 0);
        }
        if (od.getOd_order_status().equals("0")){
            orderViewHolder.order_status_TextView.setText("Trip Canceled");
            orderViewHolder.order_status_TextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_black_24dp, 0, 0, 0);
        }

        orderViewHolder.order_no_TextView.setText(od.getOd_order_no());

        orderViewHolder.order_fare_TextView.setText(od.getOd_fare());
        orderViewHolder.order_item_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_no = od.getOd_order_no();
                OrderInfoFragment orderInfoFragment = new OrderInfoFragment();
                Bundle args = new Bundle();
                args.putString("order_no", order_no);
                orderInfoFragment.setArguments(args);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmant_holder_FrameLayout, orderInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView date_time_order_TextView, order_status_TextView, service_type_TextView, order_fare_TextView, order_no_TextView;
        private CardView order_item_CardView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            date_time_order_TextView = itemView.findViewById(R.id.date_time_order_TextView);
            order_status_TextView = itemView.findViewById(R.id.order_status_TextView);
            service_type_TextView = itemView.findViewById(R.id.service_type_TextView);
            order_fare_TextView = itemView.findViewById(R.id.order_fare_TextView);
            order_item_CardView = itemView.findViewById(R.id.order_item_CardView);
            order_no_TextView = itemView.findViewById(R.id.order_no_TextView);

        }
    }
}
