package com.napier.sem.DataLayer;

/**
 * Class responsible for storing an individual row from an sql query result.#
 * @version 0.1
 * @author stanl
 */
public class PopulationReport {
    private String name;
    private String[] otherDetails;
    private int pop;
    private int cityPop;

    /**
     * Empty constructor shouldn't be used as values can not be edited
     */
    private PopulationReport()
    {

    }

    /**
     * Stores a location name and value association
     * @param name
     * @param pop
     */
    PopulationReport(String name, int pop)
    {
        this.name = name;
        this.pop = pop;
    }

    PopulationReport(String[] otherDetails, int pop) {
        this.otherDetails = otherDetails;
        this.pop = pop;
    }

    /**
     * Stores a location and 2 numbers, to be used for storing a location pop and sub-locations' pop
     * @param name
     * @param pop
     * @param cityPop
     */
    PopulationReport(String name, int pop, int cityPop)
    {
        this.name = name;
        this.pop = pop;
        this.cityPop = cityPop;
    }

    /**
     * A test function dedicated to displaying the contents of this class
     * @return Content of this class.
     */
    public String conntents()
    {
        return "name: " + name + ", pop: " + pop + ", city population: " + cityPop;

    }

    public String getName() {
        return name;
    }

    public int getPop() {
        return pop;
    }

    public int getCityPop() {
        return cityPop;
    }

    public String[] getOtherDetails() {
        return otherDetails;
    }
}
