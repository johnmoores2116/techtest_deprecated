DESCRIPTION

The techtest application is a Java console application (Java 1.8). It consumes a webpage and processes data from
the webpage, which includes Product Titles, Product Descriptions and Product Unit Prices. The application then
presents the data, with some running totals, in a JSON array format. The webpage is a Sainsbury's grocery site.
The URL to the webpage is given here:
    (http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html).

RUNNING THE APPLICATION

The instructions that follow describe cloning the git@github.com:johnmoores2116/techtest.git github
repository and then using Maven to compile and run the application.

    NOTE: The given instructions assume the following:
    
        * Maven is is installed on your computer (e.g. apache-maven-3.3.9)
        
        * Git is installed on your computer (e.g. 1.10.0.windows.1)
        
        * A Java JDK with Runtime (JRE) is installed on your computer (e.g. 1.8.0_101)
        
        * You have supplied your github public key to the author John Moores (john.moores2116@gmail.com) and
          it has been installed in the https://github.com/johnmoores2116 github repository (Settings....Deploy Keys).
          This is necessary for access to the github repository to clone (read / download) the application's
          artifacts to your computer. I believe that since June 2016, all new github repositories now only support
          ssh / https.
          
        * You are installing the application in a /tmp/ location on your computer (for windows c:\tmp\)
          
    Start Git Bash (Bash Shell) - For Windows click on the git-bash icon
    
    cd c:\tmp\
         
    git clone git@github.com:johnmoores2116/techtest.git
    
    cd techtest
    
    mvn compile
    
    mvn exec:java
    

RUNNING THE TESTS

To run the tests, the same assumptions listed above apply and following completion of the above
instructions to run the main application, the following command can be used to run the tests:

    mvn test
    

DEPENDENCIES

org.jason (https://github.com/stleary/JSON-java)
------------------------------------------------
This is used for JSON data processing. It provides a JSON object and JSONArray with useful manipulation
methods. 
 
org.jsoup (https://jsoup.org/)
------------------------------
This used is a Java library for working with HTML. It provides a convenient API for extracting and manipulating
data, using DOM, CSS, and jquery-like methods. It used to query the HTML on the webpage(s) to find the webpage
components needed to provide teh required information in the JSON output.

org.junit (http://junit.org/junit4/)
------------------------------------
Used for creating the Unit Tests for components of the application.
     
org.skyscreamer JSONAssert (https://github.com/skyscreamer/JSONassert)
----------------------------------------------------------------------
Used to test the JSON objects - Provides convenient way of asserting them.
   

PLUGINS
org.codehaus.mojo Exec Maven Plugin (http://www.mojohaus.org/)
--------------------------------------------------------------
A Maven plugin to allow execution of system and java programs 

-----------------------------------------------------------
John Moores (john.moores2116@gmail.com) 25th September 2016