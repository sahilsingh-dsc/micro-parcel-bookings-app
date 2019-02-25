package co.microparcel.microparcel.models;

public class OrderData {
    String od_date_time_of_order, od_service_type, od_fare, od_order_status;

    OrderData(){

    }

    public OrderData(String od_date_time_of_order, String od_service_type, String od_fare, String od_order_status) {
        this.od_date_time_of_order = od_date_time_of_order;
        this.od_service_type = od_service_type;
        this.od_fare = od_fare;
        this.od_order_status = od_order_status;
    }

    public String getOd_date_time_of_order() {
        return od_date_time_of_order;
    }

    public void setOd_date_time_of_order(String od_date_time_of_order) {
        this.od_date_time_of_order = od_date_time_of_order;
    }

    public String getOd_service_type() {
        return od_service_type;
    }

    public void setOd_service_type(String od_service_type) {
        this.od_service_type = od_service_type;
    }

    public String getOd_fare() {
        return od_fare;
    }

    public void setOd_fare(String od_fare) {
        this.od_fare = od_fare;
    }

    public String getOd_order_status() {
        return od_order_status;
    }

    public void setOd_order_status(String od_order_status) {
        this.od_order_status = od_order_status;
    }
}
