package com.solarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewUnitsActivity extends AppCompatActivity {


    private EditText janEdittext;
    private EditText febEdittext;
    private EditText marchEdittext;
    private EditText aprilEdittext;
    private EditText mayEdittext;
    private EditText juneEdittext;
    private EditText julyEdittext;
    private EditText augustEdittext;
    private EditText septEdittext;
    private EditText octEdittext;
    private EditText novEdittext;
    private EditText decEdittext;
//    private EditText ans1_edittext;
    private EditText ans3_edittext;
    private MaterialButton btnLogin;

    DatabaseReference ref ;
    String user_id = "";
    String ans2 = "";
    String ans1 = "";



    LinearLayout testll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_units);

        user_id = getIntent().getStringExtra("user_id");

//        ans1_edittext = (EditText) findViewById(R.id.ans1_edittext);
        ans3_edittext = (EditText) findViewById(R.id.ans3_edittext);

        janEdittext = (EditText) findViewById(R.id.jan_edittext);
        febEdittext = (EditText) findViewById(R.id.feb_edittext);
        marchEdittext = (EditText) findViewById(R.id.march_edittext);
        aprilEdittext = (EditText) findViewById(R.id.april_edittext);
        mayEdittext = (EditText) findViewById(R.id.may_edittext);
        juneEdittext = (EditText) findViewById(R.id.june_edittext);
        julyEdittext = (EditText) findViewById(R.id.july_edittext);
        augustEdittext = (EditText) findViewById(R.id.august_edittext);
        septEdittext = (EditText) findViewById(R.id.sept_edittext);
        octEdittext = (EditText) findViewById(R.id.oct_edittext);
        novEdittext = (EditText) findViewById(R.id.nov_edittext);
        decEdittext = (EditText) findViewById(R.id.dec_edittext);
        btnLogin = (MaterialButton) findViewById(R.id.btnLogin);
        testll =  findViewById(R.id.testll);

        ref = FirebaseDatabase.getInstance().getReference("Customer_unit_data");


        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup rg1 = (RadioGroup) findViewById(R.id.radioGroup2);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.q2yesbutton:
                        ans2 = "YES";
                        // do operations specific to this selection
                        break;
                    case R.id.q2nobutton:
                        ans2 = "NO";
                        // do operations specific to this selection
                        break;

                }
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.q2yesbutton2:
                        ans1 = "YES";
                        testll.setVisibility(View.VISIBLE);
                        // do operations specific to this selection
                        break;
                    case R.id.q2nobutton2:
                        ans1 = "NO";
                        testll.setVisibility(View.GONE);
                        // do operations specific to this selection
                        break;

                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jan = janEdittext.getText().toString().trim();
                String feb = febEdittext.getText().toString().trim();
                String mar = marchEdittext.getText().toString().trim();
                String apr = aprilEdittext.getText().toString().trim();
                String may = mayEdittext.getText().toString().trim();
                String jun = juneEdittext.getText().toString().trim();
                String july = julyEdittext.getText().toString().trim();
                String aug = augustEdittext.getText().toString().trim();
                String sept = septEdittext.getText().toString().trim();
                String oct = octEdittext.getText().toString().trim();
                String nov = novEdittext.getText().toString().trim();
                String dec = decEdittext.getText().toString().trim();
//                String ans1 = ans1_edittext.getText().toString().trim();
                String ans3 = ans3_edittext.getText().toString().trim();



                if(ans1.isEmpty())
                {
                    Toast.makeText(NewUnitsActivity.this, "Please fill the answer 1 ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ans1.equals("NO"))
                {
                    Toast.makeText(NewUnitsActivity.this, "Nothing to Save", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(ans3.isEmpty())
                    {
                        Toast.makeText(NewUnitsActivity.this, "Please fill the answer 3 ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(ans2.isEmpty())
                    {
                        Toast.makeText(NewUnitsActivity.this, "Please fill the answer 2 ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(
                            !jan.isEmpty() || !feb.isEmpty() || !mar.isEmpty() || !apr.isEmpty() || !may.isEmpty() || !jun.isEmpty() ||
                                    !july.isEmpty() || !aug.isEmpty() || !sept.isEmpty() || !oct.isEmpty() || !nov.isEmpty() || !dec.isEmpty()
                    )
                    {
                        Map<String,String> map  = new HashMap<>();
                        map.put("jan",jan);
                        map.put("feb",feb);
                        map.put("mar",mar);
                        map.put("apr",apr);
                        map.put("may",may);
                        map.put("jun",jun);
                        map.put("july",july);
                        map.put("aug",aug);
                        map.put("sept",sept);
                        map.put("oct",oct);
                        map.put("nov",nov);
                        map.put("dec",dec);

                        map.put("ans1",ans1);
                        map.put("ans3",ans3);
                        map.put("ans2",ans2);

                        Float cool =
                                Float.valueOf(jan)
                                        +Float.valueOf(feb)
                                        +Float.valueOf(mar)
                                        +Float.valueOf(apr)
                                        +Float.valueOf(may)
                                        +Float.valueOf(jun)
                                        +Float.valueOf(july)
                                        +Float.valueOf(aug)
                                        +Float.valueOf(sept)
                                        +Float.valueOf(oct)
                                        +Float.valueOf(nov)
                                        +Float.valueOf(dec);


                        map.put("total",String.valueOf(cool));
                        ref.child(user_id).setValue(map);
                        Toast.makeText(NewUnitsActivity.this, "Units Saved", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(NewCustomerActivty.this, "New Customer has been created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewUnitsActivity.this,CustomerProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(NewUnitsActivity.this, "Please fill all unit fields.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }
}