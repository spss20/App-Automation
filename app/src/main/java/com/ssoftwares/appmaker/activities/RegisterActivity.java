package com.ssoftwares.appmaker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.mukesh.OtpView;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutCompany, textInputLayoutUsername, textInputLayoutMobile;
    Button buttonSubmit;
    String username, email, mobile, password, companyName;
    ApiService service;
    TextView loginTextView;
    FirebaseAuth firebaseAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    BottomSheetDialog bottomSheetDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutUsername = findViewById(R.id.textInputUsername);
        textInputLayoutMobile = findViewById(R.id.textInputMobile);
        textInputLayoutPassword = findViewById(R.id.textInputPassword);
        textInputLayoutCompany = findViewById(R.id.textInputCompanyName);
        buttonSubmit = findViewById(R.id.sign_up);
        loginTextView = findViewById(R.id.loginTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        service = ApiClient.create();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    progressDialog.setMessage("Sending OTP...");
                    progressDialog.show();
                    sendOtp(mobile);
                }
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        showOTPBottomSheetDialog();


    }

    private boolean validation() {
        username = textInputLayoutUsername.getEditText().getText().toString();
        email = textInputLayoutEmail.getEditText().getText().toString();
        mobile = textInputLayoutMobile.getEditText().getText().toString();
        password = textInputLayoutPassword.getEditText().getText().toString();
        companyName = textInputLayoutCompany.getEditText().getText().toString();
        if (username.toString().isEmpty()) {
            textInputLayoutUsername.setError("Username can't be empty");
            return false;
        } else if (email.toString().isEmpty()) {
            textInputLayoutEmail.setError("Please enter Email");
            return false;
        } else if (mobile.toString().isEmpty()) {
            textInputLayoutMobile.setError("Please enter Mobile");
            return false;
        } else if (password.toString().isEmpty()) {
            textInputLayoutPassword.setError("Please enter Password");
            return false;
        } else if (companyName.toString().isEmpty()) {
            textInputLayoutCompany.setError("Please enter Company name");
        }


        return true;
    }

    public void sendOtp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.show();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(RegisterActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            submit();
                            // ...
                        } else {
                            Toast.makeText(RegisterActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    progressDialog.dismiss();
                    Log.d("TAG", "onVerificationCompleted:" + credential);
                    Toast.makeText(RegisterActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();

                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w("TAG", "onVerificationFailed", e);
                    progressDialog.dismiss();
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                        Toast.makeText(RegisterActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                        Toast.makeText(RegisterActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    // Show a message and update the UI
                    // ...
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d("TAG", "onCodeSent:" + verificationId);
                    progressDialog.dismiss();
                    mVerificationId = verificationId;
                    mResendToken = token;
                    showOTPBottomSheetDialog();
                    // Save verification ID and resending token so we can use them later


                    // ...
                }
            };

    public void showOTPBottomSheetDialog() {
        bottomSheetDialog =
                new BottomSheetDialog(RegisterActivity.this,
                        R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.mobile_verification_lay);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //  bottomSheetDialog.set(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button buttonSubmit = (Button)
                bottomSheetDialog.findViewById(R.id.submitOTP);
        OtpView otpView = bottomSheetDialog.findViewById(R.id.otp_view);
        ImageView imageView = bottomSheetDialog.findViewById(R.id.closeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpView.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Enter 6 Digit Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
        bottomSheetDialog.show();
        // Save verification ID and resending token so we can use them later

    }


    private void submit() {
        progressDialog.setMessage("Creating Account.. ");
        progressDialog.show();
        service.register(username, email, password, true, false, mobile, companyName)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        Log.d("TAG", "onResponse: Signup" + response.toString());

                        if (response.body() != null) {
                            JsonObject user = response.body().get("user").getAsJsonObject();
                            if (user.get("confirmed").getAsBoolean()) {
                                new SessionManager(RegisterActivity.this).saveUser(response.body());
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sorry! You are not confirmed by the admin. " +
                                        "Contact Administrator to confirm your account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.code() == 400)
                                Toast.makeText(RegisterActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(RegisterActivity.this, "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        AppUtils.handleNoInternetConnection(RegisterActivity.this);
                    }
                });
    }
}