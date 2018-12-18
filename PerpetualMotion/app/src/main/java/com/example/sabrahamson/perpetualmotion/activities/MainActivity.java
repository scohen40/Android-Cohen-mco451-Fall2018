package com.example.sabrahamson.perpetualmotion.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.perpetual_motion.pm_game.IDGame;
import com.example.sabrahamson.perpetualmotion.R;
import com.example.sabrahamson.perpetualmotion.lib.CardPilesAdapter;

import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {

    private IDGame mCurrentGame;

    private CardPilesAdapter mAdapter;

    // pref boolean fields
    private boolean mPrefUseAutoSave, mPrefShowErrors;

    // Preference keys
    private final String mKeyPrefsName = "PREFS";
    private String mKeyAutoSave, mKeyShowErrors, mKeyGame, mKeyCheckedPiles;
    private int countOfChecks = 0;

    private void setDefaultValuesForPreferences ()
    {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.main_prefs, true);
    }

    @Override
    protected void onStop() {
        saveToSharedPref();
        super.onStop();
    }

    private void saveToSharedPref() {
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();

        saveSettingsToSharedPrefs (editor);
        saveGameAndBoardToSharedPrefsIfAutoSaveIsOn (editor);

        editor.apply();
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
        editor.putBoolean(mKeyShowErrors, mPrefShowErrors);
        editor.putBoolean(mKeyAutoSave, mPrefUseAutoSave);
    }

    private void saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(SharedPreferences.Editor editor) {
        if (mPrefUseAutoSave)
        {
            final boolean [] CHECKED_PILES = mAdapter.getCheckedPiles();

            //editor.putString(mKeyGame, getJSONof(mCurrentGame));
            for (int i = 0; i < CHECKED_PILES.length; i++) {
                editor.putBoolean(mKeyCheckedPiles + i, CHECKED_PILES[i]);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDefaultValuesForPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFAB();
        setupBoard();
        restoreAppSettingsFromPrefs();
    }

    private void restoreAppSettingsFromPrefs() {
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);

        mPrefUseAutoSave = preferences.getBoolean(mKeyAutoSave, true);
        mPrefShowErrors = preferences.getBoolean(mKeyShowErrors, true);
    }

    private void setupBoard() {
        // create the adapter
        mAdapter = new CardPilesAdapter(getApplicationContext(), R.string.cards_in_stack);

        //Set the listener we made at the bottom as the Adapter's OIClick Listener Object
        mAdapter.setOnItemClickListener(listener);

        // get a reference to the RecyclerView
        RecyclerView rvPiles = findViewById(R.id.rv_piles);

        // set the number of columns based on the currently loaded integers.xml
        final int RV_COLUMN_COUNT = getResources().getInteger(R.integer.rv_columns);

        // Create the layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager
                (this, RV_COLUMN_COUNT);

        rvPiles.setHasFixedSize(true);
        rvPiles.setLayoutManager(layoutManager);
        rvPiles.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_auto_save).setChecked(mPrefUseAutoSave);
        menu.findItem(R.id.action_turn_show_error_messages).setChecked(mPrefShowErrors);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // dismiss the snackbar if shown
        switch (id) {
            //case R.id.home: --in a secondary activity - this handles the back button
            //  onBackPressed();
            case R.id.action_about:
                showAbout();
                return true;
            case R.id.action_toggle_auto_save:
                toggleMenuItem(item);
                mPrefUseAutoSave = item.isChecked();
                return true;
            case R.id.action_turn_show_error_messages:
                toggleMenuItem(item);
                mPrefShowErrors = item.isChecked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dismissSnackBarIfShown() {

    }



    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    private void showAbout() {

    }

    public void turn_action_discard(View view) {
        if(mCurrentGame.isGameOver()) {
            showSB_alreadyGameOver();
        } else {
            dismissSnackBarIfShown();
            final boolean[] checkedPiles = mAdapter.getCheckedPiles();
            attemptDiscard(checkedPiles, getCountOfChecks(checkedPiles));
        }
    }

    private void attemptDiscard(boolean[] checkPiles, int countOfChecks) {
        try {
            discardOneOrTwo(checkPiles, countOfChecks);
            doUpdatesAfterGameStartOrTakingTurn();
        } catch(EmptyStackException ese) {
            showSB(getString(R.string.error_cannot_discard_from_empty_pile));
        } catch(UnsupportedOperationException uoe) {
            showSB(uoe.getMessage());
        }
    }

    private void showSB(String string) {

    }

    private void doUpdatesAfterGameStartOrTakingTurn() {

    }

    private void discardOneOrTwo(boolean[] checkedPiles, int countOfChecks) {
        int pileTopToDiscard, secondPileTopToDiscard;

        switch (countOfChecks) {
            case 1:
                pileTopToDiscard = getCheckedItem(checkedPiles, 0);
                mCurrentGame.discardOneLowestOfSameSuit(pileTopToDiscard);
            case 2:
                pileTopToDiscard = getCheckedItem(checkedPiles, 0);
                secondPileTopToDiscard = getCheckedItem(checkedPiles, 0);

        }
    }

    private int getCountOfChecks(boolean[] checkPiles) {
        return 0;

    }

    private void showSB_alreadyGameOver() {
    }

    public void turn_action_deal(View view) {
    }

    private final CardPilesAdapter.OIClickListener listener = new CardPilesAdapter.OIClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            try {
                if(mCurrentGame.getNumberOfCardsInStackAtPosition(position) > 0) {
                    if(mCurrentGame.isGameOver()) {
                        //show Game Over Snackbar
                    } else {
                        //dismiss Snackbar if shown
                        mAdapter.toggleCheck(position);
                    }
                }
            } catch (Exception e){
                Log.d("STACK_CRASH", e.getMessage());
            }
        }
    };
}
