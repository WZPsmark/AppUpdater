package com.sk.appupdatepro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sk.appupdate.SkAppUpdater;

public class MainActivity extends AppCompatActivity {

    Button checkUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdate = findViewById(R.id.checkUpdate);
        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkAppUpdater.checkUpdate(MainActivity.this, SkAppUpdater.INITIATIVE_CHECK);
            }
        });

        SkAppUpdater.checkUpdate(MainActivity.this, SkAppUpdater.SILENT_CHECK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkAppUpdater.getINetManager().cancer(this);
    }
}
