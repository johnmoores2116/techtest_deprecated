package com.sainsbury.techtest.sitescrape;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * PageScraper Class
 * 
 * Used to consume the webpage and process the individual item contents into instances of the ProductInfo Class.
 * 
 * Comprises a scrape() method which works over the webpage for each of the items listed on the webpage,
 * following the individual product items' URL links. Individual instances of the ProductInfo class are created
 * using the createProductInfo() method. The ProductInfo objects are added to an instance of a JSONObject
 * together with a total summing all the unit prices and returned from the scrape method as a string (String object)
 * of JSON.
 * 
 * @author John Moores
 */
public class PageScraper {
    private URL url;
    
    // Initialised private class variables - Intialised to steady state values so all in one place
    // for ease of use. These values are used to locate items in the webpage using Selector CSS queries.
    private String productTitleDescriptionContainerLocator = "div.productTitleDescriptionContainer";
    private String productTitleElementTagLocator = "h1";
    private String productPricePerUnitLocator = "p.pricePerUnit";
    private String productTextDescriptionLocator = "div.productText";
    private String productListLocator = "ul.productLister";
    private String productListTag = "li";
    private String productInfoLocator = "div.productInfo";
    private String productInfoElementLocator = "a";
    
    // Class constructor - initialised with URL object specifying the webpage to be processed.
    public PageScraper(URL argUrl) {
        url = argUrl;
    }
    
    /**
     * Opens a HTTP(S) connection to the given URL argUrl of the current Product Item being processed and then
     * does the following:
     *     Obtains the Title for the Product Item. 
     *     Calculates the Size of the webpage describing the Product Item.
     *        Note: Handled as a string (String object) but uses java.math.BigDecimal to calculate Page Size.
     *              This combination used so that decimal place precision can be ensured. One decimal place used.   
     *     Obtains the Unit Price for the Product Item
     *         Note: Handles Unit Price as a string (String object) so that decimal place precision can be ensured.
     *               Two decimal places used.
     *     Obtains the Description of the Product Item.
     * 
     * @param argUrl A String object holding a page address of the product item.
     * @return ProductInfo object with data, or null if something went wrong.
     */
    public ProductInfo createProductInfo(String argUrl) {
        String title = "";

        // Page Size (size) specified as BigDecimal to ensure decimal place precision. One decimal
        // place with rounding up. If float is used, decimal place control is not possible / easy. 
        BigDecimal size = new BigDecimal("0.0").setScale(1, BigDecimal.ROUND_UP);

        // Unit Price (unitPrice) specified as string (String object) to ensure display of decimal
        // place precision is possible as in two decimal places for GBP currency. If float is used, decimal
        // place control is not possible / easy.
        // Subsequent totals calculation can be done using java.math.BigDecimal on the Unit Price (unitPrice)
        // String objects - Important that initialised to "0.00" so that the subsequent totals
        // calculation is safe. 
        String unitPrice = "0.00";
        String description = "";
        
        try {
        	// Obtain the webpage document from the connection to the specified url.
            Document doc = Jsoup.connect(argUrl).get();
            
            // Query (Selector CSS queries) the webpage document looking for the Title
            // Container using the contents of the productTitleDescriptionContainerLocator
            // class variable (String object).
            Element el = doc.select(productTitleDescriptionContainerLocator).first();

            if (el == null) {
            	// No Element object found on the webpage Document for the Title Container.
                return null;
            } else {
                // There is a Title Container - Get the Title Element using the contents of the
                // productTitleElementTagLocator class variable (String object)
                Element titleElement = el.getElementsByTag(productTitleElementTagLocator).first();
                
                // Get the Title Text.
                title = titleElement.text();
                
                // Get the size of the webpage in kilobytes (kb).
                // Method used, is to convert the Document object (doc) to a String object and then
                // use the String objects's length() method to give its size in bytes. Dividing by
                // 1024 will give the kb value. Use of BigDecimal ensures decimal place precision
                // (one decimal place).
                // NOTE: Assuming webpage will never be bigger than available memory or this could be
                // a problem.
                size = new BigDecimal(doc.toString().length() / 1024).setScale(1, BigDecimal.ROUND_UP);
            }
            
            // Query (Selector CSS queries) the webpage document to get the Unit Price Element of the
            // Product Item webpage for the contents of the productPricePerUnitLocator class variable
            // (String object).
            el = doc.select(productPricePerUnitLocator).first();

            if (el == null) {
            	// No Element object found on the webpage Document for the Unit Price.
                return null;
            } else {
                // There is a Unit Price - Get the Unit Price (unitPrice) from the element
            	// and place it in Working Storage for further manipulation.
                String unitPriceTxtWs = el.text();
                
                // Remove the "/unit" text from the Unit Price Text Working Storage.
                unitPriceTxtWs = unitPriceTxtWs.replace("/unit", "");
                
                // Remove the "£" text from the Unit Price Text Working Storage.
                unitPriceTxtWs = unitPriceTxtWs.replace("£", "");

                // Now have actual numeric value for Unit Price (unitPrice) in a string form.
                // Using string (String object) to ensure display of decimal place precision
                // is possible as in two decimal places for GBP currency.
                // If float is used, decimal place control is not possible / easy.
                if (unitPriceTxtWs != null && !unitPriceTxtWs.isEmpty() && unitPriceTxtWs.matches("^(-?0[.]\\d+)$|^(-?[0-9]+\\d*([.]\\d+)?)$|^0$")) {
                	unitPrice = unitPriceTxtWs;
                }
            }
                        
            // Query (Selector CSS queries) the webpage document to get the Description Element of the
            // Product Item webpage for the contents of the productTextDescriptionLocator class variable
            // (String object).
            el = doc.select(productTextDescriptionLocator).first();
            
            if (el == null) {
            	// No Element object found on the webpage Document for the Description.
                return null;
            } else {
                // There is a Description - Get the Description (description) from the element.
                description = el.text();
            }
        } catch (Exception ex) {
        	// Log Exception exception.
        	// But continue processing since the method will return an "empty" ProductInfo
        	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Instantiate and return a ProductInfo object.
        // Note: The page size (size) value (argument) is converted from java.math.BigDecimal to a string
        //       (String object) and appended with the "kb" units string. This is safe since the size
        //       (java.math.BigDecimal) value has previously been initialised with 0.0.
        return new ProductInfo(title, size.toString()+"kb", unitPrice, description);
        
    } // getProductInfo() method
        
    /**
     * Scrapes the webpage and produces the JSON output for the Product Items.
     * 
     * @return String of JSON.
     */   
    public String scrape() {
        JSONObject json = new JSONObject();
        JSONArray results = new JSONArray();

        // Price Total (priceTotal) specified as BigDecimal to ensure decimal place precision. Two decimal
        // places with rounding down - Hope that's okay for profits ;-). If float is used, decimal place
        // control is not possible / easy. 
        BigDecimal priceTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_DOWN); // total unit price.
        
        // Get the connection to the webpage using the URL.
        Connection con = Jsoup.connect(url.toString());
        
        if (con == null) {
            // If connection not possible, no need to continue. Return an empty JSON document.
            return "{}";
        }

        try {
            // Query (Selector CSS queries) the webpage document looking for the Product
        	// Item List using the contents of the productListLocator class variable
            // (String object).
            Element el = con.get().select(productListLocator).first();
            
            if (el == null) {
                // There is no List of Products, no need to continue. Return an empty JSON document. 
                return "{}";
            }
            
            // Get the available Elements from the webpage for the Product Items identified in the List.
            Elements els = el.getElementsByTag(productListTag);

            // Loop round for each of the Product Item Elements in the List.
            for (Element element: els) {
                // Query (Selector CSS queries) the Element to get the Product Info Element using the
            	// contents of the productInfoLocator class variable (String object).            	
                Element prodInfoEl = element.select(productInfoLocator).first();

                // Get the Link Element for the Product Item from the Product Info Element.
                Element linkEl = prodInfoEl.getElementsByTag(productInfoElementLocator).first();
                
                // System.out.println(linkel.attr("abs:href")); // if we need absolute URL
                // Get the URL for the specific Product Item.
                String prodInfoUrl = linkEl.attr("href");
                
                // Create an instance of the ProductInfo class using the URL for the specific Product Item.
                ProductInfo productInfo = createProductInfo(prodInfoUrl);
                
                // Add JSON representation of the ProductInfo object to the results array (JSONArray).
                results.put(productInfo.toJSON());
                
                // Accumulate the Price Total (priceTotal) using the Product Item's Unit Price from the current
                // ProductInfo class object.
                // The Unit Price (unitPrice) on the ProductInfo class is stored as a string (String object) for reasons
                // previously explained (decimal place precision). Consequently, a java.math.BigDecimal is created using the
                // the unitPrice String object to accumulate the Price Total (priceTotal) value.
                // Note: This is safe because the Unit Price (unitPrice) on the ProductInfo class is always initialised as
                // "0.00" - So an invalid number or null pointer will not be possible.
                priceTotal = priceTotal.add(new BigDecimal(productInfo.getUnitPrice()).setScale(2, BigDecimal.ROUND_DOWN));
            }
        } catch (HttpStatusException hse) {
        	// Log HttpStatusException exception.
        	// But continue processing since the method will return what we have in the form of a JSON string (String object)
        	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, "Error Processing Main Page with HttpStatusException", hse);
        }
          catch (MalformedURLException mue) {
          	// Log MalformedURLException exception.
          	// But continue processing since the method will return what we have in the form of a JSON string (String object)
          	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, "Error Processing Main Page with MalformedURLException", mue);
        }
          catch (UnsupportedMimeTypeException umte) {
            // Log UnsupportedMimeTypeException exception.
          	// But continue processing since the method will return what we have in the form of a JSON string (String object)
          	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, "Error Processing Main Page with UnsupportedMimeTypeException", umte);
        }
          catch (SocketTimeoutException ste) {
        	// Log SocketTimeoutException exception.
          	// But continue processing since the method will return what we have in the form of a JSON string (String object)
          	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, "Error Processing Main Page with SocketTimeoutException", ste);
        }
          catch (IOException ioe) {
          	// Log IOException exception.
          	// But continue processing since the method will return what we have in the form of a JSON string (String object)
          	// object - See below.
            Logger.getLogger(PageScraper.class.getName()).log(Level.SEVERE, "Error Processing Main Page with IOException", ioe);
        }
        
        // Add the total and results to the json (JSONObject).
        // The "total" contains the Total Price for all the product Items.
        // The "results" contain the list of all the items comprising, Product Title, Product Description,
        // Product Unit Price and the Page Size (kb) for the Product Item's page.
        // NOTE: The Price Total (priceTotal) is converted to a string (String object) because this ensures
        //       that the decimal places are preserved (two decimals). If the Price Total (priceTotal) is added
        //       to the JSON as a BigDecimal, and the 2nd decimal place is zero, the zero is chopped, leaving
        //       one decimal place which is not acceptable for GBP currency.
        //       I believe there maybe some parser functionality in Jackson JSON Java libraries to get round this
        //       issue but had problem creating MAVEN dependency for this library and had to use this workaround
        //       instead. Means that the "total" value in the JSON is wrapped in quotes i.e. "total": "15.10"
        //       where answer in example would probably show "total": 15.10
        //       The same also applies for the individual Unit Price ("unit_price") values for each of the product
        //       Items.  
        json.put("total", priceTotal.toString());
        json.put("results", results);
        
        return json.toString(4);
    } // scrape() method
    

    /**
     *  Accessor Methods
     */

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL argUrl) {
        url = argUrl;
    }
    
    public String getProductTitleDescriptionContainerLocator() {
    	return productTitleDescriptionContainerLocator;
    }
    
    public void setProductTitleDescriptionContainerLocator(String ptdcl) {
    	productTitleDescriptionContainerLocator = ptdcl;
    }
    
    public String getProductTitleElementTagLocator() {
    	return productTitleElementTagLocator;
    }
    
    public void setProductTitleElementTagLocator(String ptl) {
    	productTitleElementTagLocator = ptl;
    }
    
    public String getProductPricePerUnitLocator() {
    	return productPricePerUnitLocator;
    }
    
    public void setProductPricePerUnitLocator(String pppul) {
    	productPricePerUnitLocator = pppul;
    }    
    
    public String getProductTextDescriptionLocator() {
    	return productTextDescriptionLocator;
    }
    
    public void setProductTextDescriptionLocator(String ptdl) {
    	productTextDescriptionLocator = ptdl;
    }    
    
    public String getProductListLocator() {
    	return productListLocator;
    } 
    
    public void setProductListLocator(String pll) {
    	productListLocator = pll;
    }
    
    public String getProductListTag() {
    	return productListTag;
    }
    
    public void setProductListTag(String plt) {
    	productListTag = plt;
    }
    
    public String getProductInfoLocator() {
    	return productInfoLocator;
    }
    
    public void setProductInfoLocator(String pil) {
    	productInfoLocator = pil;
    }
    
    public String getProductInfoElementLocator() {
    	return productInfoElementLocator;
    }
    
    public void setProductInfoElementLocator(String piel) {
    	productInfoElementLocator = piel;
    }
    
} // PageScraper class