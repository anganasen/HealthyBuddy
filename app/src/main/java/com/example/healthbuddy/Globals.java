package com.example.healthbuddy;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Globals {
    static String email, url = "https://healthbuddy20.herokuapp.com";
    static NotificationChannel channel;
    static final int NOTIFICATION_ID = 100, BACKGROUND_ID = 200;
    static Context context;
    static FirebaseAuth mAuth;
    static GoogleSignInClient mGoogleSignInClient;

    static void register(DatabaseReference myRef, final Context c, String... strings){
        try {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("email", strings[0]);
            childUpdates.put("name", strings[1]);
            childUpdates.put("phone", strings[2]);
            childUpdates.put("address", strings[3]);
            childUpdates.put("dateOfBirth", strings[4]);
            childUpdates.put("gender", strings[5]);
            childUpdates.put("height", strings[6]);
            childUpdates.put("weight", strings[7]);
            childUpdates.put("bloodGroup", strings[8]);
            childUpdates.put("disorder", strings[9]);
            myRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError != null)
                        Toast.makeText(c, databaseError.toString(),Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(c, "Registered Successfully !!!", Toast.LENGTH_SHORT).show();
                        c.startActivity(new Intent(c, HealthLogActivity.class));
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(c, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
