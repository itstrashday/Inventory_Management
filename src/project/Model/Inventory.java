package project.Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daniel Richardson
 */
public class Inventory {
    
    public Part part;
    public Product product;
    public InHouse inHouse;
    public Outsourced outsourced;
    
    //Parts List
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
 
    //Products List
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static int productIDCount = 0;
    
    public static ObservableList<Product> getProducts() {
        return products;
    }  
    
    //Part methods
    public static void addPart(Part part) {
        allParts.add(part);
    }
    
    public static void deletePart(Part part) {
        allParts.remove(part);
    }
    public static boolean validatePartDelete(Part part) {
        boolean isFound = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getAssociatedPart().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }
    
    public static int lookupPart(String searchTerm) {
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
    
    public static void updatePart(int index, Part part){
        allParts.set(index, part);
    }
    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    
    
    //Products methods
    public static void addProduct(Product product){
        products.add(product);
    }
    
    public static void removeProduct(Product product){
        products.remove(product);
    }
    
    public static boolean validateProductDelete(Product product) {
        boolean isFound = false;
        int productID = product.getProductID();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getAssociatedPart().isEmpty()) {
                isFound = true;
            }
        }
        return isFound;
    }
    
    public static int lookupProduct(String searchTerm){
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < products.size(); i++) {
                if (Integer.parseInt(searchTerm) == products.get(i).getProductID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < products.size(); i++) {
                if (searchTerm.equals(products.get(i).getName())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No products found.");
            return -1;
        }
    }
    
    public static void updateProduct(int index, Product product){
        products.set(index, product);
    }
        
    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }
    
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
}



