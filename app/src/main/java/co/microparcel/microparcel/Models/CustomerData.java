package co.microparcel.microparcel.Models;

public class CustomerData {
    String cd_profile_type, cd_name, cd_email_id, cd_gst_no, cd_what_you_ship, cd_how_freq_you_ship;

    public CustomerData(){

    }

    public CustomerData(String cd_profile_type, String cd_name, String cd_email_id, String cd_gst_no, String cd_what_you_ship, String cd_how_freq_you_ship) {
        this.cd_profile_type = cd_profile_type;
        this.cd_name = cd_name;
        this.cd_email_id = cd_email_id;
        this.cd_gst_no = cd_gst_no;
        this.cd_what_you_ship = cd_what_you_ship;
        this.cd_how_freq_you_ship = cd_how_freq_you_ship;
    }

    public String getCd_profile_type() {
        return cd_profile_type;
    }

    public void setCd_profile_type(String cd_profile_type) {
        this.cd_profile_type = cd_profile_type;
    }

    public String getCd_name() {
        return cd_name;
    }

    public void setCd_name(String cd_name) {
        this.cd_name = cd_name;
    }

    public String getCd_email_id() {
        return cd_email_id;
    }

    public void setCd_email_id(String cd_email_id) {
        this.cd_email_id = cd_email_id;
    }

    public String getCd_gst_no() {
        return cd_gst_no;
    }

    public void setCd_gst_no(String cd_gst_no) {
        this.cd_gst_no = cd_gst_no;
    }

    public String getCd_what_you_ship() {
        return cd_what_you_ship;
    }

    public void setCd_what_you_ship(String cd_what_you_ship) {
        this.cd_what_you_ship = cd_what_you_ship;
    }

    public String getCd_how_freq_you_ship() {
        return cd_how_freq_you_ship;
    }

    public void setCd_how_freq_you_ship(String cd_how_freq_you_ship) {
        this.cd_how_freq_you_ship = cd_how_freq_you_ship;
    }
}
