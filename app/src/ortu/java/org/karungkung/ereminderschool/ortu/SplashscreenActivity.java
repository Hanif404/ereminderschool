package org.karungkung.ereminderschool.ortu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashscreenActivity extends AppCompatActivity {

    private SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(this);

        if(sm.getPrefInteger("id") != 0){
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
