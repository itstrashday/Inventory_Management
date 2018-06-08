package project.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Model.Inventory;
import static project.Model.Inventory.getAllParts;
import static project.Model.Inventory.getProducts;
import project.Model.Part;
import project.Model.Product;
import static project.View_Controller.MainScreenController.productToModifyIndex;

/**
 * FXML Controller class
 *
 * @author Daniel Richardson
 */
public class ModifyProductsScreenController implements Initializable{

    //Text Fields
    @FXML
    private TextField searchPartsField;
    @FXML
    private TextField addProductID;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInventory;
    @FXML
    private TextField addProductPrice;
    @FXML
    private TextField addProductMax;
    @FXML
    private TextField addProductMin;

    //Parts Table.... Addable Parts
    @FXML
    private TableView<Part> tableView;
    @FXML
    private TableColumn<Part, Integer> addProdIDColumn;
    @FXML
    private TableColumn<Part, String> addProdNameColumn;
    @FXML
    private TableColumn<Part, Integer> addProdINVColumn;
    @FXML
    private TableColumn<Part, Double> addProdPriceColumn;

    //Products Table.... Added Parts
    @FXML
    private TableView<Part> tableView2;
    @FXML
    private TableColumn<Part, Integer> addProdIDColumn2;
    @FXML
    private TableColumn<Part, String> addProdNameColumn2;
    @FXML
    private TableColumn<Part, Integer> addProdINVColumn2;
    @FXML
    private TableColumn<Part, Double> addProdPriceColumn2;

    //Buttons
    @FXML
    private Button searchBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button addBtn;
    
    //Initializers
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private ObservableList<Part> tempPartList = FXCollections.observableArrayList();
    private Product newProduct = new Product();
    private final String exceptionMessage = new String();
    private int productID;
    int productIndex = productToModifyIndex();
        
    @FXML
    public void updateAddPartTableView() {
        tableView.setItems(getAllParts());
    }
    
    @FXML
    public void updateProductTableView() {
        tableView2.setItems(currentParts);
    }
    
    //Main initialize method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProducts().get(productIndex);
        addProductName.setText(product.getName());
        addProductInventory.setText(Integer.toString(product.getInStock()));
        addProductPrice.setText(Double.toString(product.getPrice()));
        addProductMax.setText(Integer.toString(product.getMax()));
        addProductMin.setText(Integer.toString(product.getMin()));
        
        //Top Table (Addable Parts)
        addProdIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProdNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addProdINVColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        addProdPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
            this.updateAddPartTableView();
        
        
        //Bottom Table (Parts added to Product)
        addProdIDColumn2.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProdNameColumn2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addProdINVColumn2.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        addProdPriceColumn2.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
            this.updateProductTableView();
        
        productID = Inventory.getProductIDCount();
    }  
    
    //Search Button
    @FXML
    void searchBtnHandler(ActionEvent event) {
        String searchPart = searchPartsField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getAllParts().get(partIndex);
            tempPartList.add(tempPart);
            tableView.setItems(tempPartList);
        }
        searchPartsField.clear();
    }

    //Add Button
    @FXML
    void addBtnHandler(ActionEvent event) {
        Part part = (Part) tableView.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Deletion Error");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a part to be added.");
            alert2.showAndWait();
        } else {    
            currentParts.add(part);
        }
    }

    //Delete Button
    @FXML
    void deleteBtnHandler(ActionEvent event) {
        Part part = tableView2.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        if(part == null) {
            alert2.initModality(Modality.NONE);
            alert2.setTitle("Part Delection");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a part to remove from product.");
            alert2.showAndWait();
        } else {
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want to delete " + part.getName() + " from product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                currentParts.remove(part);
            }
        }
    }

    //Save Button
    @FXML
    void saveBtnHandler(ActionEvent event) throws IOException {
        if(isProductValid()) {
            newProduct.setProductID(Integer.parseInt(addProductID.getText()));
            newProduct.setName(addProductName.getText());
            newProduct.setInStock(Integer.parseInt(addProductInventory.getText()));
            newProduct.setPrice(Double.parseDouble(addProductPrice.getText()));
            newProduct.setMin(Integer.parseInt(addProductMin.getText()));
            newProduct.setMax(Integer.parseInt(addProductMax.getText()));
            tableView.setItems(currentParts);
            Inventory.addProduct(newProduct);
                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
        }
    }
    
    //Checks Textfields for valid entries and displays an error message
    private boolean isProductValid() {
        String name = addProductName.getText();
        String inStock = addProductInventory.getText();
        String price = addProductPrice.getText();
        String min = addProductMin.getText();
        String max = addProductMax.getText();
        int partSize = tableView2.getItems().size();
        ArrayList<Part> parts = new ArrayList<>();
        parts.addAll(tableView2.getItems());
   
        String errorMessage = "";
        if (name == null || name.length() == 0) {
            errorMessage += "Not a valid product name!\n"; 
        }
        if (inStock == null || inStock.length() == 0) {
            addProductInventory.setText(Integer.toString(0));
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
                errorMessage += "Not a valid Inventory value!\n"; 
            }
        }
        if (price == null || price.length() == 0) {
            errorMessage += "Not a valid price!\n"; 
        } else {
            try {
                double productPrice = Double.parseDouble(price);
                double partsPrice = 0;
                for (Part part : parts) {
                partsPrice = partsPrice + part.getPrice();
                }

                if (partsPrice > productPrice) {
                errorMessage += "Product price must be higher than the sum of its parts!\n";
                }
                
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Price!\n"; 
            }
        }
        if (min == null || min.length() == 0) {
            errorMessage += "Not a valid Min value!\n"; 
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Min value!\n"; 
            }
        }        
        if (max == null || max.length() == 0) {
            errorMessage += "Not a valid Max value!\n"; 
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid Max value!\n"; 
            }
        } 
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
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
    void cancelBtnHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying current product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
}