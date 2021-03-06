package org.karungkung.ereminderschool.guru;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hanif on 13/08/18.
 */

public class SessionManager {
    private Context context;

    public SessionManager(Context context){
        this.context = context;
    }

    public void setPrefString(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("SessionGuard", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPrefString(String key) {
        SharedPreferences prefs = context.getSharedPreferences("SessionGuard", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void setPrefInteger(String key, Integer value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("SessionGuard", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getPrefInteger(String key) {
        SharedPreferences prefs = context.getSharedPreferences("SessionGuard", Context.MODE_PRIVATE);
        Integer position = prefs.getInt(key, 0);
        return position;
    }

    public void clearPreferences(){
        SharedPreferences prefs = context.getSharedPreferences("SessionGuard", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
