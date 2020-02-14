package com.napier.sem.BusinessLayer;

import com.napier.sem.DataLayer.Column;
import com.napier.sem.DataLayer.PopulationReport;
import com.napier.sem.DataLayer.SQLConnection;

import java.util.ArrayList;

/**
 * @author 40344894
 * Takes in requests and returns them from the data layer in a 2d string grid format
 */
public class BusinessLayer {
    SQLConnection db;
    public BusinessLayer() {
        //Establishes an sql Connection
        db = new SQLConnection();
        //Checks if it connected in the end
        if (!db.testConnection())
        {
            System.out.println("Connection failled, exiting app");
            System.exit(-1);
        }
        else
        {
            System.out.println("Connection pass");
        }
    }

    /**
     * Terminates the database connection
     * @return If there was something to terminate
     */
    public boolean end() {
        try {
            db.disconnect();
            db = null;
            System.out.println("Connection Terminated");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns a table with the population results for the specified area
     * @param column - class of the target area
     * @param target - name of the target
     * @return 2d array forming a table of sql results
     */
    public String[][] getPopAdv(Column column, String target) {
        //Getting the needed info
        PopulationReport pr = db.popWithoutCity(target, column);
        String[][] result = new String[2][6];
        //Setting up column identifiers
        result[0][0] = "Name:";
        result[0][1] = "Total Population";
        result[0][2] = "City Population";
        result[0][3] = "City %";
        result[0][4] = "Outside City Population";
        result[0][5] = "Outside %";
        //Single time formatting
        result[1][0] = pr.getName();
        result[1][1] = "" + pr.getPop();
        result[1][2] = "" + pr.getCityPop();
        result[1][3] = "" + (((float) pr.getCityPop())/((float) pr.getPop()) * 100) + "%";
        result[1][4] = "" + (pr.getPop() - pr.getCityPop());
        result[1][5] = "" + (((float) (pr.getPop() - pr.getCityPop()))/((float) pr.getPop()) * 100) + "%";
        return result;
    }

    /**
     * Returns a population list for the requested value
     * @param column - Area that is to be picked from
     * @return d2 grid containing the specified column and its sub-details as well as its population
     */
    public  String[][] getTopPops(Column column) {
        return getTopPops(column, -1, false);
    }

    /**
     * Returns a population list for the requested value
     * @param column - Area that is to be picked from
     * @param flip - If true the list goes from smallest population to largest
     * @return d2 grid containing the specified column and its sub-details as well as its population
     */
    public  String[][] getTopPops(Column column, boolean flip) {
        return getTopPops(column, -1, flip);
    }

    /**
     * Returns a population list for the requested value
     * @param column - Area that is to be picked from
     * @param limit - max no of results that are to be displayed
     * @return d2 grid containing the specified column and its sub-details as well as its population
     */
    public String[][] getTopPops(Column column, int limit) {
        return  getTopPops(column, limit, false);
    }

    /**
     * Returns a population list for the requested value
     * @param column - Area that is to be picked from
     * @param limit - max no of results that are to be displayed
     * @param flip - If true the list goes from smallest population to largest
     * @return d2 grid containing the specified column and its sub-details as well as its population
     */
    public String[][] getTopPops(Column column, int limit, boolean flip) {
        //Getting the needed info
        ArrayList<PopulationReport> prs = db.topPop(column, limit, false);
        //Establishing the labels
        String[][] result;
        switch (column) {
            case CODE: case NAME:
                result = new String[(prs.size()+1)][6];
                result[0][0] = "Code:";
                result[0][1] = "Name:";
                result[0][2] = "Continent:";
                result[0][3] = "Region:";
                result[0][4] = "Capital:";
                result[0][5] = "Population:";
                break;
            case CONTINENT:
                result = new String[(prs.size()+1)][3];
                result[0][0] = "Continent:";
                result[0][1] = "Region:";
                result[0][2] = "Population:";
                break;
            case REGION:
                result = new String[(prs.size()+1)][2];
                result[0][0] = "Region:";
                result[0][1] = "Population:";
                break;
            default:
                return null;
        }
        //Inserting the data into a 2d grid format
        //For each report in the list
        int i = 1;
        for (PopulationReport pr:prs) {
            //for each peace of information it should hold
            int j = 0;
            for (String string:pr.getOtherDetails()) {
                //In case there's an issue with a value
                if (string != null && string.length() >= 1) {
                    result[i][j++] = string;
                } else {
                    result[i][j++] = " ";
                }
            }
            //Plus the population
            result[i++][j] = "" + pr.getPop();
        }
        return result;
    }

    public String[][] getCityReport(Column column, String target) {
        ArrayList<PopulationReport> prs = db.topCityPop(column, target);
        String[][] result;
        switch (column) {
            case CODE: case NAME:
                return null;
            case REGION: case CONTINENT:
                result = new String[(prs.size() + 1)][4];
                result[0][0] = "Country:";
                result[0][1] = "City Name";
                result[0][2] = "District";
                result[0][3] = "Population";
                break;
            default:
                return null;
        }
        //Inserting the data into a 2d grid format
        //For each report in the list
        int i = 1;
        for (PopulationReport pr:prs) {
            //for each peace of information it should hold
            int j = 0;
            for (String string:pr.getOtherDetails()) {
                //In case there's an issue with a value
                if (string != null && string.length() >= 1) {
                    result[i][j++] = string;
                } else {
                    result[i][j++] = " ";
                }
            }
            //Plus the population
            result[i++][j] = "" + pr.getPop();
        }
        return result;
    }
}
