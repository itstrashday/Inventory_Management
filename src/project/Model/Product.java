package project.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static project.Model.Inventory.allParts;
import static project.Model.Inventory.isInteger;

/**
 *
 * @author Daniel Richardson
 */
public class Product {
    
    public static ObservableList<Part> associatedParts=FXCollections.observableArrayList();

    private final SimpleIntegerProperty ProductID;
    private final SimpleStringProperty Name;
    private final SimpleDoubleProperty Price;
    private final SimpleIntegerProperty InStock;
    private final SimpleIntegerProperty Max;
    private final SimpleIntegerProperty Min;
    
    //Constructor
   public Product() {
        ProductID = new SimpleIntegerProperty();
        Name = new SimpleStringProperty();
        Price = new SimpleDoubleProperty();
        InStock = new SimpleIntegerProperty();
        Min = new SimpleIntegerProperty();
        Max = new SimpleIntegerProperty();
    }
    
    //ID
    public int getProductID() {
        return ProductID.get();
    }
    public void setProductID(int partID) {
        this.ProductID.set(partID);
    }    
    public IntegerProperty productIDProperty(){
        return ProductID;
    }

    //Name
    public StringProperty NameProperty() {
        return Name;
    }
    public void setName(String name) {
        this.Name.set(name);
    }    
    public String getName() {
        return Name.get();
    }

    //InStock
    public IntegerProperty InStockProperty() {
        return InStock;
    }
    public void setInStock(int inStock) {
        this.InStock.set(inStock);
    }
    public int getInStock() {
        return InStock.get();
    }

    //Price
    public DoubleProperty PriceProperty() {
        return Price;
    }
    public void setPrice(double price) {
        this.Price.set(price);
    }
    public double getPrice() {
        return Price.get();
    }

    //Max
    public IntegerProperty MaxProperty() {
        return Max;
    }
    public void setMax(int max) {
        this.Max.set(max);
    }
    public int getMax() {
        return Max.get();
    }

    //Min
    public IntegerProperty MinProperty() {
        return Min;
    }
    public void setMin(int min) {
        this.Min.set(min);
    }
    public int getMin() {
        return Min.get();
    }
    
    public ObservableList getAssociatedPart() {
        return associatedParts;
    }
    
    public void addAssociatedPart(ObservableList<Part> associatedParts){
        Product.associatedParts = associatedParts;
    }
    
    public boolean removeAssociatedPart(){
        return false;
    }
    
    public static int lookupAssociatedPart(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < allParts.size(); i++) {
                if (Integer.parseInt(searchTerm) == allParts.get(i).getPartID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < allParts.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(allParts.get(i).getName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            return -1;
        }
    }
}
