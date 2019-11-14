package com.example.nasande;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nasande.retrofit.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainInstance;
    private EditText debug_num;
    private Button reset_num;
    private static String network = "MobileMoney";
    private static Context appContext;
    SharedPrefManager sharedPrefManager;

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
        mainInstance = this;
        appContext = getApplicationContext();

        reset_num = (Button)findViewById(R.id.reset_num);

        reset_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debug_num = (EditText)findViewById(R.id.debug_num);
                network = debug_num.getText().toString();

                if(network.isEmpty()){
                    network = "MobileMoney";
                }

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
