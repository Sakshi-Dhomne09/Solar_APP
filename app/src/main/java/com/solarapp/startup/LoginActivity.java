package com.solarapp.startup;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.AdminHomeActivity;
import com.solarapp.MainActivity;
import com.solarapp.R;
import com.solarapp.common.Constants;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    EditText emailEdittext;
    //    passwordEdittext
    Button doneButton, registrationButton;
        TextView login_forgotpasswordTextview;
    private android.app.AlertDialog alertDialog;
    private String mVerificationId;

    Dialog otpDialog;
    EditText customdialogoptview_otpEdittext;
    Button customdialogoptview_cancelButton, customdialogoptview_okButton;

    DatabaseReference userInforef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.blue));
        setContentView(R.layout.activity_login);

        //settingStatusBarTransparent();
        mAuth = FirebaseAuth.getInstance();

        emailEdittext = findViewById(R.id.login_emailEdittext);
        login_forgotpasswordTextview = findViewById(R.id.login_forgotpasswordTextview);
//        passwordEdittext = findViewById(R.id.login_passwordEdittext);
//        forgotPasswordTextview = findViewById(R.id.login_forgotpasswordTextview);

        doneButton = findViewById(R.id.login_doneButton);

        registrationButton = findViewById(R.id.login_registrationButton);

        String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.INTERNET", "android.permission.CALL_PHONE", "android.permission.GET_ACCOUNTS", "android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};


        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);
        otpDialog = new Dialog(this);


        int permsRequestCode = 290;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }
        if (!checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, permsRequestCode);
            }
        }

        //AutoStartPermissionHelper.getInstance().getAutoStartPermission(LoginActivity.this);


        alertDialog = new SpotsDialog(LoginActivity.this);

        /*String getemail = null;

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        android.accounts.Account[] accounts = AccountManager.get(LoginActivity.this).getAccounts();
        for (android.accounts.Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                getemail = account.name;
            }
        }
        emailEdittext.setText(getemail);*/
        login_forgotpasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEdittext.getText().toString().trim();
//                String password = passwordEdittext.getText().toString().trim();

                if (!email.isEmpty()) {
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    sendVerificationCode(email);

                    /*mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                alertDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
                            }
                            else if (!task.isSuccessful())
                            {
                                alertDialog.dismiss();
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    Toast.makeText(LoginActivity.this, "Sorry this email does'nt exist.", Toast.LENGTH_SHORT).show();
                                    emailEdittext.requestFocus();
                                    emailEdittext.setText("");

                                }
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    Toast.makeText(LoginActivity.this, "Sorry, Your passord does'nt match with the account.", Toast.LENGTH_SHORT).show();
                                    passwordEdittext.requestFocus();
                                    passwordEdittext.setText("");

                                }
                                catch (Exception e)
                                {
                                    Log.d("TAG", "onComplete: " + e.getMessage());
                                }
                            }
                            else
                            {
                                alertDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                } else {
                    Toast.makeText(LoginActivity.this, "Oops!! Looks like you are missing something.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
          }
/*
        if (mAuth.getCurrentUser() != null) {

            userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String status = (String) dataSnapshot.child("status").getValue();

                    if(status.equals("ok"))
                    {
                        Intent intent = new Intent(LoginActivity.this, AccountNotActivatedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                   else
                    {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
                    }

//                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        Intent intent = new Intent(LoginActivity.this, AccountNotActivatedActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(LoginActivity.this, AccountNotActivatedActivity.class);
//                        startActivity(intent);
////                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                        finish();
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }*/
//        else
//        {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//            overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
//        }

    }

    private void settingStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            alertDialog.dismiss();

            ShowInfoDialog();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                customdialogoptview_otpEdittext.setText(code);
                //verifying the code
                verifyVerificationCode(code);
                otpDialog.cancel();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            alertDialog.dismiss();
            otpDialog.dismiss();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            alertDialog.dismiss();
            ShowInfoDialog();
//            customdialogoptview_otpEdittext.setText("Waiting for OTP");
            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        alertDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            alertDialog.dismiss();


                            //verification successful we will start the profile activity
/*

                                String uid = mAuth.getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("Email", email);

                                map.put("Uid", uid);
                                map.put("timeStamp", ServerValue.TIMESTAMP);
*/

                            Constants.login_type = "user";
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                        else{

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }

                    }
                });


    }

    ;


    private void ShowInfoDialog() {


        otpDialog.setContentView(R.layout.custom_dialog_otpview);
        otpDialog.setCanceledOnTouchOutside(true);

        customdialogoptview_otpEdittext = otpDialog.findViewById(R.id.customdialogoptview_otpEdittext);
        customdialogoptview_cancelButton = otpDialog.findViewById(R.id.customdialogoptview_cancelButton);
        customdialogoptview_okButton = otpDialog.findViewById(R.id.customdialogoptview_okButton);


        customdialogoptview_okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                verifyVerificationCode(customdialogoptview_otpEdittext.getText().toString().trim());
            }
        });

        customdialogoptview_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();
            }
        });

        otpDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        otpDialog.show();
        otpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        //int result6 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result7 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}

