package com.example.macbook.bmicalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    //field - starts with m (for member)...
    private BMICalcModel mCalc;
    EditText mEtHeight, mEtWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpFab();

        //initialize our BMI field object
        mCalc = new BMICalcModel();

        //Get a reference to the EditText objects containing the use input
        mEtHeight = findViewById(R.id.et_height);
        mEtWeight = findViewById(R.id.et_weight);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strHeight, strWeight;
                double height, weight;

                //steps:

                //Get the values from the EditTexts
                //Get the text from those EditTexts
                strHeight = mEtHeight.getText().toString();
                strWeight = mEtWeight.getText().toString();

                try {
                    height = Double.parseDouble(strHeight);
                    weight = Double.parseDouble(strWeight);

                    //Put those values into the mCalc object
                    mCalc.setHeight(height);
                    mCalc.setWeight(weight);

                    //Calculate and show the user the BMI for those number
                    Snackbar.make(view, "BMI is: " + mCalc.getBMI(), Snackbar.LENGTH_LONG)
                            .setAction("More Details...", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //create an intent object which will point to results activity
                                    Intent intent = new Intent(getApplicationContext(),
                                            ResultsActivity.class);
                                    //serialize the BMI object and send it
                                    String currentBMIObject = BMICalcModel.getJSONStringFromObject(mCalc);
                                    intent.putExtra("BMI", currentBMIObject);
                                    //send out the data
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                catch (IllegalArgumentException iae) {
                    Snackbar.make(view, "Error! Height and weight must be greater than 0"
                            , Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
