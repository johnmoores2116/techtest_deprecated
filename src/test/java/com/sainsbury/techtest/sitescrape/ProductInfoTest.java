/**
 * 
 */
package com.sainsbury.techtest.sitescrape;


import static org.junit.Assert.*;

import org.skyscreamer.jsonassert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mooresjo
 *
 */
public class ProductInfoTest {
	
    private ProductInfo productInfo;
    
    // Declarations and Initialisation of assertion reference values - follow:
    private String sizeAssertVal      = "12.0";
    private String unitPriceAssertVal = "20.00";
    private String titleAssertVal     = "Apple";
    private String descAssertVal      = "A nice red apple";    
    private String toStringAssertVal  = "ProductInfo{" + "title=" + titleAssertVal + ", size=" + sizeAssertVal + ", unitPrice=" + unitPriceAssertVal + ", description=" + descAssertVal + "}";
    private String toJSONAssertVal    = "{\"size\":\""+sizeAssertVal+"\",\"description\":\""+descAssertVal+"\",\"title\":\""+titleAssertVal+"\",\"unit_price\":\""+unitPriceAssertVal+"\"}";
    
	/**
	 * @throws java.lang.Exception
	 * 
	 * Sets up the reference instance of the ProductInfo class.
	 */
	@Before
	public void setUp() throws Exception {
        productInfo = new ProductInfo(titleAssertVal, sizeAssertVal, unitPriceAssertVal, descAssertVal);
	}

	/**
	 * @throws java.lang.Exception
	 * 
	 * Tears down the reference instance of the ProductInfo class.
	 */
	@After
	public void tearDown() throws Exception {
		productInfo = null;
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#ProductInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * 
	 * Test for the ProductInfo class's constructor.
	 * 
	 * Asserts that the class variables have been intialised as expected:
	 *     description
	 *     size
	 *     title
	 *     unitPrice
	 */
	@Test
	public void testProductInfo() {
		assertTrue("Test Failed - Assertion of \"ProductInfo description\" ("+productInfo.getDescription()+" != "+descAssertVal+").", productInfo.getDescription().compareTo(descAssertVal) == 0); 
		assertTrue("Test Failed - Assertion of \"ProductInfo size\" ("+productInfo.getSize()+" != "+sizeAssertVal+").", productInfo.getSize().compareTo(sizeAssertVal) == 0);
		assertTrue("Test Failed - Assertion of \"ProductInfo title\" ("+productInfo.getTitle()+" != "+titleAssertVal+").", productInfo.getTitle().compareTo(titleAssertVal) == 0);
		assertTrue("Test Failed - Assertion of \"ProductInfo unitPrice\" ("+productInfo.getUnitPrice()+" != "+unitPriceAssertVal+").", productInfo.getUnitPrice().compareTo(unitPriceAssertVal) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#toJSON()}.
	 *
	 * Ensures that a JSONObject can be created successfully. 
	 * 
	 */
	@Test
	public void testToJSON() {
		JSONAssert.assertEquals(toJSONAssertVal, productInfo.toJSON(), false); 
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#setTitle(java.lang.String)}.
	 * 
	 * Ensures that the ProductInfo's "title" private class variable can be set correctly using its public setter method.
	 * 
	 */
	@Test
	public void testSetTitle() {
		String stringOverride = "Orange";
		productInfo.setTitle(stringOverride);
		
		assertTrue("Test Failed - Assertion of \"ProductInfo Title\" ("+productInfo.getTitle()+" != "+stringOverride+").", productInfo.getTitle().compareTo(stringOverride) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#setSize(float)}.
	 * 
	 * Ensures that the ProductInfo's "size" private class variable can be set correctly using its public setter method.
	 * 
	 */
	@Test
	public void testSetSize() {
		String stringOverride = "99.0";
		productInfo.setSize(stringOverride);
		
		assertTrue("Test Failed - Assertion of \"ProductInfo Size\" ("+productInfo.getSize()+" != "+stringOverride+").", productInfo.getSize().compareTo(stringOverride) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#setUnitPrice(float)}.
	 * 
	 * Ensures that the ProductInfo's "unitPrice" private class variable can be set correctly using its public setter method.
	 * 
	 */
	@Test
	public void testSetUnitPrice() {
		String stringOverride = "9.00";
		productInfo.setUnitPrice(stringOverride);
		
		assertTrue("Test Failed - Assertion of \"ProductInfo Unit Price\" ("+productInfo.getUnitPrice()+" != "+stringOverride+").", productInfo.getUnitPrice().compareTo(stringOverride) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#setDescription(java.lang.String)}.
	 * 
	 * Ensures that the ProductInfo's "description" private class variable can be set correctly using its public setter method.
	 * 
	 */
	@Test
	public void testSetDescription() {
		String stringOverride = "A nice juicy orange";
		productInfo.setDescription(stringOverride);
		
		assertTrue("Test Failed - Assertion of \"ProductInfo Description\" ("+productInfo.getDescription()+" != "+stringOverride+").", productInfo.getDescription().compareTo(stringOverride) == 0);
	}

	/**
	 * Test method for {@link com.sainsbury.techtest.sitescrape.ProductInfo#toString()}.
	 * 
	 * Ensures that the ProductInfo's "toString" public override method returns an accurate String representation of the ProductInfo (JSON Object).
	 * 
	 */
	@Test
	public void testToString() {		
		assertTrue("Test Failed - Assertion of \"ProductInfo as a String\" ("+productInfo.toString()+" != "+toStringAssertVal+").", (productInfo.toString().compareTo(toStringAssertVal) == 0));
	}

}
