package com.napier.sem.DataLayer;
import java.sql.*;

public class SQLConnection
{

    public Connection db = null;
    public SQLConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.print("Error Establishing means of communication");
            System.exit(-1);
        }

        for (int i = 0; i < 100; i++)
        {
            try
            {
                db = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.print("Establishing a means communication.");
                break;
            }
            catch (SQLException e)
            {
                System.out.print("Conncetion attempt: " + i + " Failed");
                try
                {
                    Thread.sleep((10000));
                }
                catch (InterruptedException e2)
                {
                    System.out.print("WHAtoiargilugaei");
                }
            }
        }
        if (db != null)
        {
            System.out.print("Connection successful.");
        }
    }

    public boolean disconnect()
    {
        if (db != null)
        {
            try {
                db.close();
                return true;
            }
            catch (SQLException e)
            {
                return false;
            }
        }
        return false;
    }
}
