package com.tommyfrenchbookmakers.officialapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;

import java.util.List;

/**
 * Created by Tíghearnán on 16/11/2015.
 */
public class SeekbarWithIntervals extends LinearLayout {

    private RelativeLayout mRelativeLayout;
    private AppCompatSeekBar mSeekBar;

    private int mWidthMeasureSpec = 0;
    private int mHeightMeasureSpec = 0;

    public SeekbarWithIntervals(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed) {
            alignIntervals();

            mRelativeLayout.measure(mWidthMeasureSpec, mHeightMeasureSpec);
            mRelativeLayout.layout(mRelativeLayout.getLeft(), mRelativeLayout.getTop(),
                    mRelativeLayout.getRight(), mRelativeLayout.getBottom());
        }
    }

    private void alignIntervals() {
        int widthOfSeekbarThumb = getSeekbarThumbWidth();
        int thumbOffSet = widthOfSeekbarThumb / 2;
        alignFirstInterval(thumbOffSet);

        int widthOfSeekbar = getSeekBar().getWidth();
        int firstIntervalWidth = getRelativeLayout().getChildAt(0).getWidth();
        int remainingPaddableWidth = widthOfSeekbar - firstIntervalWidth - widthOfSeekbarThumb;

        int numberOfIntervals = getSeekBar().getMax();
        int maximumWidthOfEachInterval = remainingPaddableWidth / numberOfIntervals;

        alignIntervalsInBetween(maximumWidthOfEachInterval);
        alignLastInterval(thumbOffSet, maximumWidthOfEachInterval);
    }

    private int getSeekbarThumbWidth() {
        return getResources().getDimensionPixelOffset(R.dimen.seekbar_thumb_width);
    }

    private void alignFirstInterval(int offset) {
        TextView firstInterval = (TextView) getRelativeLayout().getChildAt(0);
        firstInterval.setPadding(offset + (firstInterval.getWidth() / 2), 0, 0, 0);
    }

    private void alignIntervalsInBetween(int maximumWidthOfEachInterval) {
        int widthOfPreviousIntervalsText = 0;

        for(int i = 1; i < (getRelativeLayout().getChildCount() - 1); i++) {
            TextView textViewInterval = (TextView) getRelativeLayout().getChildAt(i);
            int widthOfText = textViewInterval.getWidth();

            int leftPadding = Math.round(maximumWidthOfEachInterval - (widthOfText / 2) - (widthOfPreviousIntervalsText / 2));
            if(i == 1) textViewInterval.setPadding(leftPadding - widthOfText, 0, 0, 0);
            else textViewInterval.setPadding(leftPadding, 0, 0, 0);
            widthOfPreviousIntervalsText = widthOfText;
        }
    }

    private void alignLastInterval(int offset, int maximumWidthOfEachInterval) {
        int lastIndex = getRelativeLayout().getChildCount() - 1;

        TextView lastInterval = (TextView) getRelativeLayout().getChildAt(lastIndex);
        int widthOfText = lastInterval.getWidth();

        int leftPadding = Math.round(maximumWidthOfEachInterval - offset);
        lastInterval.setPadding(leftPadding + widthOfText / 2, 0, 0, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        getActivity().getLayoutInflater().inflate(R.layout.seekbar_with_intervals, this);
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    private RelativeLayout getRelativeLayout() {
        if(mRelativeLayout == null) mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_intervals);
        return mRelativeLayout;
    }

    public AppCompatSeekBar getSeekBar() {
        if(mSeekBar == null) mSeekBar = (AppCompatSeekBar) findViewById(R.id.appcompat_seekbar_score);
        return mSeekBar;
    }

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;

        if(getRelativeLayout().getChildCount() == 0) {
            for(String interval : intervals) {
                TextView textView = createInterval(interval, idOfPreviousInterval);
                alignTextViewToRightOfPreviousInterval(textView, idOfPreviousInterval);

                idOfPreviousInterval = textView.getId();
                getRelativeLayout().addView(textView);
            }
        }
    }

    public void setIntervals(List<String> intervals){
        displayIntervals(intervals);
        getSeekBar().setMax(intervals.size() - 1);
    }

    private TextView createInterval(String interval, int id) {
        View textBoxView = LayoutInflater.from(getContext())
                .inflate(R.layout.seekbar_with_intervals_labels, null);

        TextView textView = (TextView) textBoxView.findViewById(R.id.text_view_interval);
        textView.setId(id + 1);
        textView.setText(interval);

        return textView;
    }

    private void alignTextViewToRightOfPreviousInterval(TextView textView, int id) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(id > 0) {
            params.addRule(RelativeLayout.RIGHT_OF, id);
        }

        textView.setLayoutParams(params);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        getSeekBar().setOnSeekBarChangeListener(onSeekBarChangeListener);
    }
}
