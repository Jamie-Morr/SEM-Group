package com.napier.sem.DataLayer;
import java.sql.*;

/**
 * @author robertthepie stnlythepaltypus@gmail.com
 * @version 03/02/2020
 * Establishes an sql database connection and allows for queering of said database.
 * Does not inform the code if connection fails to establish, use boolean testConnection()
 */
public class SQLConnection
{

    private Connection connection = null;

    /**
     * Creates a SQL database connection and allows for queering, drooping and reconnecting the database.
     */
    public SQLConnection()
    {
        reconnect();
    }

    /**
     * Attempts to establish or reestablish a database connection/
     * @return boolean - Returns whether a database connection was established.
     */
    public boolean reconnect()
    {
        //Getting the database driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error Establishing database drivers");
            System.exit(-1);
        }
        //Establishing a means of communication
        for (int i = 0; i < 100; i++)
        {
            //Tries to connect
            try
            {
                connection = DriverManager.getConnection("jdbc:mysql://db:33060/world?useSSL=false", "root", "example");
                System.out.println("Establishing a means communication.");
                break;
            }
            //Connection attempt failed
            catch (SQLException e)
            {
                System.out.println("Connection attempt: " + i + " Failed");
                try
                {
                    Thread.sleep((10000));
                }
                catch (InterruptedException e2)
                {
                    System.out.println("Shouldn't happen");
                }
            }
        }
        //Displays where the connection was achieved.
        if (connection != null) {
            System.out.println("Connection successful.");
            return true;
        }
        else
        {
            System.out.println("Connection Failed.");
            return false;
        }
    }

    /**
     * Break the database connection.
     * @return boolean - Whether the connection was drooped.
     */
    public boolean disconnect()
    {
        if (connection != null)
        {
            try {
                connection.close();
                return true;
            }
            catch (SQLException e)
            {
                return false;
            }
        }
        return false;
    }

    /**
     * Tests the database connection
     * @return boolean - whether a database connection has been established
     */
    public boolean testConnection()
    {
        if (connection == null)
        {
            return false;
        }
        return true;
    }
}
