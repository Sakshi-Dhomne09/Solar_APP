package com.solarapp.startup;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.solarapp.AdminHomeActivity;
import com.solarapp.MainActivity;
import com.solarapp.R;
import com.solarapp.common.Constants;

public class AdminLoginActivity extends AppCompatActivity {


    private ImageView loginImageview;
    private TextView nouseiuygfvbnjkhg;
    private EditText loginEmailEdittext;
    private EditText loginPasswordEdittext;
    private Button loginDoneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


//        loginImageview = (ImageView) findViewById(R.id.login_imageview);
//        nouseiuygfvbnjkhg = (TextView) findViewById(R.id.nouseiuygfvbnjkhg);
        loginEmailEdittext = (EditText) findViewById(R.id.login_emailEdittext);
        loginPasswordEdittext = (EditText) findViewById(R.id.login_passwordEdittext);
        loginDoneButton = (Button) findViewById(R.id.login_doneButton);

        loginDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmailEdittext.getText().toString().trim();
                String password = loginPasswordEdittext.getText().toString().trim();

                if(email.equals("admin@gmail.com"))
                {
                    if(password.equals("123456"))
                    {
                        Constants.login_type = "admin";
                        Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(AdminLoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}