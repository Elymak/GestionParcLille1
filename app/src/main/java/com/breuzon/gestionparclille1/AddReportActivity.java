package com.breuzon.gestionparclille1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.breuzon.gestionparclille1.dao.DatabaseHelper;
import com.breuzon.gestionparclille1.model.Report;
import com.breuzon.gestionparclille1.model.ReportType;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddReportActivity extends AppCompatActivity implements LocationListener{

    /**
     * utilisé pour la géolocalisation
     */
    private LocationManager locationManager;

    /**
     * provider qui permet la localisation
     */
    private String provider;

    /**
     * acces a la DB
     */
    private DatabaseHelper databaseHelper;

    /**
     * latitude
     */
    private double latitude;

    /**
     * longitude
     */
    private double longitude;

    //views
    @BindView(R.id.localisationTextView)
    TextView localisationTextView;

    @BindView(R.id.submitButton)
    Button submitButton;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.reportTypeSpinner)
    Spinner reportTypeSpinner;

    @BindView(R.id.adresseEditText)
    EditText adresseEditText;

    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        ButterKnife.bind(this);

        //récupération des objets servant à la géolocalisation
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,true);

        //construction du spinner

        //List<ReportType> spinnerItems = getReportTypeList();
        //ReportTypeSpinnerAdapter adapter = new ReportTypeSpinnerAdapter(this, spinnerItems);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report_type_spinner_items, android.R.layout.simple_spinner_item);
        reportTypeSpinner.setAdapter(adapter);

        submitButton.setEnabled(false);

        refreshLocationAndLayout();

    }

    /*
     *                      Cette partie traite de la géolocalisation
     */

    /**
     * méthode appelée lorsqu'on clique sur le boutton "refresh"
     * permet de géolocaliser le device et de mettre à jour la vue
     */
    public void refreshLocationAndLayout() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "Impossible de récupérer les coordonnées GPS, permissions non accordées", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 2000, 5, this);

            if(location != null) {
                onLocationChanged(location);
            } else {
                Toast.makeText(this, "Impossible de récupérer la localisation actuelle", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * permet de mettre à jour les textviews
     */
    private void refreshLayout(Location location){
        DecimalFormat df = new DecimalFormat("#.00");
        localisationTextView.setText(String.format("%s°N %s° O", df.format(latitude), df.format(longitude)));
        submitButton.setEnabled(true);
        updateAddress(location);
    }

    /**
     * permet de rechercher l'adresse postale de la localisation actuelle
     */
    private void updateAddress(Location location){
        Geocoder geo = new Geocoder(this);
        try {
            List<Address> list = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(list != null && list.size() > 0){
                Address address = list.get(0);
                adresseEditText.setText(String.format("%s, %s %s",
                        address.getAddressLine(0),
                        address.getPostalCode(),
                        address.getLocality()));
            } else {
                adresseEditText.setText("Impossible de récupérer l'adresse");
            }

        } catch (IOException e) {
            adresseEditText.setText("Impossible de récupérer l'adresse");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        refreshLayout(location);
    }

    @Override
    public void onStatusChanged(String s, int status, Bundle bundle) {
       //nothing to do
    }

    @Override
    public void onProviderEnabled(String s) {
        //nothing to do
    }

    @Override
    public void onProviderDisabled(String s) {
        //nothing to do
    }

    /*
     *                      Cette partie traite de la sauvegarde en base
     */

    /**
     * methode appelée lorsqu'on clique sur le boutton "submit"
     * permet de sauvegarder un problème en base
     */
    @OnClick(R.id.submitButton)
    public void saveReport(){
        //recupère les données de la vue et les sauvegarde
        try {
            CharSequence reportTypeName = (CharSequence) reportTypeSpinner.getSelectedItem();
            ReportType reportType = new ReportType();
            for(ReportType r : getReportTypeList()){
                if(r.getName().equals(reportTypeName)){
                    reportType = r;
                }
            }

            Report report = new Report(
                    reportType,
                    latitude,
                    longitude,
                    adresseEditText.getText().toString(),
                    descriptionEditText.getText().toString()
            );

            Dao<Report, Integer> reportDao = getHelper().getReportDao();
            reportDao.create(report);

            locationManager.removeUpdates(this);
            Toast.makeText(this, "Problème sauvegardé", Toast.LENGTH_LONG).show();
            finish();
        } catch (SQLException e) {
            Log.e(AddReportActivity.class.getName(), "Cannot save a report", e);
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

    /**
     * permet de retourner la liste des ReportType
     * @return List
     */
    private List<ReportType> getReportTypeList(){
        try {
            Dao<ReportType, Integer> dao = getHelper().getReportTypesDao(); //this can throws a SQLException
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(AddReportActivity.class.getName(), "Cannot get all ReportTypes", e);
            return new ArrayList<>();
        }
    }

    @OnClick(R.id.cancelButton)
    public void endActivity(){
        locationManager.removeUpdates(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}
