package com.breuzon.gestionparclille1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
            showConfirmationDeletePopup(this);
        } else {
            Toast.makeText(this, "Un problème est survenu lors de l'affichage du layout", Toast.LENGTH_LONG).show();
        }
    }

    // Get the x and y position after the button is draw on screen
    // (It's important to note that we can't get the position in the onCreate(),
    // because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        deleteButton.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    private void showConfirmationDeletePopup(final Activity context){
        //int popupWidth = 200;
        //int popupHeight = 150;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.confirm_delete_popup, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        //popup.setWidth(popupWidth);
        //popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -60;
        int OFFSET_Y = -80;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        // Getting a reference to No button, and close the popup when clicked.
        Button noButton = layout.findViewById(R.id.popup_no);
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        // Getting a reference to Yes button, and delete the report when clicked.
        Button yesButton = layout.findViewById(R.id.popup_yes);
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
