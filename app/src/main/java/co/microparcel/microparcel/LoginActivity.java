package co.microparcel.microparcel;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private Button send_otp_Button, verify_otp_Button;
    private EditText cust_mobile_number_EditText, cust_mobile_code_EditText;
    private TextView resend_otp_TextView, otp_timer_TextView;
    private Animation aniSlide;
    private ConstraintLayout loginLayout;

    private FirebaseAuth mAuth, firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        loginLayout = findViewById(R.id.loginLayout);

        resend_otp_TextView = findViewById(R.id.resend_otp_TextView);
        otp_timer_TextView = findViewById(R.id.otp_timer_TextView);

        send_otp_Button = findViewById(R.id.send_otp_Button);
        verify_otp_Button = findViewById(R.id.verify_otp_Button);
        cust_mobile_number_EditText = findViewById(R.id.cust_mobile_number_EditText);
        cust_mobile_code_EditText = findViewById(R.id.cust_mobile_code_EditText);

        aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        send_otp_Button.setAnimation(aniSlide);
        cust_mobile_number_EditText.setAnimation(aniSlide);

        send_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobilenumber = cust_mobile_number_EditText.getText().toString().trim();

                if (TextUtils.isEmpty(mobilenumber)) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Enter your mobile number.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    cust_mobile_number_EditText.requestFocus();
                    return;

                }

                if (mobilenumber.length() < 10 || mobilenumber.length() > 10) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Enter a valid 10 digit mobile number.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    cust_mobile_number_EditText.requestFocus();
                    return;
                }

                if (!TextUtils.isDigitsOnly(mobilenumber)) {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, "Enter a valid 10 digit mobile number.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    cust_mobile_number_EditText.requestFocus();
                    return;
                }

                startPhoneNumberVerification(mobilenumber);
                startTimer();
                callMobileHider();

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

        verify_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = cust_mobile_code_EditText.getText().toString().trim();
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });



    }


    private void callVerifyCode(){
        String code = cust_mobile_code_EditText.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            Snackbar snackbar = Snackbar
                    .make(loginLayout, "Please enter code.", Snackbar.LENGTH_LONG);
            snackbar.show();
            cust_mobile_number_EditText.requestFocus();
            return;

        }

        if (code.length() < 6 || code.length() > 6) {
            Snackbar snackbar = Snackbar
                    .make(loginLayout, "Enter a valid 6 digit code.", Snackbar.LENGTH_LONG);
            snackbar.show();
            cust_mobile_number_EditText.requestFocus();
            return;
        }

        if (!TextUtils.isDigitsOnly(code)){
            Snackbar snackbar = Snackbar
                    .make(loginLayout, "Enter a valid 6 digit code.", Snackbar.LENGTH_LONG);
            snackbar.show();
            cust_mobile_number_EditText.requestFocus();
            return;
        }



    }

    private void callMobileHider(){
        cust_mobile_number_EditText.setVisibility(View.INVISIBLE);
        send_otp_Button.setVisibility(View.INVISIBLE);
        cust_mobile_code_EditText.setVisibility(View.VISIBLE);
        verify_otp_Button.setVisibility(View.VISIBLE);
    }

    private void callCodeHider(){
        cust_mobile_code_EditText.setVisibility(View.INVISIBLE);
        verify_otp_Button.setVisibility(View.INVISIBLE);
        cust_mobile_number_EditText.setVisibility(View.VISIBLE);
        send_otp_Button.setVisibility(View.VISIBLE);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(loginLayout, "दर्ज ओटीपी मान्य नहीं है |",
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
            }
        };
        cTimer.start();
    }


}
