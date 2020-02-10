package com.napier.sem.DataLayer;

public class PopulationReport {
    String name;
    int pop;
    int cityPop;
    PopulationReport()
    {

    }
    PopulationReport(String name, int pop)
    {
        this.name = name;
        this.pop = pop;
    }
    PopulationReport(String name, int pop, int cityPop)
    {
        this.name = name;
        this.pop = pop;
        this.cityPop = cityPop;
    }
    public String conntents()
    {
        return "name: " + name + ", pop: " + pop + ", city population: " + cityPop;
    }
}
