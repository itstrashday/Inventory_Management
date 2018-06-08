package project.Model;

import javafx.beans.property.*;

/**
 *
 * @author Daniel Richardson
 */
public abstract class Part {
    
    private final IntegerProperty partID;
    private final StringProperty name;
    private final IntegerProperty inStock;
    private final DoubleProperty price;
    private final IntegerProperty max;
    private final IntegerProperty min;

    //Constructor
    public Part() {
        this.partID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.inStock = new SimpleIntegerProperty();
        this.price = new SimpleDoubleProperty();
        this.max = new SimpleIntegerProperty();
        this.min = new SimpleIntegerProperty();
    }

    //ID
    public int getPartID() {
        return partID.get();
    }
    public void setPartID(int partID) {
        this.partID.set(partID);
    }    
    public IntegerProperty partIDProperty(){
        return partID;
    }

    //Name
    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }    
    public String getName() {
        return name.get();
    }

    //InStock
    public IntegerProperty inStockProperty() {
        return inStock;
    }
    public void setInStock(int inStock) {
        this.inStock.set(inStock);
    }
    public int getInStock() {
        return inStock.get();
    }

    //Price
    public DoubleProperty priceProperty() {
        return price;
    }
    public void setPrice(double price) {
        this.price.set(price);
    }
    public double getPrice() {
        return price.get();
    }

    //Max
    public IntegerProperty maxProperty() {
        return max;
    }
    public void setMax(int max) {
        this.max.set(max);
    }
    public int getMax() {
        return max.get();
    }

    //Min
    public IntegerProperty minProperty() {
        return min;
    }
    public void setMin(int min) {
        this.min.set(min);
    }
    public int getMin() {
        return min.get();
    }
}    