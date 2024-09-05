package com.solarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.solarapp.custom.imagecompressor.Compressor;
import com.solarapp.custom.imagecompressor.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class NewCustomerActivty extends AppCompatActivity {


    private TextInputEditText mobileNoEdittext;
    private TextInputEditText passwordEdittext;
    private TextInputEditText confirmPasswordEdittext;
    private TextInputEditText membernameEdittext;

    private TextInputEditText emailEdittext;
    private TextInputEditText addressEdittext;
    private Button passportPhotoBrowseButton;
    private ImageView passportPhotoImageview;
    private Button pancardBrowseButton;
    private ImageView pancardImageView;
    private MaterialButton btnLogin;

    Bitmap aadhar_card_bitmap,electricity_bitmap;

    String aadhar_card_imagepath = "",electricity_imagepath="";
    Uri aadhar_card_newUrl,electricity_newUri;
    StorageReference imagestorageRef;


    String first_push_key = "";
    private File aadharcard_actualImage, aadharcard_compressedImage;
    private File electricity_actualImage, electricity_compressedImage;

    TextInputLayout mobileNoTextInputLayout,passwordTextInputLayout,confirmPasswordTextInputLayout,
            nameTextInputLayout,emailTextInputLayout,addressTextInputLayout;

    private static final int REQUEST_WRITE_PERMISSION = 1111;

    List<String> strings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_activty);

        mobileNoTextInputLayout = findViewById(R.id.mobileNoTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);

        mobileNoEdittext = (TextInputEditText) findViewById(R.id.mobileNoEdittext);
        passwordEdittext = (TextInputEditText) findViewById(R.id.passwordEdittext);
        confirmPasswordEdittext = (TextInputEditText) findViewById(R.id.confirmPasswordEdittext);
        membernameEdittext = (TextInputEditText) findViewById(R.id.membernameEdittext);

        emailEdittext = (TextInputEditText) findViewById(R.id.emailEdittext);
        addressEdittext = (TextInputEditText) findViewById(R.id.addressEdittext);
        
        passportPhotoBrowseButton = (Button) findViewById(R.id.passportPhotoBrowseButton);
        passportPhotoImageview = (ImageView) findViewById(R.id.passportPhotoImageview);
        pancardBrowseButton = (Button) findViewById(R.id.pancardBrowseButton);
        pancardImageView = (ImageView) findViewById(R.id.pancardImageView);
        btnLogin = (MaterialButton) findViewById(R.id.btnLogin);

        imagestorageRef = FirebaseStorage.getInstance().getReference("ALL_IMAGES_STORAGE");

        passportPhotoBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 12345);
            }
        });

        pancardBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 12346);
            }
        });

        emailEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isValidEmailId(charSequence.toString())){
                    emailTextInputLayout.setErrorEnabled(false);
//                    Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                }else{
                    emailTextInputLayout.setError("InValid Email Address.");
//                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        membernameEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    nameTextInputLayout.setError("Member Name is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Member Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    nameTextInputLayout.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobileNoEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("TAG", String.valueOf(charSequence.toString().length() ));
                if(mobileNoEdittext.getText().toString().length() != 10)
                {
                    mobileNoTextInputLayout.setError("Invalid Mobile Number");
//                    Toast.makeText(NewCustomerActivty.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    mobileNoTextInputLayout.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        passwordEdittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().length() != 10)
//                {
//                    passwordTextInputLayout.setError("Invalid Password");
////                    Toast.makeText(NewCustomerActivty.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        confirmPasswordEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!confirmPasswordEdittext.getText().toString().equals(passwordEdittext.getText().toString()))
                {
                    confirmPasswordTextInputLayout.setError("Password and Confirm Password does'nt match");
//                    Toast.makeText(NewCustomerActivty.this, "Password and Confirm Password does'nt match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    confirmPasswordTextInputLayout.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addressEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty())
                {
                    addressTextInputLayout.setError("Address is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addressTextInputLayout.setErrorEnabled(false);
                }





            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_number = mobileNoEdittext.getText().toString().trim();
                String password = passwordEdittext.getText().toString().trim();
                String confirm_password = confirmPasswordEdittext.getText().toString().trim();
                String member_name = membernameEdittext.getText().toString().trim();

                String email = emailEdittext.getText().toString().trim();
                String address = addressEdittext.getText().toString().trim();


                if(mobile_number.isEmpty())
                {
                    mobileNoTextInputLayout.setError("Mobile Number is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mobile_number.length() != 10)
                {
                    mobileNoTextInputLayout.setError("Invalid Mobile Number");
//                    Toast.makeText(NewCustomerActivty.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(password.isEmpty())
                {
                    passwordTextInputLayout.setError("Password is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(confirm_password.isEmpty())
                {
                    confirmPasswordTextInputLayout.setError("Confirm Password is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Confirm Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(email.isEmpty())
                {
                    emailTextInputLayout.setError("Email is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Email is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isValidEmailId(email)){

//                    Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                }else{
                    emailTextInputLayout.setError("InValid Email Address.");
//                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(address.isEmpty())
                {
                    addressTextInputLayout.setError("Address is empty");
//                    Toast.makeText(NewCustomerActivty.this, "Address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }






                if(!password.equals(confirm_password))
                {
                    passwordTextInputLayout.setError("Password and Confirm Password does'nt match");
//                    Toast.makeText(NewCustomerActivty.this, "Password and Confirm Password does'nt match", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(aadhar_card_imagepath.equals(""))
                {
                    Toast.makeText(NewCustomerActivty.this, "Please choose Aadhar Card", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(electricity_imagepath.equals(""))
                {
                    Toast.makeText(NewCustomerActivty.this, "Please choose Electricity Bill", Toast.LENGTH_SHORT).show();
                    return;
                }



                UploadImage();

            }
        });

    }

    private void UploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(NewCustomerActivty.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StorageReference storageReference = imagestorageRef.child("images" + UUID.randomUUID().toString());
        storageReference.putFile(aadhar_card_newUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers_Data");

                        first_push_key = ref.push().getKey();
                        String mobile_number = mobileNoEdittext.getText().toString().trim();
                        String password = passwordEdittext.getText().toString().trim();
                        String confirm_password = confirmPasswordEdittext.getText().toString().trim();
                        String member_name = membernameEdittext.getText().toString().trim();

                        String email = emailEdittext.getText().toString().trim();
                        String address = addressEdittext.getText().toString().trim();

                        Map<String,String> map = new HashMap<>();
                        map.put("mobile_number",mobile_number);
                        map.put("password",password);

                        map.put("confirm_password",confirm_password);
                        map.put("member_name",member_name);
                        map.put("phone_no","phone_no");

                        map.put("aadhar_card",downloadUri.toString());
                        map.put("email",email);
                        map.put("address",address);
                        map.put("user_id",first_push_key);

                        map.put("employee_key",FirebaseAuth.getInstance().getCurrentUser().getUid());



                        ref.child(first_push_key).setValue(map);

                        Toast.makeText(NewCustomerActivty.this, "New Customer has been created", Toast.LENGTH_SHORT).show();


                        UploadImage2();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NewCustomerActivty.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });



    }

    private void UploadImage2() {

        final ProgressDialog progressDialog = new ProgressDialog(NewCustomerActivty.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StorageReference storageReference = imagestorageRef.child("images" + UUID.randomUUID().toString());
        storageReference.putFile(electricity_newUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers_Data");
//                        first_push_key = ref.push().getKey();
                        ref.child(first_push_key).child("electricity_bill").setValue(downloadUri.toString());
                        Toast.makeText(NewCustomerActivty.this, "New Customer has been created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewCustomerActivty.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NewCustomerActivty.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });

    }


    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345 && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();

            passportPhotoBrowseButton.setText("Image Selected");

            aadhar_card_imagepath = getPath(pickedImage);
            Picasso.get().load(pickedImage).into(passportPhotoImageview);
            try {
                aadhar_card_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pickedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String imagePath;
            imagePath = aadhar_card_imagepath;
            Uri uri = Uri.parse(ConvertToUri2(imagePath));

            try {
                aadharcard_actualImage = FileUtil.from(NewCustomerActivty.this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                aadharcard_compressedImage = new Compressor(NewCustomerActivty.this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(30)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .compressToFile(aadharcard_actualImage);

            } catch (IOException e) {
                e.printStackTrace();

            }
            Uri uri1 = Uri.fromFile(aadharcard_compressedImage);

            aadhar_card_newUrl = uri1;



        } else if (requestCode == 12346 && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            pancardBrowseButton.setText("Image Selected");

            electricity_imagepath = getPath(pickedImage);
            Picasso.get().load(pickedImage).into(pancardImageView);
            try {
                electricity_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pickedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String imagePath;
            imagePath = electricity_imagepath;
            Uri uri = Uri.parse(ConvertToUri2(imagePath));

            try {
                electricity_actualImage = FileUtil.from(NewCustomerActivty.this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                electricity_compressedImage = new Compressor(NewCustomerActivty.this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(30)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .compressToFile(electricity_actualImage);

            } catch (IOException e) {
                e.printStackTrace();

            }
            Uri uri1 = Uri.fromFile(electricity_compressedImage);

            electricity_newUri = uri1;



        } 
       

    

        

    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

        }
    }
    private String ConvertToUri2(String path) {
        File f = new File(path);
        Uri yourUri = Uri.fromFile(f);
        return yourUri.toString();

    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}