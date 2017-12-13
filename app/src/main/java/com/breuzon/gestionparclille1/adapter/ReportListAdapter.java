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
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by serial on 06/12/2017.
 *
 * Cette classe permet la gestion des items dans la list view des reports
 * lorsqu'on affiche la liste des reports
 */

public class ReportListAdapter extends ArrayAdapter<Report> {

    public ReportListAdapter(Context context, List<Report> reportList){
        super(context, 0, reportList);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //on récupère le report à la position
        Report report = getItem(position);

        //on récupère la vue
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_list_view_layout, parent, false);
        }

        //on récupère la text view et on set le type du report
        TextView reportListViewReportType = convertView.findViewById(R.id.reportListViewReportType);
        reportListViewReportType.setText(report.getReportType());

        //on récupère la seconde text view pour afficher les coordonnées GPS du report
        TextView reportListViewLocation = convertView.findViewById(R.id.reportListViewLocation);
        DecimalFormat df = new DecimalFormat("#.00");
        reportListViewLocation.setText(String.format("%s°N %s° O", df.format(report.getLatitude()), df.format(report.getLongitude())));

        return convertView;
    }

}
