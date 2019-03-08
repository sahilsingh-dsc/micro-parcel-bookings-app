package co.microparcel.microparcel.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.microparcel.microparcel.Models.CustomerData;
import co.microparcel.microparcel.R;

public class ProfileRegActivity extends AppCompatActivity {

    private LinearLayout personal_LinearLayout, business_LinearLayout;
    private EditText name_EditText, email_id_EditText, gst_no_EditText, what_you_ship_EditText, how_feq_ship_EditText;
    private String cd_name, cd_email_id, cd_gst_no, cd_what_you_ship, cd_how_freq_you_ship;
    private String cd_profile_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_reg);

        personal_LinearLayout = findViewById(R.id.personal_LinearLayout);
        business_LinearLayout = findViewById(R.id.business_LinearLayout);
        name_EditText = findViewById(R.id.name_EditText);
        email_id_EditText = findViewById(R.id.email_id_EditText);
        gst_no_EditText = findViewById(R.id.gst_no_EditText);
        what_you_ship_EditText = findViewById(R.id.what_you_ship_EditText);
        how_feq_ship_EditText = findViewById(R.id.how_feq_ship_EditText);

        Button submit_Button = findViewById(R.id.submit_Button);

        callSelectPersonalUser();

        personal_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSelectPersonalUser();
            }
        });

        business_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSelectBusinessUser();
            }
        });

        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd_profile_switch.equals("1")){

                    cd_name = name_EditText.getText().toString().trim();
                    cd_email_id = email_id_EditText.getText().toString().trim();

                    if (cd_name.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (cd_email_id.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    callSaveCustomerData();
                }


                if (cd_profile_switch.equals("2")){

                    cd_name = name_EditText.getText().toString().trim();
                    cd_email_id = email_id_EditText.getText().toString().trim();
                    cd_gst_no = gst_no_EditText.getText().toString().trim();
                    cd_what_you_ship = what_you_ship_EditText.getText().toString().trim();
                    cd_how_freq_you_ship = how_feq_ship_EditText.getText().toString().trim();


                    if (cd_name.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter your business name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cd_email_id.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (cd_gst_no.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter your business gst number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (cd_what_you_ship.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter what you generally ship", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (cd_how_freq_you_ship.length() == 0){
                        Toast.makeText(ProfileRegActivity.this, "Please enter how frequently you ship", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    callSaveCustomerData();
                }



            }
        });

    }

    private void callSelectPersonalUser(){

        cd_profile_switch = "1";
        personal_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        business_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        gst_no_EditText.setVisibility(View.INVISIBLE);
        what_you_ship_EditText.setVisibility(View.INVISIBLE);
        how_feq_ship_EditText.setVisibility(View.INVISIBLE);
        gst_no_EditText.setText("");
        what_you_ship_EditText.setText("");
        how_feq_ship_EditText.setText("");

    }

    private void callSelectBusinessUser(){

        cd_profile_switch = "2";
        personal_LinearLayout.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        business_LinearLayout.setBackground(getResources().getDrawable(R.drawable.button_background));
        gst_no_EditText.setVisibility(View.VISIBLE);
        what_you_ship_EditText.setVisibility(View.VISIBLE);
        how_feq_ship_EditText.setVisibility(View.VISIBLE);
        name_EditText.setText("");
        email_id_EditText.setText("");

    }

    private void callSaveCustomerData(){

        CustomerData customerData = new CustomerData(cd_profile_switch, cd_name, cd_email_id, cd_gst_no, cd_what_you_ship, cd_how_freq_you_ship);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();

        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("COLLECTION");
        customerRef.child("CUSTOMERS").child(username).setValue(customerData);

        Toast.makeText(this, "User information saved", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(ProfileRegActivity.this, MainActivity.class));

    }

}
