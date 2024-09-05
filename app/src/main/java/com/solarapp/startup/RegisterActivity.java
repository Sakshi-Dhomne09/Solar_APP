package com.solarapp.startup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.solarapp.R;
import com.solarapp.common.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import jrizani.jrspinner.JRSpinner;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

public class RegisterActivity extends AppCompatActivity {


    Button registerButton;
    DatabaseReference userInforef;
    FirebaseAuth mAuth;
    EditText nameEdittext,emailEdittext,addressEdittext,register_phoneNumberEdittext;
    String name,email,address,phoneNumber;
    private android.app.AlertDialog alertDialog ;



    int pos,pos1;

    String[] items = new String[]{"Electrical Dealers","Hardware Dealers","Automobile Dealers","Others"};

  /*  String[] electrical_sub = new String[]{"Fans","Switches","Wires","Lighting","Motors","Pumps","Industrial Items","Solar Applications","Special Electrical Items","Others"};

    String[] hardware_sub = new String[]{"Paints","Tiles & Marbles","Other Tools","Bath Fitting","Others"};
*/



    private String mVerificationId;

    Dialog otpDialog;
    EditText customdialogoptview_otpEdittext;
    Button customdialogoptview_cancelButton,customdialogoptview_okButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        final Window window =  getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.blue));
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.register_doneButton);
        nameEdittext = findViewById(R.id.register_NameEdittext);
        emailEdittext = findViewById(R.id.register_emailEdittext);
//        passwordEdittext = findViewById(R.id.register_passwordEdittext);
        addressEdittext= findViewById(R.id.register_addressEdittext);

        register_phoneNumberEdittext= findViewById(R.id.register_phoneNumberEdittext);







//        spinner2 = findViewById(R.id.resgiter_JRSpinner1);
        String getemail = null;


        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        android.accounts.Account[] accounts = AccountManager.get(RegisterActivity.this).getAccounts();
        for (android.accounts.Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                getemail = account.name;
            }
        }

        otpDialog = new Dialog(this);






       /* spinner2.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pos1= position;
            }
        });
*/
//        spinner2.setItems(hardware_sub);
//        spinner2.setExpandTint(R.color.jrspinner_color_default);

        emailEdittext.setText(getemail);

        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);

        alertDialog = new SpotsDialog(RegisterActivity.this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameEdittext.getText().toString().trim();
                email = emailEdittext.getText().toString().trim();
//                password = passwordEdittext.getText().toString().trim();
                address = addressEdittext.getText().toString().trim();
                phoneNumber = register_phoneNumberEdittext.getText().toString().trim();
                if(!name.isEmpty() && !email.isEmpty()  && !address.isEmpty() && !phoneNumber.isEmpty())
                {
                    alertDialog.show();
                    sendVerificationCode(phoneNumber);


                }
                else
                {
                    new MaDialog.Builder(RegisterActivity.this)
                            .setTitle("Error")
                            .setMessage("Oops!!! Looks like you are missing something.")
                            .setPositiveButtonText("ok")
                            .setButtonOrientation(LinearLayout.HORIZONTAL)
                            .setPositiveButtonListener(new MaDialogListener() {
                                @Override
                                public void onClick() {
                                }
                            })
                            .build();
                    //Toast.makeText(RegisterActivity.this, "Oops!!! Looks like you are missing something.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
    }


    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
//        customdialogoptview_otpEdittext.setText("");
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();


            if (code != null) {
                alertDialog.dismiss();
                ShowInfoDialog();
                customdialogoptview_otpEdittext.setText(code);
                //verifying the code
                verifyVerificationCode(code);
                otpDialog.cancel();
            }
            else
            {
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//                //signing the user
//                signInWithPhoneAuthCredential(credential);

                Toast.makeText(RegisterActivity.this, "Hurray, You got auto verified by us.", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

//            customdialogoptview_otpEdittext.setHint("Waiting for otp");
            //storing the verification id that is sent to the user
            Log.e("ANDHOMNE", "onCodeSent: s - " + s.toString() + " : t - " + forceResendingToken);

            alertDialog.dismiss();
            ShowInfoDialog();
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String formattedDate = df.format(c.getTime());

                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Map<String,String> map = new HashMap<>();
                            map.put("name",name);
                            map.put("email",email);
//                            map.put("password",password);
                            map.put("phone_number",phoneNumber);
                            map.put("address",address);
                            map.put("status","ok");
                            map.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            map.put("prime_status","no");
                            map.put("prime_trial_end_days","NA");
                            map.put("category",items[pos]);
//                                                map.put("sub_category",items[pos1]);
                            userInforef.child(mAuth.getCurrentUser().getUid()).setValue(map);
                            alertDialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,AccountNotActivatedActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }


    private void ShowInfoDialog() {

        otpDialog.setContentView(R.layout.custom_dialog_otpview);
        otpDialog.setCanceledOnTouchOutside(true);

        customdialogoptview_otpEdittext = otpDialog.findViewById(R.id.customdialogoptview_otpEdittext);
        customdialogoptview_cancelButton = otpDialog.findViewById(R.id.customdialogoptview_cancelButton);
        customdialogoptview_okButton = otpDialog.findViewById(R.id.customdialogoptview_okButton);

        customdialogoptview_okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



    private void settingStatusBarTransparent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
    }

}


