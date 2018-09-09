package org.karungkung.ereminderschool.guru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.karungkung.ereminderschool.R;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager sm = new SessionManager(this);

        if(sm.getPrefInteger("id_guru") != 0){
            Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(SplashscreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
