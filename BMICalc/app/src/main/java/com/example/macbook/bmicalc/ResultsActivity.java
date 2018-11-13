package com.example.macbook.bmicalc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setupToolbar();
        setupFab();
        processDataFromIntent();
    }

    private void processDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        TextView tv_height, tv_weight, tv_BMI, tv_BMI_group;
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_BMI = findViewById(R.id.tv_BMI);
        tv_BMI_group = findViewById(R.id.tv_BMI_group);


        if(extras != null) {
            BMICalcModel bmiCalc = BMICalcModel.getObjectFromJSONString(
                    extras.getString("BMI")
            );

            tv_height.setText(tv_height.getText().toString() + bmiCalc.getHeight());
            tv_weight.setText(tv_weight.getText().toString() + bmiCalc.getWeight());
            tv_BMI.setText(tv_BMI.getText().toString() + bmiCalc.getBMI());
            tv_BMI_group.setText(tv_BMI_group.getText().toString() + bmiCalc.getBMIGroup());
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
