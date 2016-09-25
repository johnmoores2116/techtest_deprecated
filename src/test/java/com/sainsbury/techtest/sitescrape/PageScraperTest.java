package com.sainsbury.techtest.sitescrape;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PageScraperTest {
	private PageScraper pgeScraper;
	
	private String specifiedUrlString = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";
	
	// Declarations and Initialisation of assertion reference values - follow:
	//    NOTE: Obviously, not necessarily a great idea this since it closely links this test with the webpage
	//          specified for the test. If the number of items changes then the test would need to be
	//          modified. But considering time limitation - thought the risk worth taking.
	private int numJsonEntries = 7;
	private String[] sizeAssertArray      = {"34","35","39","35","35","35","36"};
	private String[] unitPriceAssertArray = {"3.50","1.50","1.80","3.20","1.5","1.80","1.80"};
	private String[] descriptAssertArray  = {"Apricots","Avocados","Avocados","Avocados","Conference","Gold Kiwi","Kiwi"};
	private String[] titleAssertArray     = {"Sainsbury's Apricot Ripe & Ready x5","Sainsbury's Avocado Ripe & Ready XL Loose 300g","Sainsbury's Avocado, Ripe & Ready x2","Sainsbury's Avocados, Ripe & Ready x4","Sainsbury's Conference Pears, Ripe & Ready x4 (minimum)","Sainsbury's Golden Kiwi x4","Sainsbury's Kiwi Fruit, Ripe & Ready x4"};
	    
	
	/**
	 * @throws java.lang.Exception
	 * 
	 * Sets up the reference instance of the PageScraper class.
	 */
	@Before
	public void setUp() throws Exception {
        try {
            pgeScraper = new PageScraper(new URL(specifiedUrlString));
        } catch (MalformedURLException muex) {
            Logger.getLogger(PageScraperTest.class.getName()).log(Level.SEVERE, null, muex);
        }
	}

	/**
	 * @throws java.lang.Exception
	 * 
	 * Tears down the reference instance of the PageScraper class.
	 */
	@After
	public void tearDown() throws Exception {
		pgeScraper = null;
	}

	/**
	 * Test method for Test method for {@link com.sainsbury.techtest.sitescrape.PageScraper#PageScraper()}.
	 * 
	 * Test for the PageScraper class's constructor.
	 * 
	 * Asserts that the "url" class variable has been intialised as expected.
	 * 
	 */	
	@Test
	public void testPageScraper() {
		assertTrue("Test Failed - Assertion of \"PageScraper url\" ("+pgeScraper.getUrl().toString()+" != "+specifiedUrlString+").", pgeScraper.getUrl().toString().compareTo(specifiedUrlString) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.PageScraperTest#testCreateProductInfo()}.
	 * 
	 * Ensures that an instance of the ProductionInfo class can be created successfully by the PageScraper class's
	 * createProductInfo method.
	 * 
	 */
	@Test
	public void testCreateProductInfo() {
		
		// URL for one of the shopping items to use for test. 
		String itemUrl = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/sainsburys-apricot-ripe---ready-320g.html";
		
		// Create the Assertion Product Info object - Note that size is an arbitrary value - It won't be used in later Assertions since could not
		// work out how to derive independent value for page size ("7.7") to use as an assertion value - 3 out of 4 assertions hopefully okay.
		ProductInfo assertProdInfo = new ProductInfo("Sainsbury's Apricot Ripe & Ready x5", "7.7", "3.50", "Apricots");
		
		// Create instance of ProductInfo class using the createProductInfoMethod under test.
		ProductInfo prodInfo = pgeScraper.createProductInfo(itemUrl);
		
		// Do the Assertions - Will check the class variables gained from the web page, ensuring they match the expected values.
        // NOTE: Not testing the Page Size - As previously mentioned, could not work out how to derive independent value for page size
		// to use as an assertion value - 3 out of 4 assertions hopefully okay.
		assertTrue("Test Failed: Did not find expected Description: "+assertProdInfo.getDescription(), (prodInfo.getDescription().compareTo(assertProdInfo.getDescription()) == 0));
		assertTrue("Test Failed: Did not find expected Title: "+assertProdInfo.getTitle(), (prodInfo.getTitle().compareTo(assertProdInfo.getTitle()) == 0));
		assertTrue("Test Failed: Did not find expected Unit Price: "+assertProdInfo.getUnitPrice()+". Found "+prodInfo.getUnitPrice()+".", prodInfo.getUnitPrice().compareTo(assertProdInfo.getUnitPrice()) == 0);
	}
	
	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.PageScraperTest#testScrape()}.
	 * 
	 * This test comprises multiple sub-tests specified to test all the aspects of the JSON
	 * produced by the PageScaper class's scrape method. 
	 */	
	@Test
	public void testScrape() {
        String json = pgeScraper.scrape();
                
        /*********************************************************************************************************
         * Test contents of JSON are as expected.
         *********************************************************************************************************/ 
        // Assert "total" in JSON. 
        assertTrue("Test Failed: Did not locate expected \"total\" in JSON.",json.contains("total"));
        
        // Assert "results" in JSON.
        assertTrue("Test Failed: Did not locate expected \"results\" in JSON.", json.contains("results"));
        
        // Need to test for items in the repeating list part of the JSON - Testing for 7 entries in the list.
        for (int i=0; i<numJsonEntries; i++) {
        	// Assert "size" entries in the JSON list.
            assertTrue("Test Failed: Did not locate expected "+sizeAssertArray[i]+" value in JSON.", json.contains(sizeAssertArray[i]));
            
            // Assert "unitPrice" entries in the JSON list.
            assertTrue("Test Failed: Did not locate expected "+unitPriceAssertArray[i]+" value in JSON. Found "+json+".", json.contains(unitPriceAssertArray[i]));
            
            // Assert "description" entries in the JSON list.
            assertTrue("Test Failed: Did not locate expected "+descriptAssertArray[i]+" value in JSON.", json.contains(descriptAssertArray[i]));
            
            // Assert "title" entries in the JSON list.
            assertTrue("Test Failed: Did not locate expected "+titleAssertArray[i]+" value in JSON.", json.contains(titleAssertArray[i]));
        }
        
        /*********************************************************************************************************
         * Test that empty JSON is returned when no Product List is found on the specified page.
         *********************************************************************************************************/
        // Override the "Product List Locator" on the pageScraper object, giving it
        // an invalid identifier for locating the Product List, forcing it to NOT return
        // the product list.
	    pgeScraper.setProductListLocator("ul.NOTproductLister");
	    
	    // Now the "Product List Locator" is overridden - repeat the page Scrape with the
	    // PageScraper class's pageScrape method.
	    json = pgeScraper.scrape();

	    // Now do the assertion to ensure empty JSON is returned. 
        assertTrue("Test Failed: Did not get expected empty JSON ({}). Got this: "+json, (json.compareTo("{}") == 0));
        
        
        /*********************************************************************************************************
         * Test that MalformedURLException can and is thrown when a dodgy URL is used.
         *********************************************************************************************************/        
		// Override the pageScraper object giving it an invalid url so that a MalformedURLException
		// is thrown.
		try {
		    pgeScraper = new PageScraper(new URL(""));
		    assertTrue("Test Failed: Did not trap expected MalformedURLException.", false);
		} catch (MalformedURLException muex) {
			// Swallow the exception and exit test.
			assertTrue("Test Passed: Trapped expected MalformedURLException.", true);
		}
	}	
}
