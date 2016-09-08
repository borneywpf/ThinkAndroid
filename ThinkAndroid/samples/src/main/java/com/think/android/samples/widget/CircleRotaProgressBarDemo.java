package com.think.android.samples.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.think.android.samples.R;
import com.think.android.widget.CircleRotaProgressBar;

/**
 * Created by borney on 9/8/16.
 */
public class CircleRotaProgressBarDemo extends AppCompatActivity {
    private TextView mTextView;
    private CircleRotaProgressBar mCircleRotaProgressBar1;
    private CircleRotaProgressBar mCircleRotaProgressBar2;
    private CircleRotaProgressBar mCircleRotaProgressBar3;
    private SeekBar mSeekBar;
    private ToggleButton mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circlerotaprogressbar_demo);
        mTextView = (TextView) findViewById(R.id.textView);
        mCircleRotaProgressBar1 = (CircleRotaProgressBar) findViewById(R.id.circlerotaprogressbar1);
        mCircleRotaProgressBar2 = (CircleRotaProgressBar) findViewById(R.id.circlerotaprogressbar2);
        mCircleRotaProgressBar3 = (CircleRotaProgressBar) findViewById(R.id.circlerotaprogressbar3);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mButton = (ToggleButton) findViewById(R.id.button);
        mTextView.setText(String.valueOf(mSeekBar.getProgress()));
        mCircleRotaProgressBar1.setProgress(mSeekBar.getProgress());
        mCircleRotaProgressBar2.setProgress(mSeekBar.getProgress());
        mCircleRotaProgressBar3.setProgress(mSeekBar.getProgress());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextView.setText(String.valueOf(progress));
                mCircleRotaProgressBar1.setProgress(progress);
                mCircleRotaProgressBar2.setProgress(progress);
                mCircleRotaProgressBar3.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mButton.setText(isChecked ? getText(R.string.pause) : getText(R.string.start));
                mCircleRotaProgressBar1.setState(isChecked ? CircleRotaProgressBar.State.START : CircleRotaProgressBar.State.PAUSE);
                mCircleRotaProgressBar2.setState(isChecked ? CircleRotaProgressBar.State.START : CircleRotaProgressBar.State.PAUSE);
                mCircleRotaProgressBar3.setState(isChecked ? CircleRotaProgressBar.State.START : CircleRotaProgressBar.State.PAUSE);
            }
        });
    }
}
