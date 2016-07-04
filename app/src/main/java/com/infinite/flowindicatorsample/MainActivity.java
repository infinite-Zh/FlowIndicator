package com.infinite.flowindicatorsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.infinite.flowindicator.FlowIndicator;

public class MainActivity extends AppCompatActivity {

    private FlowIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndicator= (FlowIndicator) findViewById(R.id.flowIndicator);
        mIndicator.setFlow(new String[]{"step1","step2","step3","step4","step5"},3);
    }
}
