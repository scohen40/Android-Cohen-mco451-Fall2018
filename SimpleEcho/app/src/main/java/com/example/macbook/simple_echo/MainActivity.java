package com.example.macbook.simple_echo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    public void echoText(View view) {
        final EditText editText = findViewById(R.id.input_field);
        final TextView textView = findViewById(R.id.output);

        String input = String.valueOf(editText.getText());

        textView.setText(input);
    }
}
