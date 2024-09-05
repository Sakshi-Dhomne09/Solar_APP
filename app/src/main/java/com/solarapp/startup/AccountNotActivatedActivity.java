package com.solarapp.startup;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.solarapp.R;

public class AccountNotActivatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_not_activated);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}