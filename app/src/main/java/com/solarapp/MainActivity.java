package com.solarapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.common.Constants;
import com.solarapp.fragments.HomeFragment;
import com.solarapp.fragments.ProfileFragment;
import com.solarapp.startup.AccountNotActivatedActivity;
import com.solarapp.startup.SplashActivity;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment3 = new ProfileFragment();

    Fragment active = fragment1;
    FragmentManager fm ;

    DatabaseReference userInforef;

    FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//
//            userInforef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    String status = (String) dataSnapshot.child("status").getValue();
//
//                    if(status.equals("ok"))
//                    {
//                        Intent intent = new Intent(MainActivity.this, AccountNotActivatedActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(R.anim.fade_enter,R.anim.fade_exit);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }


        fm = getSupportFragmentManager();
        bottomNavigation = findViewById(R.id.bottom_navigation);


        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();

        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();


        MenuItem item = bottomNavigation.getMenu().findItem(R.id.navigation_home);
        item.setChecked(true);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;
                    case R.id.navigation_profile:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;


                }
                return false;
            }
        });
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
//        bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);

        ActivityCompat.requestPermissions( this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );



//        Variables.root = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
//        Log.e("TAG1",  Variables.root );
//        Variables.root =  Environment.getExternalStorageDirectory().toString();
//        Log.e("TAG2",  Variables.root );
        if (Environment.isExternalStorageManager()){

// If you don't have access, launch a new activity to show the user the system's dialog
// to allow access to the external storage
        }else{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

}