package com.napier.sem;

import com.napier.sem.DataLayer.PopulationReport;
import com.napier.sem.DataLayer.SQLConnection;
import java.util.ArrayList;

/**
 * This will evetualy aid in the conquest of the world through allowing the user<br>to view the populations of the world as wer as the number and percentage of people speaking certain languages<br>within specfied area(s)/location types
 * @author Oliwier, Arnas but NOT PETER
 * @version 03/02/2020
 */
public class App {
    private static SQLConnection database = null;
    public static void main(String[] args)
    {
        //Initial app code
        System.out.println("Hello Kevin this is group 25!");
        //Establishes an sql Connection
        database = new SQLConnection();
        //Checks if it connected in the end
        if (!database.testConnection())
        {
            System.out.println("Connection failled, exiting app");
            System.exit(-1);
        }
        else
        {
            System.out.println("Connection pass");
        }
        //@TODO add code here

        System.out.println("TEST 1: Area population and its cities");
        if (testPopWithCity()) {
            System.out.println("PASS");
        }
        else {
            System.out.println("FAIL");
        }





        //Ends the database connection
        database.disconnect();
        database = null;
    }

    static boolean testPopWithCity()
    {
        ArrayList<PopulationReport> report;
        report = database.popWithoutCity("Eastern Europe", 3);
        if (report != null) {
            for (PopulationReport rep : report) {
                System.out.println(rep.conntents());
            }
            return true;
        }
        else
        {
            return false;
        }
    }

}
