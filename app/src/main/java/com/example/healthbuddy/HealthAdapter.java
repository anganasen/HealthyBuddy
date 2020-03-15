package com.example.healthbuddy;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.MyViewHolder> {

    private ArrayList<Integer> pulses;
    private ArrayList<Date> times;
    private ArrayList<Integer> temperatures;
    private ArrayList<String> ecgs;
    private ArrayList<Integer> steps;
    private ArrayList<Integer> caloriesBurnts;
    private Context context;

    public HealthAdapter(ArrayList<Integer> pulses, ArrayList<Date> times, ArrayList<Integer> temperatures, ArrayList<String> ecgs, ArrayList<Integer> steps, ArrayList<Integer> caloriesBurnts, Context context) {
        this.pulses = pulses;
        this.times = times;
        this.temperatures = temperatures;
        this.ecgs = ecgs;
        this.steps = steps;
        this.caloriesBurnts = caloriesBurnts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.health_record,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.date.setText(Html.fromHtml("<i><b>Time:</b>&nbsp;&nbsp;"+times.get(i).toString().replace("GMT+05:30 2020","Hrs").replace("GMT+05:30 2019","Hrs")+"</i>"));
        viewHolder.pulse.setText(Html.fromHtml("<b><i>PPG:</i></b><br>"+pulses.get(i)+" BPM"));
//        viewHolder.temperature.setText(Html.fromHtml("<b><i>Temperature:</i></b><br>"+temperatures.get(i)+" &#176;C"));
        viewHolder.temperature.setText(Html.fromHtml("<b><i>Temperature:</i></b><br>NDA"));
        Glide.with(context).load(ecgs.get(i)).error(R.drawable.error).into(viewHolder.ecg);
//        viewHolder.steps.setText(Html.fromHtml("<b><i>Steps:</i></b><br>"+steps.get(i)));
        viewHolder.steps.setText(Html.fromHtml("<b><i>Steps:</i></b><br>NDA"));
//        viewHolder.calories.setText(Html.fromHtml("<b><i>Calories Burnt:</i></b><br>"+caloriesBurnts.get(i)+" kCal"));
    }

    @Override
    public int getItemCount() {
        return pulses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pulse, date, temperature, steps; //, calories;
        ImageView ecg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pulse = (TextView) itemView.findViewById(R.id.pulseValue);
            date = (TextView) itemView.findViewById(R.id.dateValue);
            temperature = (TextView) itemView.findViewById(R.id.temperatureValue);
            ecg = (ImageView) itemView.findViewById(R.id.ecgValue);
            steps = (TextView) itemView.findViewById(R.id.stepsValue);
//            calories = (TextView) itemView.findViewById(R.id.caloriesBurntValue);
        }
    }
}
