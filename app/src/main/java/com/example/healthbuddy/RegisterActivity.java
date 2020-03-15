package com.example.healthbuddy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText name, phone, address, dob, height, weight, disorder;
    String user_name, user_phone, user_address, user_dob, user_gender, user_height, user_weight, user_bg, user_disorder;
    TextView textView;
    RadioGroup gender;
    AlertDialog.Builder builder;
    String[] bgs = {"O+","A+","B+","AB+","AB-","A-","B-","O-"};
    Spinner spinner;
    SpinnerAdapter adapter;
    ProgressDialog progressDialog;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Register Yourself");

        progressDialog = new ProgressDialog(RegisterActivity.this);
        isRegistered();
            //startActivity(new Intent(RegisterActivity.this,HealthLogActivity.class));

        initiateViews();

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ((EditText) findViewById(R.id.userDateOfBirth)).setText(String.format(Locale.getDefault(),"%02d-%02d-%d",dayOfMonth,month+1,year));

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        ((EditText) findViewById(R.id.userDateOfBirth)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        ((Button) findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields())
                    registerUser();
            }
        });
    }

    private void initiateViews() {
        name = (EditText) findViewById(R.id.userName);
        phone = (EditText) findViewById(R.id.userPhone);
        address = (EditText) findViewById(R.id.userAddress);
        dob = (EditText) findViewById(R.id.userDateOfBirth);
        height = (EditText) findViewById(R.id.userHeight);
        weight = (EditText) findViewById(R.id.userWeight);
        disorder = (EditText) findViewById(R.id.userDisorder);
        textView = (TextView) findViewById(R.id.userEmail);
        gender = (RadioGroup) findViewById(R.id.userGender);
        textView.setText(String.format(Locale.getDefault(),"Email ID: %s",Globals.email));
        spinner = (Spinner) findViewById(R.id.userBloodGroup);
        adapter = new SpinnerAdapter();
        spinner.setAdapter(adapter);
        builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Error").setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        database = FirebaseDatabase.getInstance().getReference().child(Globals.email.split("@")[0].replaceAll("\\.","-")+"_registration");
    }

    private boolean validateFields() {
        user_name = name.getText().toString().trim();
        user_phone = phone.getText().toString().trim();
        user_address = address.getText().toString().trim();
        user_dob = dob.getText().toString().trim();
        int id = gender.getCheckedRadioButtonId();
        user_gender = id == -1 ? "" : ((RadioButton) findViewById(id)).getText().toString();
        user_height = height.getText().toString().trim();
        user_weight = weight.getText().toString().trim();
        user_bg = spinner.getSelectedItem().toString();
        user_disorder = disorder.getText().toString().trim().equals("") ? "Not Specified" : disorder.getText().toString().trim();

        //Name
        if(user_name.equals("")) {
            builder.setMessage("Name is mandatory").show();
            return false;
        }
        if(!Pattern.matches("^[A-Z ]+$",user_name)) {
            builder.setMessage("Name must contain Only BLOCK Letters and Spaces").show();
            return false;
        }
        //Phone
        if(user_phone.equals("")) {
            builder.setMessage("Contact Number is mandatory").show();
            return false;
        }
        if(!Pattern.matches("^[1-9]\\d{9}",user_phone)) {
            builder.setMessage("Contact Number must contain exact 10 digits").show();
            return false;
        }
        //Address
        if(user_address.equals("")) {
            builder.setMessage("Address is mandatory").show();
            return false;
        }
        //DateOfBirth
        if(user_dob.equals("")) {
            builder.setMessage("Date of birth is mandatory").show();
            return false;
        }
        //Gender
        if(user_gender.equals("")) {
            builder.setMessage("Gender is mandatory").show();
            return false;
        }
        //Height
        if(user_height.equals("")) {
            builder.setMessage("Height is mandatory").show();
            return false;
        }
        if(!Pattern.matches("^\\d{1,3}",user_height)) {
            builder.setMessage("Height must contain 1 to 3 digits").show();
            return false;
        }
        //Weight
        if(user_weight.equals("")) {
            builder.setMessage("Weight is mandatory").show();
            return false;
        }
        if(!Pattern.matches("^\\d{1,3}",user_weight)) {
            builder.setMessage("Weight must contain 1 to 3 digits").show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        builder.setMessage(String.format(Locale.getDefault(),"\nEmail ID: %s\n\nName: %s\n\nContact Number: %s\n\nAddress: %s\n\n" +
                "Date of birth: %s\n\nGender: %s\n\nHeight: %s CM\n\nWeight: %s KG\n\nBlood Group: %s\n\nAny Disorder(s): %s\n",
                Globals.email,user_name,user_phone,user_address,user_dob,user_gender,user_height,user_weight,user_bg,user_disorder))
        .setCancelable(false)
                .setTitle("Verify your Details")
        .setPositiveButton("CONFIRM & REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Globals.register(database,RegisterActivity.this,Globals.email,user_name,user_phone,user_address,user_dob,
                        user_gender,user_height,user_weight,user_bg,user_disorder);
            }
        }).setNegativeButton("DENY & EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                builder.setTitle("Error");
            }
        });
        builder.show();
    }

    private void isRegistered() {
        progressDialog.setMessage("Checking Registration Info, Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = getSharedPreferences("HEALTH_BUDDY",MODE_PRIVATE);
        if(preferences.getBoolean("REGISTERED",false)) {
            progressDialog.hide();
            startActivity(new Intent(RegisterActivity.this, HealthLogActivity.class));
        }

        FirebaseDatabase.getInstance().getReference().child(Globals.email.split("@")[0].replaceAll("\\.","-")+"_registration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.hide();
                if(dataSnapshot.getValue() != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("HEALTH_BUDDY",MODE_PRIVATE).edit();
                    editor.putBoolean("REGISTERED",true);
                    editor.apply();
                    startActivity(new Intent(RegisterActivity.this, HealthLogActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bgs.length;
        }

        @Override
        public Object getItem(int position) {
            return bgs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = ((LayoutInflater) RegisterActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.blood_group,parent,false);
            ((TextView) view.findViewById(R.id.bg)).setText(bgs[position]);
            return view;
        }
    }

    boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {
        if (!isUserClickedBackButton) {
            Toast.makeText(RegisterActivity.this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        } else
            finishAffinity();
        HealthLogActivity.class.getDeclaredMethods();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                isUserClickedBackButton = false;
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,Menu.FIRST,0,"Sign Out");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                signOut();
                break;
        }
        return true;
    }

    private void signOut() {
        stopService(new Intent(RegisterActivity.this, HealthBuddy.class));
        Globals.mAuth.signOut();
        Globals.mGoogleSignInClient.signOut().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences.Editor editor = getSharedPreferences("HEALTH_BUDDY",MODE_PRIVATE).edit();
                        editor.putBoolean("REGISTERED",false);
                        editor.apply();
                        startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                    }
                });
    }
}
