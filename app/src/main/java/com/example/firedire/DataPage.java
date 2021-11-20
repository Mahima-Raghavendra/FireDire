package com.example.firedire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<DataList> dataList;
    private RecyclerAdapter adapter;
    private EditText dataSetsNumber;
    private Button getData, generatePDF;
    private int given_count;
    private String datasets="";
    /*Bitmap bmp, scaledbmp;
    int pageWidth=1200;*/
    long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        dataSetsNumber = findViewById(R.id.dataSetsNumber);
        getData = findViewById(R.id.getData);
        generatePDF = findViewById(R.id.generatePDF);
        //bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pdf_image);
        //scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        datasets = dataSetsNumber.getText().toString().trim();
        if(datasets.equals("")){
            given_count = 0;
        }
        else{
            given_count = Integer.parseInt(datasets);
        }
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new RecyclerAdapter(dataList);
        recyclerView.setAdapter(adapter);

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataList list = dataSnapshot.getValue(DataList.class);
                                dataList.add(list);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        /*ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        createPDF();*/
            }
        });
    }
    /*private void createPDF() {
        generatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument document = new PdfDocument();
                final Paint paint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                final Canvas canvas = page.getCanvas();
                canvas.drawBitmap(scaledbmp,0,0, paint);
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(70);
                canvas.drawText("Temperature Data",pageWidth/2, 270, titlePaint);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                canvas.drawRect(20,780,pageWidth-20,860,paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("Date", 40, 830, paint);
                canvas.drawText("Time", 500, 830, paint);
                canvas.drawText("Temperature", 800, 830, paint);

                canvas.drawLine(480, 790, 480, 850, paint);
                canvas.drawLine(780, 790, 780, 850, paint);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int y = 950;
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                y+=80;
                                canvas.drawText((String) dataSnapshot.child("date").getValue(), 40, y, paint);
                                canvas.drawText((String) dataSnapshot.child("time").getValue(), 500, y, paint);
                                canvas.drawText((String) dataSnapshot.child("temperature").getValue(), 800, y, paint);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                document.finishPage(page);

                File file = new File(Environment.getExternalStorageDirectory(),"/Hello.pdf");
                try{
                    document.writeTo(new FileOutputStream(file));
                }catch(IOException e){
                    e.printStackTrace();
                }
                document.close();
            }
        });
    }*/

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(DataPage.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logoutMenu) {
            Logout();
        }

        return super.onOptionsItemSelected(item);
    }
}
