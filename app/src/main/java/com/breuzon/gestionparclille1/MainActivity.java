package com.breuzon.gestionparclille1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Cette classe permet de naviguer entre les différentes fonctionnalités
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewReportsButton)
    Button viewReportsButton;

    @BindView(R.id.addReportsButton)
    Button addReportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addReportsButton)
    public void callAddReportActivity() {
        Intent addReportActivity = new Intent(MainActivity.this, AddReportActivity.class);
        startActivity(addReportActivity);
    }

    @OnClick(R.id.viewReportsButton)
    public void callViewReportActivity() {
        Intent viewReportsActivity = new Intent(MainActivity.this, ViewReportsActivity.class);
        startActivity(viewReportsActivity);
    }
}
