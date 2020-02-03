package com.napier.sem;

import com.napier.sem.DataLayer.SQLConnection;

public class App {
    public static void main(String[] args)
    {
        System.out.println("Hello Kevin this is group 25!");
        //
        SQLConnection database = new SQLConnection();

        if (database.db != null)
        {
            System.out.print("Connection pass");
            database.disconnect();
        }
        else
        {
            System.out.print("Connection failed");
        }
    }
}
