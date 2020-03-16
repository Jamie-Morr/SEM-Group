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
            case WORLD:
                result = new String[1][2];
                if (prs != null && prs.size() > 0) {
                result[0][0] = "World Population:";
                result[0][1] = "" + prs.get(0).getOtherDetails()[0];
                return result;
                } else {
                    System.out.println("Owwie empty result at WORLD ??");
                    return null;
                }
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
        return getCityReport(column, target, -1, false);
    }

    public String[][] getCityReport(Column column, String target, int limit) {
        return getCityReport(column, target, limit, false);
    }

    public String[][] getCityReport(Column column, String target, boolean flip) {
        return  getCityReport(column, target, -1, flip);
    }

    public String[][] getCityReport(Column column, String target, int limit, boolean flip) {
        ArrayList<PopulationReport> prs = db.topCityPop(column, target, limit, flip);
        String[][] result;
        switch (column) {
            case DISTRICT:
                result = new String[(prs.size() + 1)][3];
                result[0][1] = "City Name";
                result[0][0] = "District";
                result[0][2] = "Population";
                break;
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

    /**
     * Intended for Column.WORLD, otherwise return null;
     * @return 2d string grid of Name, Country and Population of all world capitals
     */
    public String[][] getCapitalsOf(Column column) {
        return getCapitalsOf(column, -1, false);
    }
    /**
     * Retuns all the capitals of the specified target
     * @param column - The region type that the capitals of are to be returned
     * @param target - The name of the region
     * @return 2d string grid of Name, Country and Population
     */
    public String[][] getCapitalsOf(Column column, String target) {
        return getCapitalsOf(column, target, -1, false);
    }
    /**
     * Retuns all the capitals of the specified target
     * @param column - The region type that the capitals of are to be returned
     * @param target - The name of the region
     * @param limit - max no of results
     * @return 2d string grid of Name, Country and Population
     */
    public String[][] getCapitalsOf(Column column, String target, int limit) {
        return getCapitalsOf(column, target, limit, false);
    }
    /**
     * Retuns all the capitals of the specified target
     * @param column - The region type that the capitals of are to be returned
     * @param target - The name of the region
     * @param flip - If the list should go from smallest to largest population amount
     * @return 2d string grid of Name, Country and Population
     */
    public String[][] getCapitalsOf(Column column, String target, boolean flip) {
        return getCapitalsOf(column, target, -1, flip);
    }
    /**
     * Intended for Column.WORLD, otherwise return null;
     * @param column - Column.WORLD
     * @param limit - max no of results
     * @param flip - If the list should go from smallest to largest population amount
     * @return 2d string grid of Name, Country and Population of all world capitals
     */
    public String[][] getCapitalsOf(Column column, int limit, boolean flip) {
        ArrayList<PopulationReport> prs = db.CapitalsOf(column, limit, flip);
        String[][] result;
        if (column == Column.WORLD) {
            result = new String[(prs.size() + 1)][3];
            result[0][0] = "City Name";
            result[0][1] = "Country";
            result[0][2] = "Population";
        }
        else {
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
    /**
     * Retuns all the capitals of the specified target
     * @param column - The region type that the capitals of are to be returned
     * @param target - The name of the region
     * @param limit - max no of results
     * @param flip - If the list should go from smallest to largest population amount
     * @return 2d string grid of Name, Country and Population
     */
    public String[][] getCapitalsOf(Column column, String target, int limit, boolean flip) {
        ArrayList<PopulationReport> prs = db.CapitalsOf(column, target, limit, flip);
        String[][] result;
        switch (column) {
            case REGION: case CONTINENT:
                result = new String[(prs.size() + 1)][3];
                result[0][0] = "City Name";
                result[0][1] = "Country";
                result[0][2] = "Population";
                break;
            case WORLD:
                return getCapitalsOf(column, limit, flip);
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
