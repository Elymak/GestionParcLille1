package com.breuzon.gestionparclille1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.breuzon.gestionparclille1.R;
import com.breuzon.gestionparclille1.dao.DatabaseHelper;
import com.breuzon.gestionparclille1.model.Report;
import com.breuzon.gestionparclille1.model.ReportType;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by serial on 06/12/2017.
 */

public class ReportListAdapter extends ArrayAdapter<Report> {

    public ReportListAdapter(Context context, List<Report> reportList){
        super(context, 0, reportList);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Report report = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_list_view_layout, parent, false);
        }

        TextView reportListViewReportType = convertView.findViewById(R.id.reportListViewReportType);
        reportListViewReportType.setText(report.getReportType().getName());

        Log.d(ReportListAdapter.class.getName(), "#######\n# Report Type Name : " + report.getReportType().getName() + "\n#######");

        try{
            Dao<ReportType, Integer> reportTypedao = getHelper().getReportTypesDao();

            //TODO

        } catch (SQLException e){

        }


        TextView reportListViewLocation = convertView.findViewById(R.id.reportListViewLocation);
        DecimalFormat df = new DecimalFormat("#.00");
        reportListViewLocation.setText(String.format("%s°N %s° O", df.format(report.getLatitude()), df.format(report.getLongitude())));

        return convertView;
    }

    /**
     * permet d'accéder à la base
     * @return DatabaseHelper
     */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }


}