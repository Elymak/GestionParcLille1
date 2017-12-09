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
 */

public class ReportListAdapter extends ArrayAdapter<Report> {

    public ReportListAdapter(Context context, List<Report> reportList){
        super(context, 0, reportList);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Report report = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_list_view_layout, parent, false);
        }

        TextView reportListViewReportType = convertView.findViewById(R.id.reportListViewReportType);
        reportListViewReportType.setText(report.getReportType());

        Log.d(ReportListAdapter.class.getName(), "#######\n# Report Type Name : " + report.getReportType() + "\n#######");


        TextView reportListViewLocation = convertView.findViewById(R.id.reportListViewLocation);
        DecimalFormat df = new DecimalFormat("#.00");
        reportListViewLocation.setText(String.format("%s°N %s° O", df.format(report.getLatitude()), df.format(report.getLongitude())));

        return convertView;
    }

}
