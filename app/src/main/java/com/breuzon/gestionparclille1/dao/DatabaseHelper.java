package com.breuzon.gestionparclille1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.breuzon.gestionparclille1.R;
import com.breuzon.gestionparclille1.model.Report;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

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
            TableUtils.createTable(connectionSource, Report.class);

            //ajout de 10 problemes
            this.getReportDao().create(new Report("Arbre à tailler", 50.610072, 3.136982, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Mauvaise herbe", 50.610055, 3.136993, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Haie à tailler", 50.610067, 3.136944, "Avenue Paul Langevin","La haie nous bloque presque le passage"));
            this.getReportDao().create(new Report("Détritus", 50.610044, 3.136975, "Avenue Paul Langevin","Il n'y a plus de place dans la poubelle"));
            this.getReportDao().create(new Report("Arbre à tailler", 50.610103, 3.136965, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Haie à tailler", 50.610074, 3.136845, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Détritus", 50.610123, 3.136870, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Détritus", 50.610114, 3.137023, "Avenue Paul Langevin",""));
            this.getReportDao().create(new Report("Autre", 50.610090, 3.137043, "Avenue Paul Langevin","Les fleurs sont fanées"));
            this.getReportDao().create(new Report("Arbre à abattre", 50.610082, 3.136932, "Avenue Paul Langevin","Il est trop vieux et trop grand. Il pourrait causer de gros dégats..."));

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

            TableUtils.dropTable(connectionSource, Report.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version "
                    + oldVersion + " to new " + newVersion, e);
        }
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
