package com.napier.sem;

import com.napier.sem.BusinessLayer.BusinessLayer;
import com.napier.sem.DataLayer.Column;
import com.napier.sem.DataLayer.PopulationReport;
import com.napier.sem.DataLayer.SQLConnection;

import javax.sound.midi.SysexMessage;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.ArrayList;

/**
 * This will evetualy aid in the conquest of the world through allowing the user<br>to view the populations of the world as wer as the number and percentage of people speaking certain languages<br>within specfied area(s)/location types
 * @author Oliwier, Arnas but NOT PETER
 * @version 03/02/2020
 */
public class App {
    private static BusinessLayer bl = null;
    public static void main(String[] args)
    {
        //Initial app code
        System.out.println("Hello Kevin this is group 25!");
        bl = new BusinessLayer();
        //@TODO add code here

        read(bl.getPopAdv(Column.REGION, "Eastern Europe"));



        //Tells the business layer to pack up and go home.
        bl.end();
        bl = null;
    }

    /**
     * Prints out the passed in string as a grid
     * @param display - The 2d array that is meant to be displayed
     */
    private static void read(String[][] display) {
        //for each row
        for (String[] strings : display) {
            //for each block in display
            for (String string : strings) {
                System.out.print(equlize(string, 25));
            }
            System.out.println();
        }
    }

    /**
     * Spams spaces onto a string unil it is of the specified length
     * @param string - the string that is to be extended in space
     * @param length - the required length for the string
     * @return passed in string with atleast the specifed length (Terms and conditions apply)
     */
    private static String equlize(String string, int length) {
        if (string.length() < length) {
            return equlize( string + " ", length);
        }
        else {
            return string;
        }
    }
}
