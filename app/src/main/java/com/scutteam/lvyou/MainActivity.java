package com.scutteam.lvyou;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.activeandroid.ActiveAndroid;
import com.loopj.android.http.SyncHttpClient;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //下面两行只是为了测试
        SyncHttpClient client = new SyncHttpClient();
        ActiveAndroid.initialize(this);
    }
}
