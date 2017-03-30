package com.iigt.myscoleftview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.iigt.myscoleftview.R;
import com.iigt.myscoleftview.view.MyScrollView;

public class MainActivity extends AppCompatActivity implements MyScrollView.OnProgressChangedListener {
    private static final String TAG = "MainActivity";

    MyScrollView proViewDft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        proViewDft = (MyScrollView) findViewById(R.id.proview_dft);
        proViewDft.setProgress(15);
        proViewDft.setOnProgressChangedListener(this);
    }

    @Override
    public void onProgressChanged(View v, int progress) {
        String name = null;
        switch (v.getId()) {
            case R.id.proview_dft:
                name = "proViewDft";
                break;
        }
        Log.i(TAG, name + " 进度发生变化, progress == " + progress);
    }

}
