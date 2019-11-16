package com.example.nasande;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nasande.retrofit.ApiService;
import com.example.nasande.retrofit.SharedPrefManager;

import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainInstance;
    private EditText debug_num;
    private Button btn_upload;
    private static String network = "MobileMoney";
    private static Context appContext;
    SharedPrefManager sharedPrefManager;
    ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager = new SharedPrefManager(this);
        if (!sharedPrefManager.getSPIsLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }

        mainInstance = this;
        appContext = getApplicationContext();

        btn_upload = findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),1);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == 1 && resultCode == 1 && data != null && data.getData() != null){
        Uri uri = data.getData();
        fileUpload(uri);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode){
            case 100:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {

                }
            }

        }

    }

    private void fileUpload(Uri uri){
        String basic_auth = sharedPrefManager.getSPBasicAuth();
        String csrf_token = sharedPrefManager.getSPCsrfToken();
       //TODO RequestBody filePart = RequestBody.create()
        // TODO mApiService.postFile(basic_auth,csrf_token,);

    }

    public static MainActivity  getInstace(){
        return mainInstance;
    }

    public static String  getNetwork(){
        return network;
    }

    public static Context  getAppContext(){
        return appContext;
    }

    public void updateViews(final String t) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
               TextView notif = (TextView) findViewById(R.id.debug_text);
               notif.setText(t);
            }
        });
    }
}
