package project.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Model.InHouse;
import project.Model.Inventory;
import project.Model.Outsourced;

/**
 * FXML Controller class
 *
 * @author Daniel Richardson
 */
public class AddPartsScreenController implements Initializable {

    //Radio Buttons
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    
    //Labels
    @FXML
    private Label labeVariation;
    
    //Text Fields
    @FXML
    private TextField partID;
    @FXML
    private TextField nameField;
    @FXML
    private TextField inventory;
    @FXML
    private TextField price;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private TextField machineIDField;
    
    //Control Buttons
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    
    //Initializers
    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partIDVar;

    //Main initialze method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDVar = Inventory.getPartIDCount();
    }    

    //This radio button(INHOUSE) asks for Machine ID
    @FXML
    private void inHouseRadioHandler(ActionEvent event) throws IOException {
        isOutsourced = false;
        labeVariation.setText("Machine ID");
        machineIDField.setPromptText("Machine ID");
        outsourcedRadio.setSelected(false);
    }

    //This radio button(OUTSOURCED) asks for Company Name
    @FXML
    private void outsourcedRadioHandler(ActionEvent event) throws IOException {
        isOutsourced = true;
        labeVariation.setText("Company Name");
        machineIDField.setPromptText("Company Name");
        inHouseRadio.setSelected(false);
    }

    //Save Button
    @FXML
    private void saveBtnHandler(ActionEvent event) throws IOException {
        //Checks if fields are valid entries
        if (isPartValid()) {
            //Adds part to Data model (inhouse radio is default selection)
            if(isOutsourced == false) {
            InHouse inPart = new InHouse();
            inPart.setPartID(Integer.parseInt(partID.getText()));
            inPart.setName(nameField.getText());
            inPart.setPrice(Double.parseDouble(price.getText()));
            inPart.setInStock(Integer.parseInt(inventory.getText()));
            inPart.setMin(Integer.parseInt(minField.getText()));
            inPart.setMax(Integer.parseInt(maxField.getText()));
            inPart.setMachineID(Integer.parseInt(machineIDField.getText()));
            Inventory.addPart(inPart);
            //Switches back to Main Screen only if part is valid
                Parent addPartSaved = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSaved);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
        //Adds part to Data model if outsourced == true
        } else {
            Outsourced outPart = new Outsourced();
            outPart.setPartID(Integer.parseInt(partID.getText()));
            outPart.setName(nameField.getText());
            outPart.setPrice(Double.parseDouble(price.getText()));
            outPart.setInStock(Integer.parseInt(inventory.getText()));
            outPart.setMin(Integer.parseInt(minField.getText()));
            outPart.setMax(Integer.parseInt(maxField.getText()));
            outPart.setCompanyName(machineIDField.getText());
            Inventory.addPart(outPart);
            //Switches back to Main Screen only if part is valid
                Parent addPartSaved = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSaved);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
    }
    
    //Checks Textfields for valid entries and displays an error message
    private boolean isPartValid() {
        String partIDValid = partID.getText();
        String name = nameField.getText();
        String inStock = inventory.getText();
        String cost = price.getText();
        String min = minField.getText();
        String max = maxField.getText();
        String companyMachine = machineIDField.getText();
        String errorMessage = "";
        //Checks PartID
        if (partIDValid == null || partIDValid.length() == 0) {
            errorMessage += "Not a valid part name!\n"; 
        } else {
            try {
                Integer.parseInt(partIDValid);
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Part ID (Must be a number)!\n";
            }
        }
        //Checks Name
        if (name == null || name.length() == 0) {
            errorMessage += "Not a valid part name!\n"; 
        }
        //Checks Inventory
        if (inStock == null || inStock.length() == 0) {
            errorMessage += "Not a valid Inventory value!\n";  
        } else {
            try {
                int inStockComp = Integer.parseInt(inStock);
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (inStockComp < minComp || inStockComp > maxComp) {
                errorMessage += "Inventory must be between the minimum or maximum value!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Inventory value (Must be a number)!\n"; 
            }
        }
        //Checks Price/Cost
        if (cost == null || cost.length() == 0) {
            errorMessage += "Not a valid price!\n"; 
        } else {
            try {
                Double.parseDouble(cost);
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Price (Number must include a decimal value)!\n"; 
            }
        }
        //Checks Minimum Value
        if (min == null || min.length() == 0) {
            errorMessage += "Not a valid Minimum value!\n"; 
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Minimum value (Must be a number)!\n"; 
            }
        }        
        //Checks Maximum Value
        if (max == null || max.length() == 0) {
            errorMessage += "Not a valid Maximum value!\n"; 
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid Max value! (Must be a number)\n"; 
            }
        }
        //Checks Machine ID & Company Name
        if (companyMachine == null || companyMachine.length() == 0) {
            errorMessage += "Not a valid Machine ID or Company Name!\n"; 
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //Displays error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    //Go Back
    @FXML
    private void cancelBtnHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }
}
