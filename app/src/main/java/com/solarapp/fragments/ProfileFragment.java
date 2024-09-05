package com.solarapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.AboutUsActivity;
import com.solarapp.EditProfileActivity;
import com.solarapp.R;
import com.solarapp.common.Constants;
import com.solarapp.startup.LoginActivity;
import com.solarapp.startup.SplashActivity;


public class ProfileFragment extends Fragment {



    View myView;

    TextView username_textview,email_textview;
    LinearLayout aboutus_ll,profile_editll;
    Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_profile, container, false);

        username_textview =  myView.findViewById(R.id.username_textview);
        email_textview =  myView.findViewById(R.id.email_textview);
        aboutus_ll =  myView.findViewById(R.id.aboutus_ll);
        profile_editll =  myView.findViewById(R.id.profile_editll);
        logoutButton =  myView.findViewById(R.id.logoutButton);

        return myView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = (String) snapshot.child("name").getValue();
                String email = (String) snapshot.child("email").getValue();
                username_textview.setText(first_name);
                email_textview.setText(email);
//                Toast.makeText(getActivity(), snapshot.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        aboutus_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        profile_editll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }
}