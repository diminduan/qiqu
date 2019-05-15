package com.example.duand.qiqu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.duand.qiqu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowRouteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        Intent intent = super.getIntent();
        String result = (String) intent.getSerializableExtra("result");
        try {
            JSONObject jsonObject = new JSONObject(result);
            Log.e("check", "onCreate: "+result );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
