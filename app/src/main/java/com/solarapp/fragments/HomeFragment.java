package com.solarapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.BuildConfig;
import com.solarapp.CustomerProfileActivity;
import com.solarapp.MainActivity;
import com.solarapp.NewCustomerActivty;
import com.solarapp.R;
import com.solarapp.ShareLiveLocationActivity;
import com.solarapp.adapter.CustomSliderAdapter;
import com.solarapp.startup.LoginActivity;
import com.solarapp.utils.PicassoImageLoadingService;

import java.util.ArrayList;
import java.util.List;

import in.balakrishnan.easycam.CameraBundleBuilder;
import in.balakrishnan.easycam.CameraControllerActivity;
import ss.com.bannerslider.Slider;


public class HomeFragment extends Fragment {


    private String[] list;
    private Slider genralVisitImageslider;
    View myView;
    Button addnew_customerButton,cust_profButton,cust_shareLocButton;
    List<String> all_images_url = new ArrayList<>();
    TextView txtMarquee;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView=  inflater.inflate(R.layout.fragment_home, container, false);

        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtMarquee = (TextView) myView.findViewById(R.id.marqueeText);
        txtMarquee.setSelected(true);
        txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtMarquee.setText("Welcome to Technoveent Research and Development");
        txtMarquee.setSelected(true);
        txtMarquee.setSingleLine(true);

        addnew_customerButton = myView.findViewById(R.id.addnew_customerButton);
        cust_shareLocButton = myView.findViewById(R.id.cust_shareLocButton);
        cust_profButton = myView.findViewById(R.id.cust_profButton);
        genralVisitImageslider = (Slider) myView.findViewById(R.id.genral_visit_imageslider);

        Slider.init(new PicassoImageLoadingService(getActivity()));
        setSliderViews();

        cust_shareLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraControllerActivity.class);
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

        addnew_customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewCustomerActivty.class);
                startActivity(intent);
            }
        });


        cust_profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomerProfileActivity.class);
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
                Intent intent = new Intent(getActivity(), ShareLiveLocationActivity.class);
                intent.putExtra("cool",list[0].toString());
                startActivity(intent);

//                Log.e("TAG", list[0].toString());
            }



        }
    }

}