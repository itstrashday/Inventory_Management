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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Model.InHouse;
import project.Model.Inventory;
import static project.Model.Inventory.getAllParts;
import project.Model.Outsourced;
import project.Model.Part;
import static project.View_Controller.MainScreenController.partToModifyIndex;

/**
 * FXML Controller class
 *
 * @author Daniel Richardson
 */
public class ModifyPartsScreenController implements Initializable {
    
    //Labels
    @FXML
    private Label labelVariation;

    //Radio Buttons
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    
    //Text Fields
    @FXML
    private TextField modifyName;
    @FXML
    private TextField modifyINV;
    @FXML
    private TextField modifyPrice;
    @FXML
    private TextField modifyMax;
    @FXML
    private TextField modifyMin;
    @FXML
    private TextField modifyMachineID;
    @FXML
    private TextField modifyID;
    
    //Buttons
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    
    //Initializers
    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getAllParts().get(partIndex);
        partID = getAllParts().get(partIndex).getPartID();
        modifyName.setText(part.getName());
        modifyINV.setText(Integer.toString(part.getInStock()));
        modifyPrice.setText(Double.toString(part.getPrice()));
        modifyMax.setText(Integer.toString(part.getMax()));
        modifyMin.setText(Integer.toString(part.getMin()));
        if (part instanceof InHouse) {
            labelVariation.setText("Machine ID");
            inHouseRadio.setSelected(true);
        }
        else {
            labelVariation.setText("Company Name");
            outsourcedRadio.setSelected(true);
        }
    }
         
    //This radio button(INHOUSE) asks for Machine ID
    @FXML
    private void inHouseRadioHandler(ActionEvent event) throws IOException {
        
        isOutsourced = false;
        outsourcedRadio.setSelected(false);
        labelVariation.setText("Machine ID");
        modifyMachineID.setText("");
        modifyMachineID.setPromptText("Machine ID");
    }

    //This radio button(OUTSOURCED) asks for Company Name
    @FXML
    private void outsourcedRadioHandler(ActionEvent event) throws IOException {
        
        isOutsourced = true;
        inHouseRadio.setSelected(false);
        labelVariation.setText("Company Name");
        modifyMachineID.setText("");
        modifyMachineID.setPromptText("Company Name");
    }

    //Save Button
    @FXML
    private void saveBtnHandler(ActionEvent event) throws IOException {
        if (isPartValid()) {
            //Adds part to Data model (inhouse radio is default selection)
            if(isOutsourced == false) {
            InHouse inPart = new InHouse();
            inPart.setPartID(Integer.parseInt(modifyID.getText()));
            inPart.setName(modifyName.getText());
            inPart.setPrice(Double.parseDouble(modifyPrice.getText()));
            inPart.setInStock(Integer.parseInt(modifyINV.getText()));
            inPart.setMin(Integer.parseInt(modifyMin.getText()));
            inPart.setMax(Integer.parseInt(modifyMax.getText()));
            inPart.setMachineID(Integer.parseInt(modifyMachineID.getText()));
            Inventory.addPart(inPart);
            //Switches back to Main Screen only if part is valid and saved
                Parent addPartSaved = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSaved);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
        //Adds part to Data model if outsourced == true
        } else {
            Outsourced outPart = new Outsourced();
            outPart.setPartID(Integer.parseInt(modifyID.getText()));
            outPart.setName(modifyName.getText());
            outPart.setPrice(Double.parseDouble(modifyPrice.getText()));
            outPart.setInStock(Integer.parseInt(modifyINV.getText()));
            outPart.setMin(Integer.parseInt(modifyMin.getText()));
            outPart.setMax(Integer.parseInt(modifyMax.getText()));
            outPart.setCompanyName(modifyMachineID.getText());
            Inventory.addPart(outPart);
            //Switches back to Main Screen only if partis valid and saved
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
        String partIDValid = modifyID.getText();
        String name = modifyName.getText();
        String inStock = modifyINV.getText();
        String cost = modifyPrice.getText();
        String min = modifyMin.getText();
        String max = modifyMax.getText();
        String companyMachine = modifyMachineID.getText();
        String errorMessage = "";
        //Checks PartID
        if (partIDValid == null || partIDValid.length() == 0) {
            errorMessage += "Not a valid part number!\n"; 
        } else {
            try {
                Integer.parseInt(partIDValid);
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Part ID!\n";
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
        alert.setContentText("Are you sure you want to cancel modifying the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
}
