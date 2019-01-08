package com.example.hw3recyclerview;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GridGameAdapter mObjGridGameAdapter;
    private int mTurnsTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setUpBoard() {
        int squares = 16;
        int rows = (int) (squares / Math.sqrt(squares));

        RecyclerView objRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        objRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager objLayoutManager = new GridLayoutManager(this, rows);

        mObjGridGameAdapter = new GridGameAdapter(squares);

        objRecyclerView.setLayoutManager(objLayoutManager);
        objRecyclerView.setAdapter(mObjGridGameAdapter);

        mTurnsTaken = 0;
    }

    public void buttonHandler(View view) {
        showGuessResults(view);
        incrementGuessesCounterAndUpdateStatusBar();
    }

    private void showGuessResults(View view) {
        View sbContainer = findViewById(R.id.activity_main);

        Button currentButton = (Button) view;

        String currentText =  currentButton.getText().toString();
        int currentElement = Integer.parseInt(currentText);

        String msg = "You clicked on " + currentButton.getText().toString() + ". ";
        msg += mObjGridGameAdapter.isWinner(currentElement) ?
                "This is the winning number!" : "Please try a different number.";

        Snackbar.make(sbContainer, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void incrementGuessesCounterAndUpdateStatusBar() {
        TextView tvStatusBar = (TextView) findViewById(R.id.status_bar);
        tvStatusBar.setText("Guesses taken: " + ++mTurnsTaken);
    }

    public void newGame(MenuItem item) {
        mObjGridGameAdapter.startNewGame();

        mTurnsTaken = -1;
        incrementGuessesCounterAndUpdateStatusBar();

        View sbContainer = findViewById(R.id.activity_main);
        Snackbar.make(sbContainer, "Welcome to a new game!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("WINNING_NUMBER", mObjGridGameAdapter.getWinningNumber());
        outState.putInt("CURRENT_GUESSES", mTurnsTaken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mObjGridGameAdapter.overwriteWinningNumber(savedInstanceState.getInt("WINNING_NUMBER"));
        mTurnsTaken = savedInstanceState.getInt("CURRENT_GUESSES") - 1;
        incrementGuessesCounterAndUpdateStatusBar();
    }
}
