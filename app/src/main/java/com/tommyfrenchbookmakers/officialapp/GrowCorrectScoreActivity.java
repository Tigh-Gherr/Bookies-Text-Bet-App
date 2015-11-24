package com.tommyfrenchbookmakers.officialapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class GrowCorrectScoreActivity extends AppCompatActivity {

    private TextView mHomeTextView;
    private DiscreteSeekBar mHomeSeekBar;

    private TextView mAwayTextView;
    private DiscreteSeekBar mAwaySeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grow_correct_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mHomeTextView = (TextView) findViewById(R.id.text_view_homeScore);
        mHomeTextView.setText("Home: 0");

        mHomeSeekBar = (DiscreteSeekBar) findViewById(R.id.seekbar_home);
        mHomeSeekBar.setMax(9);
        mHomeSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mHomeTextView.setText("Home: " + value);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                // Testing the comment.
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        mAwayTextView = (TextView) findViewById(R.id.text_view_awayScore);
        mAwayTextView.setText("Away: 0");

        mAwaySeekBar = (DiscreteSeekBar) findViewById(R.id.seekbar_away);
        mAwaySeekBar.setMax(4);
        mAwaySeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mAwayTextView.setText("Away: " + value);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

    }

}
