package co.microparcel.microparcel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.microparcel.microparcel.R;
import co.microparcel.microparcel.models.OrderData;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderData> orderData;
    private Context context;

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
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder orderViewHolder, int i) {

        final OrderData od = orderData.get(i);
        orderViewHolder.date_time_order_TextView.setText(od.getOd_date_time_of_order());
        orderViewHolder.service_type_TextView.setText(od.getOd_service_type());
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
        orderViewHolder.order_fare_TextView.setText(od.getOd_fare());

    }


    @Override
    public int getItemCount() {
        return orderData.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView date_time_order_TextView, order_status_TextView, service_type_TextView, order_fare_TextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            date_time_order_TextView = itemView.findViewById(R.id.date_time_order_TextView);
            order_status_TextView = itemView.findViewById(R.id.order_status_TextView);
            service_type_TextView = itemView.findViewById(R.id.service_type_TextView);
            order_fare_TextView = itemView.findViewById(R.id.order_fare_TextView);

        }
    }
}
