package com.solarapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solarapp.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoiceActivity extends AppCompatActivity {

    private Bitmap bitmap;
    RelativeLayout llScroll;
    String user_id;
    String user_name;
    String mainKw;
    String user_suggested_panel;
    DatabaseReference userInforef;
    TextView nameTextview;
    TextView solarWattTextview;
    TextView solarWattNumTextview;
    TextView solarWattNum2Textview;
    TextView solarWattNum3Textview;
    TextView solarWattNumPriceTextview;
    ScrollView scrollView;
    ImageView shareImgeview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        user_id = getIntent().getStringExtra("user_id");
        mainKw = getIntent().getStringExtra("mainKw");
        user_name = getIntent().getStringExtra("user_name");
        user_suggested_panel = getIntent().getStringExtra("user_suggested_panel");
        userInforef = FirebaseDatabase.getInstance().getReference(Constants.USER_INFORMATION);
        nameTextview = findViewById(R.id.nameTextview);
        solarWattTextview = findViewById(R.id.solarWattTextview);
        solarWattNumTextview = findViewById(R.id.solarWattNumTextview);
        solarWattNum2Textview = findViewById(R.id.solarWattNum2Textview);
        solarWattNum3Textview = findViewById(R.id.solarWattNum3Textview);
        solarWattNumPriceTextview = findViewById(R.id.solarWattNumPriceTextview);
        shareImgeview = findViewById(R.id.shareImgeview);
        nameTextview.setText(user_name);

        solarWattNumTextview.setText("0"+mainKw+" KW");
        solarWattNum2Textview.setText("0"+mainKw+" KW");
        solarWattNum3Textview.setText("0"+mainKw+" KW");
        Integer val = Integer.valueOf(mainKw) * 58000;
        solarWattNumPriceTextview.setText("Rs. "+String.valueOf(val)+" /-");
        solarWattTextview.setText("0"+mainKw+" KW");

        /*userInforef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                String phone_number = (String) dataSnapshot.child("phone_number").getValue();
                String address = (String) dataSnapshot.child("address").getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        llScroll = findViewById(R.id.llScroll);
        scrollView= (ScrollView)findViewById(R.id.getScroll);

//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
////                bitmap = loadBitmapFromView(v, v.getWidth(), v.getHeight() );
//                bitmap = loadBitmapFromView();
//                createPdf();
//                Toast.makeText(InvoiceActivity.this, "Done", Toast.LENGTH_SHORT).show();
//            }
//        }, 2000);

        shareImgeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImgeview.setVisibility(View.INVISIBLE);
                bitmap = loadBitmapFromView();
                createPdf();
                Toast.makeText(InvoiceActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Bitmap loadBitmapFromView() {
//        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.draw(c);

       /* Bitmap bitmap = Bitmap.createBitmap(
                scrollView.getChildAt(0).getWidth(),
                scrollView.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);*/

        Bitmap bitmap = Bitmap.createBitmap(
                scrollView.getChildAt(0).getWidth(),
                scrollView.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);


        Log.e("TAG", String.valueOf( scrollView.getChildAt(0).getWidth()) );
        Log.e("TAG", String.valueOf( scrollView.getChildAt(0).getHeight()) );
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        scrollView.getChildAt(0).draw(c);

        return bitmap;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;



        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 2).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String targetPdf = path+"/newreciept.pdf";

        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//        File file = new File("/sdcard/invoice.pdf");
        File file = new File(path+"/newreciept.pdf");

        if (file.exists())
        {
            /*Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File("/sdcard/cashreceipt.pdf");

            String myFilePath = "/sdcard/cashreceipt.pdf";
            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }*/
            shareImgeview.setVisibility(View.VISIBLE);
            File fileq = new File(path+"/newreciept.pdf");
            Uri uri = Uri.fromFile(fileq);

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.whatsapp");

            startActivity(share);
            /*Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(Admin_Pdf_ReciptView_Activity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }*/
        }
    }
}