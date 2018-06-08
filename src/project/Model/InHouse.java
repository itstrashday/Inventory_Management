package project.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Daniel Richardson
 */
public class InHouse extends Part {

    private final IntegerProperty machineID;

    public InHouse() {
        super();
        machineID = new SimpleIntegerProperty();
    }
    
    public InHouse (int partID, String name, int inStock, double price, int max, int min) {
        this.machineID = new SimpleIntegerProperty();
    }

    //Getters/Setters
    public IntegerProperty getMachineIDProperty() {
        return machineID;
    }
    public void setMachineID(int machineID) {
        this.machineID.set(machineID);
    }
    public int getMachineID() {
        return machineID.get();
    }  
}
