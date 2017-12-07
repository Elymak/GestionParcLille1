package com.breuzon.gestionparclille1.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by serial on 04/12/2017.
 *
 * <p>
 *     Cette classe représente le model stocké en base d'un problème rencontré dans le parc
 *     de Lille 1
 * </p>
 *
 * <p>
 *     La table comprend 6 champs :
 * </p>
 *
 * <ul>
 *     <li>identifiant</li>
 *     <li>type de problème</li>
 *     <li>latitude</li>
 *     <li>longitude</li>
 *     <li>adresse</li>
 *     <li>description</li>
 * </ul>
 *
 */
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * définition des champs de la table dans des variables statiques
     */
    private static final String ID_FIELD = "report_id";
    private static final String REP_FIELD = "report_type";
    private static final String LAT_FIELD = "lat";
    private static final String LON_FIELD = "lon";
    private static final String ADR_FIELD = "adr";
    private static final String DESC_FIELD = "desc";


    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int reportId;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = REP_FIELD)
    public ReportType reportType;

    @DatabaseField(columnName = LAT_FIELD)
    public double latitude;

    @DatabaseField(columnName = LON_FIELD)
    public double longitude;

    @DatabaseField(columnName = ADR_FIELD)
    public String adresse;

    @DatabaseField(columnName = DESC_FIELD)
    public String description;

    /**
     * Constructeur par défaut de l'objet Report
     */
    public Report() {
        //constructeur vide utile pour SQLite
    }

    /**
     * Constructeur de l'objet Report
     * @param reportType le type de report
     * @param latitude la latitude du report
     * @param longitude la longitude du report
     * @param adresse l'adresse du report (si elle existe)
     * @param description la description du report
     */
    public Report(ReportType reportType, double latitude, double longitude, String adresse, String description) {
        this.reportType = reportType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.description = description;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
