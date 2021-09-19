package su11;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/**
 * The CookieInventoryFile class models the collection of records (modeled by
 * CookieInventoryItem) in the cookies.txt file
 */
public class CookieInventoryFile extends ArrayList<CookieInventoryItem> {

    /**
     * default constructor 
     */
    public CookieInventoryFile() {

    }

    /**
     * constructor, load cookies from file and add to list
     * @param file input file
     * @throws java.io.FileNotFoundException if file not found
     */
    public CookieInventoryFile(File file) throws FileNotFoundException {
        loadFromFile(file);
    }

    /**
     * searches the CookieInventoryFile elements for a CookieInventoryItem with
     * the same ID
     * @param flavourId cookie flavour ID number
     * @return If an item is found, that CookieInventoryItem is returned. If
     * there is no item with that flavour ID, a null object (null) is returned
     */
    public CookieInventoryItem find(int flavourId) {

        //iterate the list and find cookie item
        for (CookieInventoryItem item : this) {

            //compare by flavourid
            if (item.cookie.getId() == flavourId) {
                return item;
            }
        }

        //not found
        return null;
    }

    /**
     * read all the records from the file and construct a CookieInventoryItem
     * object for each record. Each CookieInventoryItem object is added to the
     * ArrayList inside the CookieInventoryFile
     * format: flavour ID number | quantity baked
     * @param file File object
     * @throws java.io.FileNotFoundException if could not write to file
     */
    public void loadFromFile(File file) throws FileNotFoundException {

        //open file to read
        Scanner fileScanner = new Scanner(file);

        //read line by line until end of file
        while (fileScanner.hasNext()) {

            //read a line
            String line = fileScanner.nextLine();

            //parse by |
            String[] data = line.split("\\|");

            //flavour ID number
            int flavourID = Integer.parseInt(data[0]);

            //quantity baked
            int quantityBaked = Integer.parseInt(data[1]);

            //add to array
            add(new CookieInventoryItem(Cookies.getCookie(flavourID),
                    quantityBaked));
        }

        //close file
        fileScanner.close();
    }

    /**
     * iterates through all of the CookieInventoryItem objects in the ArrayList
     * and deconstructs them into a delimited string that can be written to the
     * file. Use the toFileString() method in the CookieInventoryItem class
     * @param file File object
     * @throws java.io.IOException I/O error
     */
    public void writeToFile(File file) throws IOException {

        //create object to write file
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        //iterate the list, convert each item to string and write to file
        for (CookieInventoryItem item : this) {
            output.write(item.toFileString());
            output.write("\n"); //write a new line
        }

        //close file
        output.close();

    }
}
