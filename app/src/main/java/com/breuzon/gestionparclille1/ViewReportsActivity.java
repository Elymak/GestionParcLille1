package com.breuzon.gestionparclille1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.breuzon.gestionparclille1.adapter.ReportListAdapter;
import com.breuzon.gestionparclille1.dao.DatabaseHelper;
import com.breuzon.gestionparclille1.model.Report;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewReportsActivity extends AppCompatActivity {

    /**
     * acces a la DB
     */
    private DatabaseHelper databaseHelper;

    @BindView(R.id.reportListView)
    ListView reportListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        ButterKnife.bind(this);

        refreshLayout();

        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Report report = (Report) adapter.getItemAtPosition(position);
                int id = report.getReportId();

                Intent intent = new Intent(ViewReportsActivity.this, ReportDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("reportId", id);
                intent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(intent, 1);
            }
        });
    }

    private void refreshLayout() {
        List<Report> reports = getReportList();
        ReportListAdapter adapter = new ReportListAdapter(this, reports);
        reportListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            refreshLayout();
        } else {
            Toast.makeText(this, "result no ok", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * permet de retourner la liste des Report
     * @return List
     */
    private List<Report> getReportList(){
        try {
            Dao<Report, Integer> dao = getHelper().getReportDao(); //this can throws a SQLException
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(AddReportActivity.class.getName(), "Cannot get all Reports", e);
            return new ArrayList<>();
        }
    }

    /**
     * permet d'accéder à la base
     * @return DatabaseHelper
     */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
