package com.example.healthbuddy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class GetData extends AsyncTask<String,String,String> {
    ProgressBar pd;
    Context c;
    LinearLayout linearLayout;
    ScrollView scrollView;
    ImageView button;
    String message;
    TextView textView;
    LinearLayout.LayoutParams params;

    public GetData(ProgressBar pd, Context c, LinearLayout linearLayout, ScrollView scrollView, ImageView button, String message) {
        this.pd = pd;
        this.c = c;
        this.message = message;
        this.button = button;
        this.scrollView = scrollView;
        this.linearLayout = linearLayout;
    }

    @Override
    protected void onPreExecute() {
        button.setClickable(false);
        textView = new TextView(c);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.user_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.fontForUserMessage);
        }
        textView.setPadding(20,10,20,10);
        textView.setText(String.format(Locale.getDefault(),"You:- %s",message));
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        pd.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String urlParams = URLEncoder.encode(message,"UTF-8");
            URL url = new URL(Globals.url+"/get?msg="+urlParams);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        button.setClickable(true);
        pd.setVisibility(View.INVISIBLE);
        try {
            textView = new TextView(c);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            textView.setLayoutParams(params);
            textView.setBackgroundResource(R.drawable.bot_message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(R.style.fontForBotMessage);
            }
            textView.setPadding(20,10,20,10);
            textView.setText(String.format(Locale.getDefault(),"Bot:%s",result));
            linearLayout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } catch (Exception e) {
            Toast.makeText(c,e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
