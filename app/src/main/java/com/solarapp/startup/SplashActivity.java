package com.solarapp.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.InvoiceActivity;
import com.solarapp.MainActivity;
import com.solarapp.R;
import com.solarapp.common.Constants;

public class SplashActivity extends AppCompatActivity {


    DatabaseReference userInforef;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {

            userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String status = (String) dataSnapshot.child("status").getValue();

                    if(status.equals("ok"))
                    {
                        Intent intent = new Intent(SplashActivity.this, AccountNotActivatedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

//                Intent intent = new Intent(SplashActivity.this, InvoiceActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();

                }
            }, 1000);
        }


       /* new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

//                Intent intent = new Intent(SplashActivity.this, InvoiceActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();

            }
        }, 1000);*/
    }
}