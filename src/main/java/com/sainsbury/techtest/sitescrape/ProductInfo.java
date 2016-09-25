package com.sainsbury.techtest.sitescrape;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProductInfo Class
 * 
 * Describes the individual Product.
 * 
 * Comprises a number of setter and getter methods and also a method for converting
 * a ProductInfo class object to a JSONObject.
 * 
 * @author John Moores
 */

public class ProductInfo {
    private String title;
    private String description;
    
    // NOTE: Size (size) and Unit Price (unitPrice) are specified as string (String) since this helps with
    //       handling decimal place precision. There are two issues:
    //       1. The use of float found to be difficult to ensure required decimal places (1 for size and 2 for Unit Price).
    //       2. Loading JSONObject with a numeric (e.g. java.math.BigDecimal) means that where zero is in the least
    //          significant decimal place, that decimal place will be chopped, which for GBP currency is wrong.
    //          i.e. 15.10 becomes 15.1
    private String size;
    private String unitPrice;

    public ProductInfo(String title, String size, String unitPrice, String description) {
        this.title = title;
        this.size = size;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    /**
     *  Accessor Methods
     */     
    
    /**
     * Creates a JSONObject from the ProductInfo object.
     * 
     * @return JSONObject containing all the fields of the ProductInfo object.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject job = new JSONObject();
        job.put("title", title);
        job.put("size", size);
        job.put("unit_price", unitPrice);
        job.put("description", description);
        
        return job;
    }
       
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductInfo{" + "title=" + title + ", size=" + size + ", unitPrice=" + unitPrice + ", description=" + description + '}';
    }
    
}