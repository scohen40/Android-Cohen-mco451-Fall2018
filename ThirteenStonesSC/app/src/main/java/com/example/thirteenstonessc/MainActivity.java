package com.example.thirteenstonessc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import static com.example.thirteenstonessc.Utils.showInfoDialog;

public class MainActivity extends AppCompatActivity {
    ThirteenStones mCurrentGame;
    TextView mStatusBar;
    View mSBContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
        setUpFAB();

        mCurrentGame = new ThirteenStones();
        mStatusBar = findViewById(R.id.tv_status_bar);
        mSBContainer = findViewById(R.id.activity_main);

        startFirstGame();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(MainActivity.this, getString(R.string.info_title), mCurrentGame.getRules());
            }
        });
    }

    private void updateStatusBar() {
        String str = "Current Player: " + mCurrentGame.getCurrentOrWinningPlayerNumberOneOrTwo() +
                "; Stones remaining in pile: " + mCurrentGame.getPileCurrent();
        mStatusBar.setText(str);
    }

    private void startFirstGame() {
        mCurrentGame = new ThirteenStones();
        updateStatusBar();
    }

    private void startNextNewGame() {
        mCurrentGame.startGame();
        updateStatusBar();
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
        if (id == R.id.action_new_game) {
            startNextNewGame();
            return true;
        } else if(id == R.id.action_about) {
            showInfoDialog(this, getString(R.string.action_about), getString(R.string.about_text));
        }

        return super.onOptionsItemSelected(item);
    }

    public void pick123(View view) {
        Button buttonClicked = (Button) view;
        try {
            mCurrentGame.takeTurn(Integer.parseInt(buttonClicked.getText().toString()));
            updateStatusBar();
            if(mCurrentGame.isGameOver()) {
                showInfoDialog(this, "Game Over",
                        "The winner is player " + mCurrentGame.getCurrentOrWinningPlayerNumberOneOrTwo());
            }
        } catch (Exception e) {
            Snackbar.make(mSBContainer, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("CURRENT_GAME", ThirteenStones.getJSONof(mCurrentGame));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentGame = ThirteenStones.getGameFromJSON(savedInstanceState.getString("CURRENT_GAME"));
        updateStatusBar();
    }


}
