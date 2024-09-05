package com.solarapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.common.Constants;
import com.solarapp.model.All_Employee_Model;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestsActivity extends AppCompatActivity {


    RecyclerView usersRecyclerview;
    private DatabaseReference usersRef;


    String mainKw = "";


    private AdminRequestsActivity.SearchCategoriesAdapter searchCategoriesAdapter;
    List<All_Employee_Model> users_models;

//    private EditText searchEdittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);
        usersRecyclerview = findViewById(R.id.users_Recyclerview);
//        searchEdittext = findViewById(R.id.search_searchEdittext);

//        searchEdittext.addTextChangedListener(mTextEditorWatcher);
        usersRef = FirebaseDatabase.getInstance().getReference("User_Information");
        usersRef.keepSynced(true);

        usersRecyclerview.setFocusable(false);
        usersRecyclerview.setNestedScrollingEnabled(false);

        final LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(AdminRequestsActivity.this, LinearLayoutManager.VERTICAL, false);
        usersRecyclerview.setLayoutManager(layoutManager);

//        usersRecyclerview.addItemDecoration(new DividerItemDecoration(AdminAllUsers_Actvity.this, null));


        usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users_models = new ArrayList<>();

                for(DataSnapshot ds :dataSnapshot.getChildren())
                {



                    users_models.add(ds.getValue(All_Employee_Model.class));


                    for (int i = 0 ; i<=users_models.size()-1;i++)
                    {
                        if(users_models.get(i).getStatus().equals("yes"))
                        {
//                            Log.e("TAG", "time to remove value" );
                            users_models.remove(i);
//                        searchCategoriesAdapter.notifyDataSetChanged();
                        }
//                        else
//                        {
//                            if(!users_models.get(i).getEmployee_key().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                            {
////                                Log.e("TAG", "time to remove value" );
//                                users_models.remove(i);
////                        searchCategoriesAdapter.notifyDataSetChanged();
//                            }
//                        }
                    }


                }



                searchCategoriesAdapter  = new AdminRequestsActivity.SearchCategoriesAdapter(users_models,AdminRequestsActivity.this);
                usersRecyclerview.setAdapter(searchCategoriesAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        usersRecyclerview.post(new Runnable()
//        {
//            @Override
//            public void run() {
//                searchCategoriesAdapter.notifyDataSetChanged();
//            }
//        });




    }
//    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Search(searchEdittext.getText().toString().trim());
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    };

    private void Search(String l)
    {
        List<All_Employee_Model> list = new ArrayList<>();
        for (All_Employee_Model search_model :users_models)
        {
            if(search_model.getName().toLowerCase().contains(l.toLowerCase()))
            {
                list.add(search_model);
            }
        }
        searchCategoriesAdapter  = new AdminRequestsActivity.SearchCategoriesAdapter(list,AdminRequestsActivity.this);
        usersRecyclerview.setAdapter(searchCategoriesAdapter);

    }




    class SearchCategoriesAdapter extends RecyclerView.Adapter<AdminRequestsActivity.SearchCategoriesAdapter.ViewHolder> {


        List<All_Employee_Model> strings  = new ArrayList<>();
        Context context;


        public SearchCategoriesAdapter(List<All_Employee_Model> strings, Context context) {
            this.strings = strings;
            this.context = context;
        }

        @NonNull
        @Override
        public AdminRequestsActivity.SearchCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.driverrequest_layout_item,viewGroup,false);
            return new AdminRequestsActivity.SearchCategoriesAdapter.ViewHolder(view);
        }
//
//        @Override
//        public long getItemId(int position) {
//            return super.getItemId(position);
//
//        }

        @Override
        public void onBindViewHolder(@NonNull AdminRequestsActivity.SearchCategoriesAdapter.ViewHolder viewHolder, int i) {




            viewHolder.driverrequestItemDrivernameTextview.setText("Name: "+strings.get(i).getName().toUpperCase());

            viewHolder.driverrequestItemAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User_Information").child(strings.get(i).getUid());

                    usersRef.keepSynced(true);

                    usersRef.child("status").setValue("yes");
                    Toast.makeText(context, "Employee account has been activated", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            });
//
//            viewHolder.emailtextview.setText("Email: "+strings.get(i).getEmail());
//
//            viewHolder.phoneNumbertextview.setText("Phone No: "+strings.get(i).getPhone_number());
//
//            viewHolder.addressTextview.setText("Address: "+strings.get(i).getAddress());
//
//
//
//
//
//
//
//
//
//            viewHolder.aadharCardButton.setVisibility(View.GONE);
//
//
//            viewHolder.electricityBillButton.setVisibility(View.GONE);
//
//
//            viewHolder.saveunitsButton.setVisibility(View.GONE);
//
//            viewHolder.invoice_button.setVisibility(View.GONE);
//            viewHolder.suggestedTextview.setVisibility(View.GONE);




        }


        @Override
        public int getItemCount() {
            return strings.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

          /*  public TextView nameTextview;
            public TextView emailtextview;
            public TextView phoneNumbertextview;
            public TextView suggestedTextview;
            public TextView addressTextview;
            public Button aadharCardButton;
            public Button electricityBillButton;
            public Button saveunitsButton;
            public Button invoice_button;
*/

            public TextView driverrequestItemDrivernameTextview;
            public Button driverrequestItemDeclineButton;
            public Button driverrequestItemAcceptButton;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);


               /* nameTextview = (TextView) itemView.findViewById(R.id.nameTextview);
                suggestedTextview = (TextView) itemView.findViewById(R.id.suggestedTextview);
                emailtextview = (TextView) itemView.findViewById(R.id.emailtextview);
                phoneNumbertextview = (TextView) itemView.findViewById(R.id.phoneNumbertextview);
                addressTextview = (TextView) itemView.findViewById(R.id.addressTextview);
                aadharCardButton = (Button) itemView.findViewById(R.id.aadhar_card_Button);
                electricityBillButton = (Button) itemView.findViewById(R.id.electricity_bill_button);
                saveunitsButton = (Button) itemView.findViewById(R.id.saveunits_Button);
                invoice_button = (Button) itemView.findViewById(R.id.invoice_button);*/

                driverrequestItemDrivernameTextview = (TextView) itemView.findViewById(R.id.driverrequest_item_drivername_textview);
                driverrequestItemDeclineButton = (Button) itemView.findViewById(R.id.driverrequest_item_declineButton);
                driverrequestItemAcceptButton = (Button) itemView.findViewById(R.id.driverrequest_item_acceptButton);




            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(NewCustomerActivty.this, "New Customer has been created", Toast.LENGTH_SHORT).show();

        if(Constants.login_type.equals("admin")) {
            Intent intent = new Intent(AdminRequestsActivity.this,AdminHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(AdminRequestsActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}