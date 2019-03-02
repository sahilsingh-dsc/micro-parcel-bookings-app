package co.microparcel.microparcel.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import co.microparcel.microparcel.R;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText customer_email_EditText, customer_password_EditText;
    private Button login_Button;
    private AlertDialog loadingDialog;
    private TextView signup_act_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loadingDialog = new SpotsDialog.Builder().setContext(LoginActivity.this)
                .setTheme(R.style.loading)
                .setMessage("Authenticating User")
                .setCancelable(false)
                .build();

        customer_email_EditText = findViewById(R.id.customer_email_EditText);
        customer_password_EditText = findViewById(R.id.customer_password_EditText);
        login_Button = findViewById(R.id.login_Button);

        signup_act_TextView = findViewById(R.id.signup_act_TextView);
        signup_act_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = customer_email_EditText.getText().toString().trim();
                String password = customer_password_EditText.getText().toString().trim();

                if (email.length() == 0){
                    Toast.makeText(LoginActivity.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() == 0 || password.length() < 6){
                    Toast.makeText(LoginActivity.this, "Enter a valid password with min 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                callLoginWithFirebase(email, password);
            }
        });

    }

    private void callLoginWithFirebase(String email, String password) {
        loadingDialog.show();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Auth Successfull", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Auth Unsuccessfull", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });

    }

}
