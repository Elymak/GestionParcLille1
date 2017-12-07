package com.breuzon.gestionparclille1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.breuzon.gestionparclille1.R;
import com.breuzon.gestionparclille1.model.Report;
import com.breuzon.gestionparclille1.model.ReportType;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by serial on 04/12/2017.
 *
 * <p>Cette classe permet d'accéder à la base</p>
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /*
     * variables statiques pour le nom de la DB et son numéro de version
     */

    /**
     * nom de la DB
     */
    private static final String DATABASE_NAME = "reports_database.db";

    /**
     * version de la DB
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * liste de tous les types de reports possible
     */
    private static List<ReportType> reportTypesList = new ArrayList<ReportType>() {{
        add(new ReportType("Arbre à tailler"));
        add(new ReportType("Arbre à abattre"));
        add(new ReportType("Détritus"));
        add(new ReportType("Haie à tailler"));
        add(new ReportType("Mauvaise herbe"));
        add(new ReportType("Autre"));
    }};

    /**
     * Dao pour les ReportType
     */
    private Dao<ReportType, Integer> reportTypesDao;

    /**
     * Dao pour les Report
     */
    private Dao<Report, Integer> reportDao;

    /**
     * Constructeur de l'objet DatabaseHelper
     * @param context le contexte de l'application
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            // création des tables
            TableUtils.createTable(connectionSource, ReportType.class);
            TableUtils.createTable(connectionSource, Report.class);
            getReportTypesDao().create(reportTypesList);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            // Dans le cas d'un éventuel changement dans la base pour une nouvelle version de l'appli
            // incrémentez la varaible DATABASE_VERSION, alors cette méthode sera invoquée
            // automatiquement.

            // Le développeur doit gérer la logique de mise à niveau ici, c'est-à-dire créer
            // une nouvelle table ou une nouvelle colonne dans une table existante,
            // prendre les sauvegardes de la base de données existante, etc.

            TableUtils.dropTable(connectionSource, ReportType.class, true);
            TableUtils.dropTable(connectionSource, Report.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version "
                    + oldVersion + " to new " + newVersion, e);
        }
    }


    /**
     * permet de récupérer le Dao des ReportTypes
     * @return Dao
     * @throws SQLException Exception SQL
     */
    public Dao<ReportType, Integer> getReportTypesDao() throws SQLException {
        if (reportTypesDao == null) {
            reportTypesDao = getDao(ReportType.class);
        }
        return reportTypesDao;
    }

    /**
     * permet de récupérer le Dao des Reports
     * @return Dao
     * @throws SQLException Exception SQL
     */
    public Dao<Report, Integer> getReportDao() throws SQLException {
        if (reportDao == null) {
            reportDao = getDao(Report.class);
        }
        return reportDao;
    }
}
