package co.microparcel.microparcel.Ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.Objects;

import co.microparcel.microparcel.R;

import static android.support.constraint.Constraints.TAG;

public class MoreFragment extends Fragment {

    View view;
    private Button logout_Button, call_us_Button;
    private TextView customer_name_TextView, customer_email_TextView, customer_mobile_no_TextView, customer_type_TextView, mail_us_TextView, customer_gst_TextView;
    public MoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_more, container, false);

        logout_Button = view.findViewById(R.id.logout_Button);
        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogout();
            }
        });
        customer_name_TextView = view.findViewById(R.id.customer_name_TextView);
        customer_mobile_no_TextView = view.findViewById(R.id.customer_mobile_no_Textview);
        customer_email_TextView = view.findViewById(R.id.customer_email_TextView);

        customer_type_TextView = view.findViewById(R.id.customer_type_TextView);
        mail_us_TextView = view.findViewById(R.id.mail_us_TextView);
        call_us_Button = view.findViewById(R.id.call_us_Button);
        call_us_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:8770664141"));
                        startActivity(callIntent);
            }
        });
        customer_gst_TextView = view.findViewById(R.id.customer_gst_TextView);

        mail_us_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:contact@microparcel.co"));
            }
        });

        callCustomerDetails();

        return view;
    }

    private void callLogout(){
        AlertDialog.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setMessage("Are you sure, you want to logout ?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callCustomerDetails(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String username = firebaseUser.getUid();
        String mobilenumber = firebaseUser.getPhoneNumber();
        customer_mobile_no_TextView.setText(mobilenumber);

        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("COLLECTION");
        customerRef.child("CUSTOMERS").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String customer_type = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    customer_type = Objects.requireNonNull(dataSnapshot.child("cd_profile_type").getValue()).toString();


                    if (customer_type.equals("1")) {
                        String name = Objects.requireNonNull(dataSnapshot.child("cd_name").getValue()).toString();
                        String email = Objects.requireNonNull(dataSnapshot.child("cd_email_id").getValue()).toString();
                        customer_type_TextView.setText("Personal User");
                        customer_name_TextView.setText(name);
                        customer_email_TextView.setText(email);
                                            }

                    if (customer_type.equals("2")) {
                        String name = Objects.requireNonNull(dataSnapshot.child("cd_name").getValue()).toString();
                        String email = Objects.requireNonNull(dataSnapshot.child("cd_email_id").getValue()).toString();
                        String gst_no = Objects.requireNonNull(dataSnapshot.child("cd_gst_no").getValue()).toString();
                        customer_type_TextView.setText("Business User");
                        customer_name_TextView.setText(name);
                        customer_email_TextView.setText(email);
                        customer_gst_TextView.setText(String.format("GSTIN %s", gst_no));

                    }
                }

                customer_type_TextView.setVisibility(View.VISIBLE);
                customer_mobile_no_TextView.setVisibility(View.VISIBLE);
                customer_name_TextView.setVisibility(View.VISIBLE);
                customer_email_TextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
