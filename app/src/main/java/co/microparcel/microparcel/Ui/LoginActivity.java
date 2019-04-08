package co.microparcel.microparcel.Ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import co.microparcel.microparcel.R;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText user_mob_EditText, otp_code_EditText;
    private Button get_otp_Button, verify_and_login_Button, change_mobile_no_Button, resend_otp_Button;
    private LinearLayout resend_mob_LinearLayout;
    private TextView otp_countdown_TextView, auto_verify_TextView;
    private ProgressBar auto_verify_ProgressBar;
    private FirebaseAuth mAuth;
    private AlertDialog loadingDialog;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loadingDialog = new SpotsDialog.Builder().setContext(LoginActivity.this)
                .setTheme(R.style.loading)
                .setMessage("Verifing OTP")
                .setCancelable(false)
                .build();


        user_mob_EditText = findViewById(R.id.user_mob_EditText);
        otp_code_EditText = findViewById(R.id.otp_code_EditText);
        get_otp_Button = findViewById(R.id.get_otp_Button);
        verify_and_login_Button = findViewById(R.id.verify_and_login_Button);
        resend_mob_LinearLayout = findViewById(R.id.resend_mob_LinearLayout);
        change_mobile_no_Button = findViewById(R.id.change_mobile_no_Button);
        resend_otp_Button = findViewById(R.id.resend_otp_Button);
        resend_mob_LinearLayout = findViewById(R.id.resend_mob_LinearLayout);
        otp_countdown_TextView = findViewById(R.id.otp_countdown_TextView);
        auto_verify_ProgressBar = findViewById(R.id.auto_verify_ProgressBar);
        auto_verify_TextView = findViewById(R.id.auto_verify_TextView);

        get_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobilenumber = user_mob_EditText.getText().toString().trim();
                if (TextUtils.isEmpty(mobilenumber)) {
                    Toast.makeText(LoginActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(mobilenumber)) {
                    Toast.makeText(LoginActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mobilenumber.length() < 10 || mobilenumber.length() > 10) {
                    Toast.makeText(LoginActivity.this, "Enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                callSendOtpHider();
                startPhoneNumberVerification(mobilenumber);
            }
        });

        verify_and_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otpcode = otp_code_EditText.getText().toString().trim();
                if (TextUtils.isEmpty(otpcode)) {
                    Toast.makeText(LoginActivity.this, "Enter otp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(otpcode)) {
                    Toast.makeText(LoginActivity.this, "Enter valid otp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (otpcode.length() < 6 || otpcode.length() > 6) {
                    Toast.makeText(LoginActivity.this, "Enter valid 6 digit otp", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, otpcode);

            }
        });

        change_mobile_no_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangeMobHider();
            }
        });

        resend_otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp_countdown_TextView.setVisibility(View.VISIBLE);
                countDownTimer.start();
                resend_mob_LinearLayout.setVisibility(View.INVISIBLE);
                auto_verify_ProgressBar.setVisibility(View.VISIBLE);
                auto_verify_TextView.setVisibility(View.VISIBLE);

                String mobilenumber = user_mob_EditText.getText().toString().trim();
                if (TextUtils.isEmpty(mobilenumber)) {
                    Toast.makeText(LoginActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(mobilenumber)) {
                    Toast.makeText(LoginActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mobilenumber.length() < 10 || mobilenumber.length() > 10) {
                    Toast.makeText(LoginActivity.this, "Enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                resendVerificationCode(mobilenumber, mResendToken);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    callChangeMobHider();
                    loadingDialog.dismiss();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this, "Quota Exceeded", Toast.LENGTH_SHORT).show();
                    callChangeMobHider();
                    loadingDialog.dismiss();
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


        private void callSendOtpHider () {
            user_mob_EditText.setVisibility(View.INVISIBLE);
            get_otp_Button.setVisibility(View.INVISIBLE);
            otp_code_EditText.setVisibility(View.VISIBLE);
            verify_and_login_Button.setVisibility(View.VISIBLE);
            auto_verify_ProgressBar.setVisibility(View.VISIBLE);
            auto_verify_TextView.setVisibility(View.VISIBLE);
            countDownTimer.start();
        }

        private void callChangeMobHider () {
            user_mob_EditText.setVisibility(View.VISIBLE);
            get_otp_Button.setVisibility(View.VISIBLE);
            otp_code_EditText.setVisibility(View.INVISIBLE);
            verify_and_login_Button.setVisibility(View.INVISIBLE);
            resend_mob_LinearLayout.setVisibility(View.INVISIBLE);
            auto_verify_ProgressBar.setVisibility(View.INVISIBLE);
            auto_verify_TextView.setVisibility(View.INVISIBLE);
            resend_mob_LinearLayout.setVisibility(View.INVISIBLE);

            countDownTimer.cancel();
        }

        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                otp_countdown_TextView.setText(String.format("%s sec", String.valueOf(millisUntilFinished / 1000)));
            }

            public void onFinish() {
                resend_mob_LinearLayout.setVisibility(View.VISIBLE);
                change_mobile_no_Button.setVisibility(View.VISIBLE);
                resend_otp_Button.setVisibility(View.VISIBLE);
                otp_countdown_TextView.setVisibility(View.INVISIBLE);
                auto_verify_ProgressBar.setVisibility(View.INVISIBLE);
                auto_verify_TextView.setVisibility(View.INVISIBLE);
            }
        };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loadingDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert firebaseUser != null;
                            final String username = firebaseUser.getUid();
                            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("COLLECTION");
                            profileRef.child("CUSTOMERS").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(username).exists()){
                                        Toast.makeText(LoginActivity.this, "exist", Toast.LENGTH_SHORT).show();


                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        loadingDialog.dismiss();
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this, "not exist", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, ProfileRegActivity.class));
                                        finish();
                                        loadingDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
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
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}




