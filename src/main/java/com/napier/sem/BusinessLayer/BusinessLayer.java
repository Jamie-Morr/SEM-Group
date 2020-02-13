package com.napier.sem.BusinessLayer;

import com.napier.sem.DataLayer.Column;
import com.napier.sem.DataLayer.PopulationReport;
import com.napier.sem.DataLayer.SQLConnection;

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
}
