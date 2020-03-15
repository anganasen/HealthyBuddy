package com.example.healthbuddy;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ChatBot extends AppCompatActivity {

    ProgressBar progressDialog;
    EditText message;
    ImageView button;
    ScrollView scrollView;
    LinearLayout linearLayout;
    String m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressDialog = (ProgressBar) findViewById(R.id.typing);
        getSupportActionBar().setTitle("Chat with Health Buddy");
        button = (ImageView) findViewById(R.id.send);
        linearLayout = (LinearLayout) findViewById(R.id.senderArea);
        scrollView = (ScrollView) findViewById(R.id.messageHolder);
        message = (EditText) findViewById(R.id.userMessage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m =  message.getText().toString();
                if(!m.equals("")) {
                    message.setText("");
                    try {
                        new GetData(progressDialog, ChatBot.this, linearLayout, scrollView, button, m).execute();
                    } catch (Exception e) {
                        Toast.makeText(ChatBot.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
