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

    private EditText cust_mobile_number_EditText, cust_mobile_code_EditText;
    private Button send_otp_Button, verify_otp_Button;
    private TextView resend_otp_TextView, otp_timer_TextView;
    private ConstraintLayout loginLayout;
    private FirebaseAuth mAuth, firebaseAuth;
    AlertDialog autoVerifyDialog;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        autoVerifyDialog = new SpotsDialog.Builder().setContext(LoginActivity.this)
                .setTheme(R.style.loading)
                .setMessage("Auto Verifying OTP")
                .setCancelable(false)
                .build();


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        loginLayout = findViewById(R.id.loginLayout);

        cust_mobile_number_EditText = findViewById(R.id.cust_mobile_number_EditText);
        cust_mobile_code_EditText = findViewById(R.id.cust_mobile_code_EditText);


        otp_timer_TextView = findViewById(R.id.otp_timer_TextView);
        resend_otp_TextView = findViewById(R.id.resend_otp_TextView);

        send_otp_Button = findViewById(R.id.send_otp_Button);
        send_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = cust_mobile_number_EditText.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Enter valid mobile number.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    cust_mobile_number_EditText.requestFocus();
                    return;

                }

                if (mobile.length() < 10 || mobile.length() > 10) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Mobile number must be of 10 digits.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    cust_mobile_number_EditText.requestFocus();
                    return;
                }

                startPhoneNumberVerification(mobile);
                cust_mobile_number_EditText.setVisibility(View.INVISIBLE);
                send_otp_Button.setVisibility(View.INVISIBLE);
                cust_mobile_code_EditText.setVisibility(View.VISIBLE);
                otp_timer_TextView.setVisibility(View.VISIBLE);
                verify_otp_Button.setVisibility(View.VISIBLE);
                autoVerifyDialog.show();
                startTimer();
            }

        });

        resend_otp_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cust_mobile_code_EditText.setText("");
                String mobileno = cust_mobile_number_EditText.getText().toString().trim();
                resendVerificationCode(mobileno, mResendToken);
                otp_timer_TextView.setVisibility(View.VISIBLE);
                startTimer();
                resend_otp_TextView.setVisibility(View.INVISIBLE);
            }
        });


        verify_otp_Button = findViewById(R.id.verify_otp_Button);
        verify_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = cust_mobile_code_EditText.getText().toString().trim();
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Enter valid mobile number.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(loginLayout, "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            autoVerifyDialog.dismiss();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(loginLayout, "This otp is invalid.",
                                        Snackbar.LENGTH_LONG).show();
                                cust_mobile_code_EditText.setEnabled(true);
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getString(R.string.country_code)+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getString(R.string.country_code)+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    CountDownTimer cTimer = null;

    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                otp_timer_TextView.setText(millisUntilFinished / 1000+" sec");
            }
            public void onFinish() {
                resend_otp_TextView.setVisibility(View.VISIBLE);
                otp_timer_TextView.setVisibility(View.INVISIBLE);
                autoVerifyDialog.dismiss();
            }
        };
        cTimer.start();
    }


}
