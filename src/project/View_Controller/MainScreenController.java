package project.View_Controller;

import java.io.IOException;
import java.net.URL;
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
import static project.Model.Inventory.deletePart;
import static project.Model.Inventory.getAllParts;
import static project.Model.Inventory.getProducts;
import static project.Model.Inventory.removeProduct;
import static project.Model.Inventory.validatePartDelete;
import static project.Model.Inventory.validateProductDelete;
import project.Model.Part;
import project.Model.Product;

/**
 * FXML Controller class
 *
 * @author Daniel Richardson
 */
public class MainScreenController implements Initializable {
            
    //Parts Buttons
    @FXML
    private Button searchPartsBtn;
    @FXML
    private Button addPartsBtn;
    @FXML
    private Button modifyPartsBtn;
    @FXML
    private Button deletePartsBtn;
    
    //Text Fields
    @FXML
    private TextField searchPartsField;
    @FXML
    private TextField searchProductsField;
    
    //Parts Table & Columns
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partINV;
    @FXML
    private TableColumn<Part, Double> partCost;
    
    //Products Buttons
    @FXML
    private Button searchProductsBtn;
    @FXML
    private Button addProductsBtn;
    @FXML
    private Button modifyProductsBtn;
    @FXML
    private Button deleteProductsBtn;
    
    //Products Table & Columns
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productINV;
    @FXML
    private TableColumn<Product, Double> productCost;
    
    //Exit Button
    @FXML
    private Button exitBtn;
    
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    //Main initialize method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize the PARTS table with appropriate columns
        partID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partINV.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partCost.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
            this.updatePartsTableView();
        
        productID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        productName.setCellValueFactory(cellData -> cellData.getValue().NameProperty());
        productINV.setCellValueFactory(cellData -> cellData.getValue().InStockProperty().asObject());
        productCost.setCellValueFactory(cellData -> cellData.getValue().PriceProperty().asObject());
            this.updateProductsTableView();
    }
 
    //Search Parts Button
    @FXML
    private void searchPartsBtnHandler(ActionEvent event) {
        String searchPart = searchPartsField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getAllParts().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            partsTable.setItems(tempPartList);
        }
        searchPartsField.clear();
    }

    //Add Parts Button
    @FXML
    private void addPartsBtnHandler(ActionEvent event) throws IOException {
        Stage stage; 
        Parent root;       
        stage=(Stage) addPartsBtn.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
            "AddPartsScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    //Modify Parts Button
    @FXML
    private void modifyPartsBtnHandler(ActionEvent event) throws IOException {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Deletion Error");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a part to be modified.");
            alert2.showAndWait();
        } else {
            modifyPart = partsTable.getSelectionModel().getSelectedItem();
            modifyPartIndex = getAllParts().indexOf(modifyPart);
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyPartsScreen.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        }           
    }

    //Delete Parts Button
    @FXML
    private void deletePartsBtnHandler(ActionEvent event) {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Deletion Error");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a part to be deleted.");
            alert2.showAndWait();
        }
        else if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getName() + " was removed.");
            }
            else {
                System.out.println("Part " + part.getName() + " was not removed.");
            }
        }
    }
    
    //Update parts table view
    public void updatePartsTableView() {
        partsTable.setItems(getAllParts());
    }

    //Search Products Button
    @FXML
    private void searchProductsBtnHandler(ActionEvent event) {
        String searchProduct = searchProductsField.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any known products.");
            alert.showAndWait();
        }
        else {
            prodIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getProducts().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            productsTable.setItems(tempProductList);
        }
        searchProductsField.clear();
    }

    //Add Button
    @FXML
    private void addProductsBtnHandler(ActionEvent event) throws IOException {
        Stage stage; 
        Parent root;       
        stage=(Stage) addProductsBtn.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
               "AddProductScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Modify Button
    @FXML
    private void modifyProductsBtnHandler(ActionEvent event) throws IOException {
        Product product = productsTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Deletion Error");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a product to be modified.");
            alert2.showAndWait();
        } else {
            modifyProduct = productsTable.getSelectionModel().getSelectedItem();
            modifyProductIndex = getProducts().indexOf(modifyProduct);
            Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProductsScreen.fxml"));
            Scene modifyProductScene = new Scene(modifyProductParent);
            Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyProductStage.setScene(modifyProductScene);
            modifyProductStage.show();
        }
    }

    //Delete Button
    @FXML
    private void deleteProductsBtnHandler(ActionEvent event) {
        Product product = productsTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Deletion Error");
            alert2.setHeaderText("Nothing Selected");
            alert2.setContentText("Please select a product to be deleted.");
            alert2.showAndWait();
        }
        else if (validateProductDelete(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product cannot be deleted!");
            alert.setContentText("Product contains one or more parts.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                removeProduct(product);
                updateProductsTableView();
                System.out.println("Product " + product.getName() + " was removed.");
            } else {
                System.out.println("Product " + product.getName() + " was removed.");
            }
        }
    }
    
    //Update products table view
    public void updateProductsTableView() {
        productsTable.setItems(getProducts());
    }
    
    //Exit Button Handler... Closes Application
    @FXML
    private void exitBtnHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?\n All data will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }  
}
