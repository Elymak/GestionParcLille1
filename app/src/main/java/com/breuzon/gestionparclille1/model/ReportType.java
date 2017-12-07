package com.breuzon.gestionparclille1.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by serial on 07/12/2017.
 */

public class ReportType {

    @DatabaseField(generatedId = true)
    public int reportTypeId;

    @DatabaseField(columnName = "report_type_name")
    public String name;

    public ReportType(){

    }

    public ReportType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
