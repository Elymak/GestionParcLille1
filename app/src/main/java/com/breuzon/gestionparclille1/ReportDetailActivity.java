package com.breuzon.gestionparclille1;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    private Report report;

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

    @BindView(R.id.supprimerReportButton)
    public Button deleteButton;

    private Point p;

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

        report = null;
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
    public void onDeleteButtonClick(){
        if(p != null) {
            showDialog();
        } else {
            Toast.makeText(this, "Un problème est survenu lors de l'affichage du layout", Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(ReportDetailActivity.this);
        dialog.setContentView(R.layout.confirm_delete_popup);
        dialog.show();

        // Getting a reference to No button, and close the popup when clicked.
        Button noButton = dialog.findViewById(R.id.popup_no);
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Getting a reference to Yes button, and delete the report when clicked.
        Button yesButton = dialog.findViewById(R.id.popup_yes);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "YEEEEES", Toast.LENGTH_LONG).show();
                deleteAndFinishActivity();
            }
        });

    }

    private void deleteAndFinishActivity(){
        if(report != null){
            try {
                getHelper().getReportDao().delete(report);
                setResult(RESULT_OK,null);
                finish();
            } catch (SQLException e) {
                Toast.makeText(this, "Impossible de supprimer ce problème", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Un problème est survenu lors de la suppression", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.googleMapButton)
    public void googleMapOnClick(){
        startGoogleMapsActivity();
        //Toast.makeText(this, "google", Toast.LENGTH_LONG).show();
    }

    private void startGoogleMapsActivity(){
        if(report != null) {
            String s = "geo:" + report.getLatitude() + "," + report.getLongitude()
                    + "?q=" + report.getLatitude() + "," + report.getLongitude()
                    + "(" + report.getAdresse().replace(' ', '+') + ")";
            Uri gmmIntentUri = Uri.parse(s);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        } else {
            Toast.makeText(this, "Impossible de lancer l'application Google Maps", Toast.LENGTH_LONG).show();
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
