package com.solarapp;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.common.Constants;

import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    Button registerButton;
    DatabaseReference userInforef;
    FirebaseAuth mAuth;
    EditText nameEdittext,emailEdittext,addressEdittext,register_phoneNumberEdittext;
    String name,email,address,phoneNumber;
    private android.app.AlertDialog alertDialog ;



    int pos,pos1;

    String[] items = new String[]{"Electrical Dealers","Hardware Dealers","Automobile Dealers","Others"};



    private String mVerificationId;

    Dialog otpDialog;
    EditText customdialogoptview_otpEdittext;
    Button customdialogoptview_cancelButton,customdialogoptview_okButton;

    TextInputLayout nameInputLayout,emailInputLayout,addressInputLayout,phoneNumberInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.register_doneButton);
        nameEdittext = findViewById(R.id.register_NameEdittext);
        emailEdittext = findViewById(R.id.register_emailEdittext);
//        passwordEdittext = findViewById(R.id.register_passwordEdittext);
        addressEdittext= findViewById(R.id.register_addressEdittext);

        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        addressInputLayout = findViewById(R.id.addressInputLayout);
        phoneNumberInputLayout = findViewById(R.id.phoneNumberInputLayout);

        register_phoneNumberEdittext= findViewById(R.id.register_phoneNumberEdittext);

        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);




        userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                String phone_number = (String) dataSnapshot.child("phone_number").getValue();
                String address = (String) dataSnapshot.child("address").getValue();

                addressEdittext.setText(address);
                register_phoneNumberEdittext.setText(phone_number);
                emailEdittext.setText(email);
                nameEdittext.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nameEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    nameInputLayout.setError("Member Name is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Member Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    nameInputLayout.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        register_phoneNumberEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("TAG", String.valueOf(charSequence.toString().length() ));
                if(register_phoneNumberEdittext.getText().toString().length() != 10)
                {
                    phoneNumberInputLayout.setError("Invalid Mobile Number");
//                    Toast.makeText(NewCustomerActivty.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    phoneNumberInputLayout.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isValidEmailId(charSequence.toString())){
                    emailInputLayout.setErrorEnabled(false);
//                    Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                }else{
                    emailInputLayout.setError("InValid Email Address.");
//                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addressEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    addressInputLayout.setError("Address is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addressInputLayout.setErrorEnabled(false);
                }





            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameEdittext.getText().toString().trim();
                String email = emailEdittext.getText().toString().trim();
                String address = addressEdittext.getText().toString().trim();
                String phone_number = register_phoneNumberEdittext.getText().toString().trim();

                if(!isValidEmailId(email)){

                    emailInputLayout.setError("InValid Email Address.");
//                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
//                    Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                }

                if(name.equals(""))
                {
                    nameInputLayout.setError("Name is empty.");
//                    Toast.makeText(EditProfileActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.equals(""))
                {
                    emailInputLayout.setError("Email is empty.");
//                    Toast.makeText(EditProfileActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(address.equals(""))
                {
                    addressInputLayout.setError("Address is empty.");
//                    Toast.makeText(EditProfileActivity.this, "Address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone_number.length() !=10)
                {
                    phoneNumberInputLayout.setError("Invalid Phone number");
//                    Toast.makeText(EditProfileActivity.this, "Phone Number should be 10 digit.", Toast.LENGTH_SHORT).show();
                    return;
                }

                userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name);
                userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email);
                userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue(address);
                userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone_number").setValue(phone_number);

                Toast.makeText(EditProfileActivity.this, "Profile has been updated.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditProfileActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });



    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}