package com.tommyfrenchbookmakers.officialapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.tighearnan.frenchsscanner.R;

import java.util.List;

public class SeekbarWithIntervalsLL extends LinearLayout {
    private LinearLayout mLinearLayout;
    private AppCompatSeekBar mSeekBar;

    private int mWidthMeasureSpec = 0;
    private int mHeightMeasureSpec = 0;

    public SeekbarWithIntervalsLL(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        getActivity().getLayoutInflater().inflate(R.layout.seekbar_with_intervals, this);
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {

            // We've changed the intervals layout, we need to refresh.
            mLinearLayout.measure(mWidthMeasureSpec, mHeightMeasureSpec);
            mLinearLayout.layout(mLinearLayout.getLeft(), mLinearLayout.getTop(),
                    mLinearLayout.getRight(), mLinearLayout.getBottom());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getProgress() {
        return getSeekBar().getProgress();
    }

    public void setProgress(int progress) {
        getSeekBar().setProgress(progress);
    }

    public void setIntervals(List<String> intervals) {
        displayIntervals(intervals);
        getSeekBar().setMax(intervals.size() - 1);
    }

    private void displayIntervals(List<String> intervals) {
        if (getLinearLayout().getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                param.weight = 1;

                getLinearLayout().addView(textViewInterval, param);
            }
        }
    }

    private TextView createInterval(String interval) {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.seekbar_with_intervals_labels, null);

        TextView textView = (TextView) layout.findViewById(R.id.text_view_interval);
        textView.setText(interval);

        return textView;
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        getSeekBar().setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private LinearLayout getLinearLayout() {
        if (mLinearLayout == null) {
            mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_intervals);
        }

        return mLinearLayout;
    }

    private SeekBar getSeekBar() {
        if (mSeekBar == null) {
            mSeekBar = (AppCompatSeekBar) findViewById(R.id.seekbar);
        }

        return mSeekBar;
    }
}