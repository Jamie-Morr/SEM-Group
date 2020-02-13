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

    /**
     * Stores the connection
     */
    private Connection connection = null;

    /**
     * Creates a SQL database connection and allows for questioning of the database.
     */
    public SQLConnection() {
        reconnect();
    }

    /**
     * Attempts to establish or reestablish a database connection
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
     * @return boolean - Whether the connection was drooped.
     */
    public boolean disconnect() {
        //Tries to close the connection even if there is non
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Tests the database connection.
     * @return boolean - whether a database connection has been established
     */
    public boolean testConnection() {
        //This is a very complex function and explaining its complex interconnecting functionalities and dependancies can be brain-breaking but to sum up this mess, if the connection isn't missing returns true
        return connection != null;
    }

    /**
     * Gets the combined population of the specified location as well as its cities.
     * @param areaName - content that is being searched for
     * @param areaCategory - class of what is being search for
     * @return A class contaning the name and population number of the specified location as well as the combined population of its cities.
     */
    public PopulationReport popWithoutCity(String areaName, Column areaCategory) {
        //converts the enum into the corresponding column
        String categoryName;
        switch (areaCategory) {
            case CODE:
                categoryName = "Code";
                break;
            case NAME:
                categoryName = "Name";
                break;
            case CONTINENT:
                categoryName = "Continent";
                break;
            case REGION:
                categoryName = "Region";
                break;
            default:
                return null;
        }
        try {
            //Creates and calls the sql query
            Statement statement = connection.createStatement();
            String query = "Select waterDown2.areaType as areaType, sum(waterDown2.pop1) as pop1, sum(waterDown2.pop2) as pop2 from( " +
                    "Select waterDown.areaType as areaType, waterDown.pop1 as pop1, sum(waterDown.pop2) as pop2 from( " +
                    "Select country.name as name, country." + categoryName + " as areaType, country.Population as pop1, city.Population as pop2 " +
                    "From country JOIN city ON country.code = city.CountryCode " +
                    "Where country." + categoryName + " = \"" + areaName + "\") as waterDown " +
                    "group by waterDown.name, waterDown.pop1) as waterDown2 group by waterDown2.areaType";
            ResultSet resultSet = statement.executeQuery(query);
            //Creates a list to store the results in, its an arrayList because they sound cooler than a list.
            ArrayList<PopulationReport> results = new ArrayList<>();
            //inserts the results into the list
            if (resultSet.next()) {
                return new PopulationReport(resultSet.getString("areaType"), resultSet.getInt("pop1"), resultSet.getInt("pop2"));
            }
            else {
                return null;
            }
        } catch (Exception e){
            //System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns a list of the specified column's populations order from top to lowest
     * @param column - what land mass is meant to be grouped by
     * @return list of PopulationReports containing the specified columns and their populations
     */
    public  ArrayList<PopulationReport> topPop(Column column) {
        //Calls the variation of the function that has a limit but slaps it with that negative energy.
        return topPop(column, -1, false);
    }

    /**
     * Returns a list of the specified column's populations order from top to lowest
     * @param column - what land mass is meant to be grouped by
     * @param limit - how many volumes should be displayed
     * @return list of PopulationReports containing the specified columns and their populations
     */
    public ArrayList<PopulationReport> topPop(Column column, int limit) {
        return topPop(column, -1, false);
    }

    /**
     * Returns a list of the specified column's populations order from top to lowest
     * @param column - what land mass is meant to be grouped by
     * @param limit - how many volumes should be displayed
     * @param invert - if true the list goes from smallest to biggest population
     * @return list of PopulationReports containing the specified columns and their populations
     */
    public ArrayList<PopulationReport> topPop(Column column, int limit, boolean invert) {
        //converts the enum into the corresponding column
        String query;
        switch (column) {
            case CODE: case NAME:
                query = "SELECT Code, Name, Continent, Region, sum(Population) as Population, Capital";
                break;
            case CONTINENT:
                query = "SELECT Continent, Region, sum(Population) as Population";
                break;
            case REGION:
                query = "SELECT Region, sum(Population) as Population";
                break;
            default:
                return null;
        }
        try {
            //Creates and calls the sql query
            Statement statement = connection.createStatement();
            //Sets up the basic query
            query += " FROM country ";
            switch (column) {
                case CODE: case NAME:
                    query += "GROUP BY Code, Name, Continent, Region";
                    break;
                case CONTINENT:
                    query += "GROUP BY Continent, Region";
                    break;
                case REGION:
                    query += "GROUP BY Region";
                    break;
            }
            //Orders the list
            query += " ORDER BY sum(Population)";
            if (!invert) {
                query += " DESC";
            }
            //appends a limit if aplicable
            if (!(limit < 0)) {
                query += " LIMIT" + limit;
            }
            ResultSet resultSet = statement.executeQuery(query);
            //Creates a list to store the results in, its an arrayList because they sound cooler than a list.
            ArrayList<PopulationReport> results = new ArrayList<>();
            //inserts the results into the list
            switch (column) {
                case CODE: case NAME:
                    while (resultSet.next()) {
                        results.add(new PopulationReport(
                                new String[]{
                                        resultSet.getString("Code"),
                                        resultSet.getString("Name"),
                                        resultSet.getString("Continent"),
                                        resultSet.getString("Region"),
                                        resultSet.getString("Capital")
                                }
                                , resultSet.getInt("Population")));
                    }
                    break;
                case CONTINENT:
                    while (resultSet.next()) {
                        results.add(new PopulationReport(
                                new String[]{
                                        resultSet.getString("Continent"),
                                        resultSet.getString("Region")
                                }
                                , resultSet.getInt("Population")));
                    }
                    break;
                case REGION:
                    while (resultSet.next()) {
                        results.add(new PopulationReport(
                                new String[]{
                                        resultSet.getString("Region")
                                }
                                , resultSet.getInt("Population")));
                    }
                    break;
                }
            return results;
        } catch (Exception e){
            //System.out.println(e.getMessage());
            return null;
        }
    }

}
