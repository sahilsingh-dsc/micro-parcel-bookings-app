package co.microparcel.microparcel.Ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import co.microparcel.microparcel.R;
import dmax.dialog.SpotsDialog;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_customer_email_EditText, signup_customer_password_EditText;
    private Button signup_Button;
    private AlertDialog loadingDialog;
    private TextView existing_user_Textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loadingDialog = new SpotsDialog.Builder().setContext(SignupActivity.this)
                .setTheme(R.style.loading)
                .setMessage("Registering User")
                .setCancelable(false)
                .build();

        signup_customer_email_EditText = findViewById(R.id.signup_customer_email_EditText);
        signup_customer_password_EditText = findViewById(R.id.signup_customer_password_EditText);
        signup_Button = findViewById(R.id.signup_Button);


        existing_user_Textview = findViewById(R.id.existing_user_Textview);
        existing_user_Textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_customer_email_EditText.getText().toString().trim();
                String password = signup_customer_password_EditText.getText().toString().trim();

                if (email.length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() == 0 || password.length() < 6){
                    Toast.makeText(SignupActivity.this, "Enter a valid password with min 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                callSignupWithFirebase(email, password);
            }
        });

    }

    private void callSignupWithFirebase(String email, String password) {
        loadingDialog.show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   startActivity(new Intent(SignupActivity.this, ProfileRegActivity.class));
                    loadingDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                }else {
                    loadingDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Registration Unsuccessfull", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
