package su11;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/**
 * FXML Controller class
 *
 * @author Ya Su
 */
public class CookiesFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtQtySold, txtQtyBaked;
    @FXML
    private Button btnSell, btnAdd, btnExit;
    @FXML
    private ComboBox<Cookies> ddlCookies;
    
    ObservableList<Cookies> obsCookies;

    /**
     * CookieInventoryFile object load, save to file
     */
    private CookieInventoryFile cookieInventoryFile;

    /**
     * sell handler update the quality the method must validate and check the
     * available quantity
     */
    public void SellButton() {

        //error
        boolean hasError = false;
        String message = "";//error/sucess message

        //validate empty text box
        if (txtQtySold.getText().equals("")) {

            hasError = true;
            message = "Please enter the number of cookies sold.";
        } else {

            //read quantity
            int quantity = 0;

            try {
                //read quantity
                quantity = Integer.parseInt(txtQtySold.getText());

                if (quantity <= 0) {
                    hasError = true;
                    message = "You must enter a quantity that is greater than 0.";
                }

            } catch (NumberFormatException e) {
                hasError = true;
                message = "You must enter a valid numberic value.";
            }

            if (!hasError) {

                //get chosen cookie
                Cookies cookies = ddlCookies.getValue();

                //check inventory
                CookieInventoryItem item = cookieInventoryFile.find(cookies.getId());

                //check enough quantity
                if (item == null) {
                    hasError = true;
                    message = "Sorry, not enough " + cookies.getName()
                            + " cookies to sell. You only have 0 left.";
                } else if (item.getQuantity() < quantity) {
                    hasError = true;
                    message = "Sorry, not enough " + cookies.getName()
                            + " cookies to sell. You only have " + item.getQuantity()
                            + " left.";
                } else {

                    //remaning quantity
                    int remainingQuantity = item.getQuantity() - quantity;

                    if (remainingQuantity == 0) { //out of stock
                        cookieInventoryFile.remove(item);
                    } else {
                        item.setQuantity(item.getQuantity() - quantity);
                    }

                    message = cookies.getName()
                            + " cookies has been sold sucessfullyto sell. You have " + remainingQuantity
                            + " left.";

                    txtQtySold.setText(""); //clear input
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        //show error if any
        if (hasError) {
            //error            
            alert.setTitle("Data Entry Error");

        } else {
            alert.setTitle("Sold successfuly");
        }

        alert.setHeaderText(message);
        alert.showAndWait();

    }

    /**
     * add quantity to item
     */
    public void AddButton() {

        //error
        boolean hasError = false;
        String message = "";//error/sucess message

        //validate empty text box
        if (txtQtyBaked.getText().equals("")) {

            hasError = true;
            message = "Please enter the number of cookies baked.";
        } else {

            //read quantity
            int quantity = 0;

            try {
                //read quantity
                quantity = Integer.parseInt(txtQtyBaked.getText());

                if (quantity <= 0) {
                    hasError = true;
                    message = "You must enter a quantity that is greater than 0.";
                }

            } catch (NumberFormatException e) {
                hasError = true;
                message = "You must enter a valid numberic value.";
            }

            if (!hasError) {

                //get chosen cookie
                Cookies cookies = ddlCookies.getValue();

                //check inventory
                CookieInventoryItem item = cookieInventoryFile.find(cookies.getId());

                //check enough quantity
                if (item == null) {
                    item = new CookieInventoryItem(cookies, quantity);
                    cookieInventoryFile.add(item);
                } else {
                    item.setQuantity(item.getQuantity() + quantity);
                }

                message = cookies.getName()
                        + " cookies has been baked sucessfully. You have " + item.getQuantity()
                        + ".";
                txtQtyBaked.setText(""); //clear input
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        //show error if any
        if (hasError) {
            //error            
            alert.setTitle("Data Entry Error");

        } else {
            alert.setTitle("Baked successfuly");
        }

        alert.setHeaderText(message);
        alert.showAndWait();

    }


    public void exitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Are you sure you wish to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            
            try {
                cookieInventoryFile.writeToFile(new File("cookies.txt"));

            } catch (IOException ex) {

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cookie Inventory");
                alert.setHeaderText("Could not save to file!");

                alert.showAndWait();

            }
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //add list of cookies to combo boxes
        obsCookies = FXCollections.observableArrayList();

        for (Cookies cookie : Cookies.values()) {
            obsCookies.add(cookie);
        }
        ddlCookies.setItems(obsCookies);
        //ddlCookies.getSelectionModel().selectFirst();

        try {
            //Connect an input stream to the cookies.dat file and read in the records
            cookieInventoryFile = new CookieInventoryFile(new File("cookies.txt"));
        } catch (FileNotFoundException ex) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cookie Inventory");
            alert.setHeaderText("Cookie file not found!");

            alert.showAndWait();

            //file not found, create empty CookieInventoryFile
            cookieInventoryFile = new CookieInventoryFile();
        }
    }

}
