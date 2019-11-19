package com.example.nasande;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasande.retrofit.ApiService;
import com.example.nasande.retrofit.SharedPrefManager;
import com.example.nasande.retrofit.UtilsApi;
import com.example.nasande.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainInstance;
    private EditText debug_num;
    private Button btn_upload;
    private static String network = "MobileMoney";
    private static Context appContext;
    SharedPrefManager sharedPrefManager;
    ApiService mApiService;
    private View mProgressView;

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
        mProgressView = findViewById(R.id.upload_progress);

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fileUpload(Uri uri){
        String basic_auth = sharedPrefManager.getSPBasicAuth();
        String csrf_token = sharedPrefManager.getSPCsrfToken();
        File theFile = FileUtils.getFile(this,uri);
        /*
       RequestBody filePart = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),
               theFile);
               */
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), theFile);
        

       // MultipartBody.Part file = MultipartBody.Part.createFormData("image_field",theFile.getName(),filePart);

        mApiService = UtilsApi.getAPIService();

       Call<ResponseBody> call = mApiService.postFile(basic_auth,csrf_token,file);

       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if (response.isSuccessful()) {
                   mProgressView.setVisibility(View.GONE);
                   try {
                       JSONObject jsonRESULTS = new JSONObject(response.body().string());
                       if (!jsonRESULTS.getString("fid").isEmpty()) {
                           String fid = jsonRESULTS.getString("fid");
                           Toast.makeText(MainActivity.this, "Image id : "+ fid, Toast.LENGTH_SHORT).show();

                       } else {
                           String error_message = jsonRESULTS.getString("error_msg");
                           Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_SHORT).show();
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               } else {
                   mProgressView.setVisibility(View.GONE);
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               Log.e("debug", "onFailure: ERROR > " + t.toString());
               mProgressView.setVisibility(View.GONE);
           }
       });

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
