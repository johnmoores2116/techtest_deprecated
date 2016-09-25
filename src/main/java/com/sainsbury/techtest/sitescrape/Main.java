package com.sainsbury.techtest.sitescrape;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class
 * 
 * Entry point.
 * 
 * @author John Moores
 */
public class Main {
    public static final void main(String[] args) {
        String urlStr = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";
        
        if (args.length == 1) {
            // If user provides a URL, then okay to use it so override the intitialised value with the value
            // provided by the user.
            urlStr = args[0];
        }
        
        try {
        	// Create the java.net.URL object from the URL string (String object).
            URL url = new URL(urlStr);
            
            // Instantiate the PageScraper object to commence processing of the webpage specified by the
            // URL (url).
            PageScraper webPageScraper = new PageScraper(url);
            
            // Output the JSON to the terminal.
            System.out.println(webPageScraper.scrape());
        } catch (MalformedURLException ex) {
            System.out.println("The web-address (URL) \"" + urlStr + "\" is invalid since program caught a MalformedURLException exception. Exiting....");
        }
    }
}