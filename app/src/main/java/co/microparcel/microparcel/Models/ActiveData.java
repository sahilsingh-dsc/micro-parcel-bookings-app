package co.microparcel.microparcel.Models;

import java.util.Date;

public class ActiveData {

    private String ad_pickoff_address, ad_dropoff_address, ad_pickoff_contact_name, ad_pickoff_contact_mobile, ad_dropoff_contact_name, ad_dropoff_contact_mobile, ad_goodstoship, ad_fare, ad_km, ad_order_no, ad_pickoff_latlng, ad_dropoff_latlng;
    private Integer ad_vehicle, ad_vehicle_type, ad_loading, ad_unloading, ad_pod;

    public ActiveData(){

    }

    public ActiveData(String ad_pickoff_address, String ad_dropoff_address, String ad_pickoff_contact_name, String ad_pickoff_contact_mobile, String ad_dropoff_contact_name, String ad_dropoff_contact_mobile, String ad_goodstoship, String ad_fare, String ad_km, String ad_order_no, String ad_pickoff_latlng, String ad_dropoff_latlng, Integer ad_vehicle, Integer ad_vehicle_type, Integer ad_loading, Integer ad_unloading, Integer ad_pod) {
        this.ad_pickoff_address = ad_pickoff_address;
        this.ad_dropoff_address = ad_dropoff_address;
        this.ad_pickoff_contact_name = ad_pickoff_contact_name;
        this.ad_pickoff_contact_mobile = ad_pickoff_contact_mobile;
        this.ad_dropoff_contact_name = ad_dropoff_contact_name;
        this.ad_dropoff_contact_mobile = ad_dropoff_contact_mobile;
        this.ad_goodstoship = ad_goodstoship;
        this.ad_fare = ad_fare;
        this.ad_km = ad_km;
        this.ad_order_no = ad_order_no;
        this.ad_pickoff_latlng = ad_pickoff_latlng;
        this.ad_dropoff_latlng = ad_dropoff_latlng;
        this.ad_vehicle = ad_vehicle;
        this.ad_vehicle_type = ad_vehicle_type;
        this.ad_loading = ad_loading;
        this.ad_unloading = ad_unloading;
        this.ad_pod = ad_pod;
    }

    public String getAd_pickoff_address() {
        return ad_pickoff_address;
    }

    public void setAd_pickoff_address(String ad_pickoff_address) {
        this.ad_pickoff_address = ad_pickoff_address;
    }

    public String getAd_dropoff_address() {
        return ad_dropoff_address;
    }

    public void setAd_dropoff_address(String ad_dropoff_address) {
        this.ad_dropoff_address = ad_dropoff_address;
    }

    public String getAd_pickoff_contact_name() {
        return ad_pickoff_contact_name;
    }

    public void setAd_pickoff_contact_name(String ad_pickoff_contact_name) {
        this.ad_pickoff_contact_name = ad_pickoff_contact_name;
    }

    public String getAd_pickoff_contact_mobile() {
        return ad_pickoff_contact_mobile;
    }

    public void setAd_pickoff_contact_mobile(String ad_pickoff_contact_mobile) {
        this.ad_pickoff_contact_mobile = ad_pickoff_contact_mobile;
    }

    public String getAd_dropoff_contact_name() {
        return ad_dropoff_contact_name;
    }

    public void setAd_dropoff_contact_name(String ad_dropoff_contact_name) {
        this.ad_dropoff_contact_name = ad_dropoff_contact_name;
    }

    public String getAd_dropoff_contact_mobile() {
        return ad_dropoff_contact_mobile;
    }

    public void setAd_dropoff_contact_mobile(String ad_dropoff_contact_mobile) {
        this.ad_dropoff_contact_mobile = ad_dropoff_contact_mobile;
    }

    public String getAd_goodstoship() {
        return ad_goodstoship;
    }

    public void setAd_goodstoship(String ad_goodstoship) {
        this.ad_goodstoship = ad_goodstoship;
    }

    public String getAd_fare() {
        return ad_fare;
    }

    public void setAd_fare(String ad_fare) {
        this.ad_fare = ad_fare;
    }

    public String getAd_km() {
        return ad_km;
    }

    public void setAd_km(String ad_km) {
        this.ad_km = ad_km;
    }

    public String getAd_order_no() {
        return ad_order_no;
    }

    public void setAd_order_no(String ad_order_no) {
        this.ad_order_no = ad_order_no;
    }

    public String getAd_pickoff_latlng() {
        return ad_pickoff_latlng;
    }

    public void setAd_pickoff_latlng(String ad_pickoff_latlng) {
        this.ad_pickoff_latlng = ad_pickoff_latlng;
    }

    public String getAd_dropoff_latlng() {
        return ad_dropoff_latlng;
    }

    public void setAd_dropoff_latlng(String ad_dropoff_latlng) {
        this.ad_dropoff_latlng = ad_dropoff_latlng;
    }

    public Integer getAd_vehicle() {
        return ad_vehicle;
    }

    public void setAd_vehicle(Integer ad_vehicle) {
        this.ad_vehicle = ad_vehicle;
    }

    public Integer getAd_vehicle_type() {
        return ad_vehicle_type;
    }

    public void setAd_vehicle_type(Integer ad_vehicle_type) {
        this.ad_vehicle_type = ad_vehicle_type;
    }

    public Integer getAd_loading() {
        return ad_loading;
    }

    public void setAd_loading(Integer ad_loading) {
        this.ad_loading = ad_loading;
    }

    public Integer getAd_unloading() {
        return ad_unloading;
    }

    public void setAd_unloading(Integer ad_unloading) {
        this.ad_unloading = ad_unloading;
    }

    public Integer getAd_pod() {
        return ad_pod;
    }

    public void setAd_pod(Integer ad_pod) {
        this.ad_pod = ad_pod;
    }
}
