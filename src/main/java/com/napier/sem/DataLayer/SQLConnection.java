package com.napier.sem.DataLayer;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author robertthepie stnlythepaltypus@gmail.com
 * @version 03/02/2020
 * Establishes an sql database connection and allows for queering of said database.
 * Does not inform the code if connection fails to establish, use boolean testConnection()
 */
public class SQLConnection {

    private Connection connection = null;

    /**
     * Creates a SQL database connection and allows for queering, drooping and reconnecting the database.
     */
    public SQLConnection() {
        reconnect();
    }

    /**
     * Attempts to establish or reestablish a database connection/
     *
     * @return boolean - Returns whether a database connection was established.
     */
    public boolean reconnect() {
        //Getting the database driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error Establishing database drivers");
            System.exit(-1);
        }
        //Establishing a means of communication
        for (int i = 0; i < 100; i++) {
            //Tries to connect
            try {
                connection = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Establishing a means communication.");
                break;
            }
            //Connection attempt failed
            catch (SQLException e) {
                System.out.println("Connection attempt: " + i + " Failed");
                try {
                    Thread.sleep((10000));
                } catch (InterruptedException e2) {
                    System.out.println("Shouldn't happen");
                }
            }
        }
        //Displays where the connection was achieved.
        if (connection != null) {
            System.out.println("Connection successful.");
            return true;
        } else {
            System.out.println("Connection Failed.");
            return false;
        }
    }

    /**
     * Break the database connection.
     *
     * @return boolean - Whether the connection was drooped.
     */
    public boolean disconnect() {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Tests the database connection
     *
     * @return boolean - whether a database connection has been established
     */
    public boolean testConnection() {
        if (connection == null) {
            return false;
        }
        return true;
    }

    public ArrayList<PopulationReport> popWithoutCity(String areaName, int areaCategory) {
        String categoryName = null;
        switch (areaCategory)
        {
            case 0:
                categoryName = "Code";
                break;
            case 1:
                categoryName = "Name";
                break;
            case 2:
                categoryName = "Continent";
                break;
            case 3:
                categoryName = "Region";
                break;
            default:
                break;
        }
        try {
            Statement statement = connection.createStatement();
            String query = "Select waterDown2.areaType, sum(waterDown2.pop1), sum(waterDown2.pop2) from( " +
                    "Select waterDown.areaType as areaType, waterDown.pop1 as pop1, sum(waterDown.pop2) as pop2 from( " +
                    "Select country." + areaCategory + " as areaType, country.Popilation as pop1, city.Population as pop2 " +
                    "From 'Country' RIGHT JOIN city ON country.code = city.CountryCode " +
                    "Where country." + categoryName + " '" + areaName + "') as waterDown " +
                    "group by derp.name) as waterDown2 group by waterDown2.areaType";
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<PopulationReport> results = new ArrayList<>();
            if(resultSet.next()) {
                results.add(new PopulationReport(resultSet.getString("areaType"), resultSet.getInt("pop1"), resultSet.getInt("pop2")));
            }
            else if (results.isEmpty())
            {
                return null;
            }
            else {
                return results;
            }
        } catch (Exception e) {
            return null;
        }
        //This can't actually be reached?
        return null;
    }
}
