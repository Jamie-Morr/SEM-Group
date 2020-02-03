package com.napier.sem;

import com.napier.sem.DataLayer.SQLConnection;

/**
 * This will evetualy aid in the conquest of the world through allowing the user<br>to view the populations of the world as wer as the number and percentage of people speaking certain languages<br>within specfied area(s)/location types
 * @author Oliwier, Arnas but NOT PETER
 * @version 03/02/2020
 */
public class App {
    public static void main(String[] args)
    {
        //Arnas, prints to console "Hello Kevin this is group 25!"
        System.out.println("Hello Kevin this is group 25!");
        //Oliwier, SQL connections
        SQLConnection database = new SQLConnection();

        if (database.testConnection())
        {
            System.out.println("Connection pass");
            database.disconnect();
        }
        else
        {
            System.out.println("Connection failed");
        }
    }
}
