package com.tommyfrenchbookmakers.officialapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestCorrectScoreActivityFragment extends Fragment {

    private TextView mHomeTextView;
//    private AppCompatSeekBar mHomeSeekBar;
    private SeekbarWithIntervals mHomeSeekBar;
    private TextView mHomeStartScoreTextView;
    private LinearLayout mHomeMiddleScoresLinearLayout;
    private TextView mHomeEndScoreTextView;

    private TextView mAwayTextView;
    private SeekbarWithIntervals mAwaySeekBar;
//    private AppCompatSeekBar mAwaySeekBar;
    private TextView mAwayStartScoreTextView;
    private LinearLayout mAwayMiddleScoresLinearLayout;
    private TextView mAwayEndScoreLinearLayout;


    private AppCompatButton mOddsButton;

    public TestCorrectScoreActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test_correct_score, container, false);

        mOddsButton = (AppCompatButton) v.findViewById(R.id.appcompat_button_confirmOdds);

        mHomeTextView = (TextView) v.findViewById(R.id.text_view_homeScore);
        mAwayTextView = (TextView) v.findViewById(R.id.text_view_awayScore);

        mHomeSeekBar = (SeekbarWithIntervals) v.findViewById(R.id.seekbar_home);
        mHomeSeekBar.setIntervals(getIntervals(9));
        mHomeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHomeTextView.setText("Home: " + progress);
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        mHomeStartScoreTextView = (TextView) v.findViewById(R.id.text_view_homeStartingScore);
//        mHomeStartScoreTextView.setText(homeScores.get(0));
//        mHomeEndScoreTextView = (TextView) v.findViewById(R.id.text_view_awayEndingScore);
//        mHomeEndScoreTextView.setText(homeScores.get(homeScores.size() - 1));

//        mHomeMiddleScoresLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_homeScores);
//        setupMiddleScores(mHomeMiddleScoresLinearLayout, homeScores);

        mAwaySeekBar = (SeekbarWithIntervals) v.findViewById(R.id.seekbar_away);
        mAwaySeekBar.setIntervals(getIntervals(4));
        mAwaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAwayTextView.setText("Away: " + progress);
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return v;
    }

    private List<String> getIntervals(int amount) {
        ArrayList<String> intervals = new ArrayList<>();
        for(int i = 0; i <= amount; i++) {
            intervals.add(i+"");
        }
        return intervals;
    }

//    private void setupMiddleScores(final LinearLayout layout, List<String> list) {
//
//        for(int i = 0; i < list.size(); i++) {
//            String score = list.get(i);
//            TextView textView = new TextView(getActivity());
//            textView.setText(score);
//
//            LinearLayout.LayoutParams params;
//
//            if(i == 0) {
//                int widthOfSeekbarThumb = R.dimen.seekbar_thumb_width;
//                int thumbOffset = widthOfSeekbarThumb / 2;
//
//                textView.setPadding(thumbOffset, 0, 0, 0);
//
//                int widthOfSeekbar = mHomeSeekBar.getWidth();
//                int textViewWidth = textView.getWidth();
//                int remainingWidth = widthOfSeekbar - textViewWidth - widthOfSeekbarThumb;
//
//                int numberOfIntervals = list.size();
//                int maximumWidth = remainingWidth / numberOfIntervals;
//
//                textView.setPadding(thumbOffset, 0, maximumWidth, 0);
//
//                params  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            } else if (i == list.size() - 1) {
//
//
//                textView.setGravity(Gravity.RIGHT);
//                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            } else {
//                params  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.weight = 1;
//            }
//
//            textView.setLayoutParams(params);
//            textView.setGravity(Gravity.CENTER);
//
//            layout.addView(textView);
//        }
//
//        layout.measure(layout.getMeasuredWidth(), layout.getMeasuredHeight());
//        layout.layout(layout.getLeft(), layout.getTop(), layout.getRight(), layout.getBottom());
//    }
}
