/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Alex Rueb and Nick Tonjum
 * Created Spring 2019
 * This program attempts to decrypt credit card information in track 1 of the magnetic strip
 * by first taking in a file and parsing it into different strings, then checking each string.
 * Each string must have certain qualities such as starting with %B and ending with ?; as well as
 * others and this program checks the strings. If the strings work, it prints the information.
 */
public class Security2 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // Take in string
        String input = "src/input.txt";
        String dump = "";
        try (BufferedReader b = new BufferedReader(new FileReader(input))) {
            String line = b.readLine();
            while (line != null) {
                dump = dump.concat(line);
                line = b.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        // Split into array of strings separated by '%B'
        String[] myStrings = dump.split("%B");
        
        // Loop through all strings and check each for correct format
        int counter = 0;

        // Run through the entire string of each string
        for (int i = 1; i < myStrings.length; i++) {
            
            // Create variables for each object we want printed
            String target = myStrings[i];
            String cardNum;
            String year;
            String month;
            String cvv;
            
            // Take the first 16 characters after the %B
            String chopped = target.substring(0, 16);
            
            // Check if the first 16 characters are integers
            if (chopped.matches("\\d{16}")) {
                cardNum = chopped;
                
                // Check if the 16th character is a ^
                if (target.indexOf("^") == 16) {
                    
                    // If there is a ^, continue until there is another ^
                    if (target.indexOf("^", 17) != -1) {
                        int next = target.indexOf("^", 17);
                        String threeChop = target.substring(17, next);
                        
                        // Check if the characters between 2 ^'s are characters and not integers
                        if (threeChop.matches("([a-zA-Z])+/([a-zA-Z])+")) {
                            String[] fullName = threeChop.split("/");
                            
                            // Take the next 34 characters in the string which should be all integers
                            String secChop = target.substring(next + 1, next + 34);
                            
                            // Check to make sure the 34th character in secChop is a ?
                            if (target.indexOf("?") == next + 34) {
                                
                                // If all characters are integers in secChop, continue
                                if (secChop.matches("\\d{33}")) {
                                    counter++;
                                    System.out.println("");
                                    
                                    // assign variables to sections in string secChop
                                    year = secChop.substring(0, 2);
                                    month = secChop.substring(2, 4);
                                    cvv = secChop.substring(4, 7);
                                    
                                    // Print out all information
                                    System.out.println("Information for Track Record: " + counter);
                                    System.out.println("Cardholder's Name: " + fullName[0] + fullName[1]);
                                    System.out.println("Card Number: " + cardNum);
                                    System.out.println("Expiration Date: " + month + "/" + year);
                                    System.out.println("CVC Number: " + cvv);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
