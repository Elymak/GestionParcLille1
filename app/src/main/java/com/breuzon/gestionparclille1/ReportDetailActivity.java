package com.breuzon.gestionparclille1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.breuzon.gestionparclille1.dao.DatabaseHelper;
import com.breuzon.gestionparclille1.model.Report;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @BindView(R.id.detailReportIdTextView)
    public TextView reportIdTextView;

    @BindView(R.id.detailReportTypeTextView)
    public TextView reportTypeTextView;

    @BindView(R.id.detailReportLocalisationTextView)
    public TextView reportLocalisationTextView;

    @BindView(R.id.detailReportAdresseTextView)
    public TextView reportAdresseTextView;

    @BindView(R.id.detailReportDescriptionTextView)
    public TextView reportDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null) {
            value = b.getInt("reportId");
        }

        Report report = null;
        try {
            report = getHelper().getReportDao().queryForId(value);
            showDetailsForReport(report);
        } catch (SQLException e) {
            Toast.makeText(this, "Ce problème n'existe pas", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void showDetailsForReport(Report report){
        if(report != null) {
            reportIdTextView.setText(report.getReportId()+"");
            reportTypeTextView.setText(report.getReportType());
            reportLocalisationTextView.setText(report.getLatitude() + "° N\n" + report.getLongitude() + "° O");
            reportAdresseTextView.setText(report.getAdresse());
            reportDescriptionTextView.setText(report.getDescription());
        } else {
            Toast.makeText(this, "Un problème est survenu lors de la récupération des données", Toast.LENGTH_LONG).show();
        }

    }

    @OnClick(R.id.supprimerReportButton)
    public void deleteOnClick(){
        Toast.makeText(this, "delete", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.googleMapButton)
    public void googleMapOnClick(){
        Toast.makeText(this, "google", Toast.LENGTH_LONG).show();
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
