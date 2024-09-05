package com.solarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.common.Constants;
import com.solarapp.model.All_Users_Model;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerProfileActivity extends AppCompatActivity {




    RecyclerView usersRecyclerview;
    private DatabaseReference usersRef;


    String mainKw = "";


    private SearchCategoriesAdapter searchCategoriesAdapter;
    List<All_Users_Model> users_models;

    private EditText searchEdittext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        usersRecyclerview = findViewById(R.id.users_Recyclerview);
        searchEdittext = findViewById(R.id.search_searchEdittext);

        searchEdittext.addTextChangedListener(mTextEditorWatcher);
        usersRef = FirebaseDatabase.getInstance().getReference("Customers_Data");
        usersRef.keepSynced(true);

        usersRecyclerview.setFocusable(false);
        usersRecyclerview.setNestedScrollingEnabled(false);

        final LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(CustomerProfileActivity.this, LinearLayoutManager.VERTICAL, false);
        usersRecyclerview.setLayoutManager(layoutManager);

//        usersRecyclerview.addItemDecoration(new DividerItemDecoration(AdminAllUsers_Actvity.this, null));


        if(Constants.login_type.equals("admin"))
        {
            usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users_models = new ArrayList<>();

                    for(DataSnapshot ds :dataSnapshot.getChildren())
                    {
                        users_models.add(ds.getValue(All_Users_Model.class));
                    }

                    searchCategoriesAdapter  = new SearchCategoriesAdapter(users_models,CustomerProfileActivity.this);
                    usersRecyclerview.setAdapter(searchCategoriesAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users_models = new ArrayList<>();

                    for(DataSnapshot ds :dataSnapshot.getChildren())
                    {



                        users_models.add(ds.getValue(All_Users_Model.class));


                        for (int i = 0 ; i<=users_models.size()-1;i++)
                        {
                            if(users_models.get(i).getEmployee_key()== null)
                            {
//                            Log.e("TAG", "time to remove value" );
                                users_models.remove(i);
//                        searchCategoriesAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                if(!users_models.get(i).getEmployee_key().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {
//                                Log.e("TAG", "time to remove value" );
                                    users_models.remove(i);
//                        searchCategoriesAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }



                    searchCategoriesAdapter  = new SearchCategoriesAdapter(users_models,CustomerProfileActivity.this);
                    usersRecyclerview.setAdapter(searchCategoriesAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

//        usersRecyclerview.post(new Runnable()
//        {
//            @Override
//            public void run() {
//                searchCategoriesAdapter.notifyDataSetChanged();
//            }
//        });




    }
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Search(searchEdittext.getText().toString().trim());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void Search(String l)
    {
        List<All_Users_Model> list = new ArrayList<>();
        for (All_Users_Model search_model :users_models)
        {
            if(search_model.getMember_name().toLowerCase().contains(l.toLowerCase()))
            {
                list.add(search_model);
            }
        }
        searchCategoriesAdapter  = new SearchCategoriesAdapter(list,CustomerProfileActivity.this);
        usersRecyclerview.setAdapter(searchCategoriesAdapter);

    }




    class SearchCategoriesAdapter extends RecyclerView.Adapter<SearchCategoriesAdapter.ViewHolder> {


        List<All_Users_Model> strings  = new ArrayList<>();
        Context context;


        public SearchCategoriesAdapter(List<All_Users_Model> strings, Context context) {
            this.strings = strings;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item_layout,viewGroup,false);
            return new ViewHolder(view);
        }

//        @Override
//        public long getItemId(int position) {
//            return super.getItemId(position);
//
//        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {



            viewHolder.nameTextview.setText("Name: "+strings.get(i).getMember_name().toUpperCase());

            viewHolder.emailtextview.setText("Email: "+strings.get(i).getEmail());

            viewHolder.phoneNumbertextview.setText("Phone No: "+strings.get(i).getMobile_number());

            viewHolder.addressTextview.setText("Address: "+strings.get(i).getAddress());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer_unit_data");




            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                    Log.e("TAG", strings.get(i).getUser_id());

                    if(dataSnapshot.hasChild(strings.get(i).getUser_id()))
                    {

                        String total = (String) dataSnapshot.child(strings.get(i).getUser_id()).child("total").getValue();
                        if (Float.valueOf(total)<= 2880)
                        {
                            viewHolder.suggestedTextview.setText("Suggested Plant : 2KW ");
                            mainKw = "2";
                        }
                        else if (Float.valueOf(total)<= 4320)
                        {
                            viewHolder.suggestedTextview.setText("Suggested Plant : 3 KW ");
                            mainKw = "3";
                        }

                        else if (Float.valueOf(total)<= 5760)
                        {
                            viewHolder.suggestedTextview.setText("Suggested Plant : 4 KW ");
                            mainKw = "4";
                        }

                        else if (Float.valueOf(total)<= 7200)
                        {
                            viewHolder.suggestedTextview.setText("Suggested Plant : 5 KW ");
                            mainKw = "5";
                        }

                        else if (Float.valueOf(total)<= 8640)
                        {
                            viewHolder.suggestedTextview.setText("Suggested Plant : 6 KW ");
                            mainKw = "6";
                        }
                    }
                    else
                    {

                        viewHolder.suggestedTextview.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            viewHolder.aadharCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CustomerProfileActivity.this, ImageZoomActivity.class);
                    intent.putExtra("image",strings.get(i).getAadhar_card());
                    startActivity(intent);

                }
            });


            viewHolder.electricityBillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerProfileActivity.this, ImageZoomActivity.class);
                    intent.putExtra("image",strings.get(i).getElectricity_bill());
                    startActivity(intent);
                }
            });


            viewHolder.saveunitsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerProfileActivity.this, NewUnitsActivity.class);
                    intent.putExtra("user_id",strings.get(i).getUser_id());
                    startActivity(intent);
                }
            });

            viewHolder.invoice_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerProfileActivity.this, InvoiceActivity.class);
                    intent.putExtra("user_id",strings.get(i).getUser_id());
                    intent.putExtra("mainKw",mainKw);
                    intent.putExtra("user_name",strings.get(i).getMember_name());
                    intent.putExtra("user_suggested_panel",viewHolder.suggestedTextview.getText().toString());
                    startActivity(intent);

                }
            });




        }


        @Override
        public int getItemCount() {
            return strings.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView nameTextview;
            public TextView emailtextview;
            public TextView phoneNumbertextview;
            public TextView suggestedTextview;
            public TextView addressTextview;
            public Button aadharCardButton;
            public Button electricityBillButton;
            public Button saveunitsButton;
            public Button invoice_button;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                nameTextview = (TextView) itemView.findViewById(R.id.nameTextview);
                suggestedTextview = (TextView) itemView.findViewById(R.id.suggestedTextview);
                emailtextview = (TextView) itemView.findViewById(R.id.emailtextview);
                phoneNumbertextview = (TextView) itemView.findViewById(R.id.phoneNumbertextview);
                addressTextview = (TextView) itemView.findViewById(R.id.addressTextview);
                aadharCardButton = (Button) itemView.findViewById(R.id.aadhar_card_Button);
                electricityBillButton = (Button) itemView.findViewById(R.id.electricity_bill_button);
                saveunitsButton = (Button) itemView.findViewById(R.id.saveunits_Button);
                invoice_button = (Button) itemView.findViewById(R.id.invoice_button);



            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(NewCustomerActivty.this, "New Customer has been created", Toast.LENGTH_SHORT).show();

        if(Constants.login_type.equals("admin")) {
            Intent intent = new Intent(CustomerProfileActivity.this,AdminHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(CustomerProfileActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}