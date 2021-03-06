package com.avocarrot.json2view.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.avocarrot.json2view.DynamicView;
import com.avocarrot.json2view.DynamicViewId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObject jsonObject;

        try {

            jsonObject = new JSONObject(readFile("sample.json", this));

        } catch (JSONException je) {
            je.printStackTrace();
            jsonObject = null;
        }

        if (jsonObject != null) {

            DynamicView.createViewAsync(this, jsonObject, SampleViewHolder.class, new DynamicView.OnJsonParsedAsView() {
                @Override
                public void onBackgroundChanges(View view) {
                    ((SampleViewHolder) view.getTag()).clickableView.setOnClickListener(MainActivity.this);
                    view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                }

                @Override
                public void onSuccess(View view) {
                    setContentView(view);
                }

                @Override
                public void onFailure() {
                    Log.e("Json2View", "View parsing was no possible");
                }
            });

        } else {
            Log.e("Json2View", "Could not load valid json file");
        }

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.avocarrot.com/")));
    }

    /**
     * Helper function to load file from assets
     */
    private String readFile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null) isr.close();
                if (fIn != null) fIn.close();
                if (input != null) input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    /**
     * Holder class that keep UI Component from the Dynamic View
     */
    static public class SampleViewHolder {
        @DynamicViewId(id = "testClick")
        public View clickableView;

        public SampleViewHolder() {
        }
    }
}
