package com.solarapp;

 
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.solarapp.adapter.CustomSliderAdapter;
import com.solarapp.utils.PicassoImageLoadingService;

import java.util.ArrayList;
import java.util.List;

import in.balakrishnan.easycam.CameraBundleBuilder;
import in.balakrishnan.easycam.CameraControllerActivity;
import ss.com.bannerslider.Slider;

public class AdminHomeActivity extends AppCompatActivity {

    private String[] list;
    private Slider genralVisitImageslider;
 
    Button addnew_customerButton,cust_profButton,cust_shareLocButton,cust_empProfileButton,cust_empRequestsButton;
    List<String> all_images_url = new ArrayList<>();
    TextView txtMarquee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        txtMarquee = (TextView) findViewById(R.id.marqueeText);

        txtMarquee.setSelected(true);

        txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtMarquee.setText("Welcome to Technoveent Research and Development");
        txtMarquee.setSelected(true);
        txtMarquee.setSingleLine(true);

        addnew_customerButton = findViewById(R.id.addnew_customerButton);
        cust_shareLocButton = findViewById(R.id.cust_shareLocButton);
        cust_profButton = findViewById(R.id.cust_profButton);
        cust_empProfileButton = findViewById(R.id.cust_empProfileButton);
        cust_empRequestsButton = findViewById(R.id.cust_empRequestsButton);
        genralVisitImageslider = (Slider) findViewById(R.id.genral_visit_imageslider);

        Slider.init(new PicassoImageLoadingService(AdminHomeActivity.this));
        setSliderViews();

        cust_shareLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, CameraControllerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("inputData", new CameraBundleBuilder()
                        .setFullscreenMode(false)
                        .setDoneButtonString("Add")
                        .setSinglePhotoMode(false)
                        .setMax_photo(1)
                        .setManualFocus(true)
                        .setBucketName(getClass().getName())
                        .setPreviewEnableCount(true)
                        .setPreviewIconVisiblity(true)
                        .setPreviewPageRedirection(true)
                        .setEnableDone(false)
                        .setClearBucket(true)
                        .createCameraBundle());
                startActivityForResult(intent, 214);
            }
        });

        cust_empRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminRequestsActivity.class);
                startActivity(intent);
            }
        });

        cust_empProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, EmployeeProfileActivity.class);
                startActivity(intent);
            }
        });

        addnew_customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, NewCustomerActivty.class);
                startActivity(intent);
            }
        });



//
        cust_profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, CustomerProfileActivity.class);
                startActivity(intent);


            }
        });

    }
    private void setSliderViews() {


        Uri banner1 = Uri.parse("android.resource://com.solarapp1/" + R.drawable.banner_1);
        Uri banner2 = Uri.parse("android.resource://com.solarapp1/" + R.drawable.banner_2);
        Uri banner3 = Uri.parse("android.resource://com.solarapp1/" + R.drawable.banner_3);
        Uri banner4 = Uri.parse("android.resource://com.solarapp1/" + R.drawable.banner_4);
//        Uri otherPath = Uri.parse("android.resource://com.segf4ult.test/drawable/icon");

//        String path = path.toString();


//        Uri banner1 = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID +  "/drawable/banner_1.jpg");


//        String banner1 = "drawable://" + R.drawable.banner_1;



        all_images_url.add(String.valueOf(banner1));
        all_images_url.add(String.valueOf(banner2));
        all_images_url.add(String.valueOf(banner3));
        all_images_url.add(String.valueOf(banner4));

        genralVisitImageslider.setAdapter(new CustomSliderAdapter(all_images_url));
        genralVisitImageslider.setInterval(5000);
        genralVisitImageslider.setLoopSlides(true);


       /* imagesRef.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String i = (String) ds.getValue();
                    all_images_url.add(i);
                    genralVisitImageslider.setAdapter(new CustomSliderAdapter(all_images_url));
                    genralVisitImageslider.setInterval(5000);
                    genralVisitImageslider.setLoopSlides(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 214) {

//            assert data != null;
            if (resultCode == -1) {
                assert data != null;
                list = data.getStringArrayExtra("resultData");

//                Uri imageUri = data.getData();
////            imageView.setImageURI(imageUri);
                Intent intent = new Intent(AdminHomeActivity.this, ShareLiveLocationActivity.class);
                intent.putExtra("cool",list[0].toString());
                startActivity(intent);

//                Log.e("TAG", list[0].toString());
            }



        }
    }

}