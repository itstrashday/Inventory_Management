package project.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Daniel Richardson
 */
public class Outsourced extends Part {
    
    private final StringProperty companyName;
    
    public Outsourced() {
        super();
        companyName = new SimpleStringProperty();
    }

    //Getters/Setters
    public StringProperty getCompanyNameProperty() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    public String getCompanyName() {
        return companyName.get();
    }
}
