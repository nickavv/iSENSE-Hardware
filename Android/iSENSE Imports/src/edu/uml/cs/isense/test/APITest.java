package edu.uml.cs.isense.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.uml.cs.isense.comm.API;

/**
 * Tests for API.java.
 *
 * @author moldorma@gmail.com (Nick Ver Voort)
 */
@RunWith(JUnit4.class)
public class APITest {
	
    @Test
    public void getAPITest() {
    	API api = API.getInstance();
    	Assert.assertTrue(api != null);
    }

}