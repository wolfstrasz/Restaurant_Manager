/**
 * 
 */
package roms;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class MoneyTest {
    
    @Test
    public void testZero() {
        Money zero = new Money();
        assertEquals("0.00", zero.toString());
    }
    
    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for the Money class below.
     */
    @Test
    public void testStrInput0()
    {
    	// Test if it rounds to floor.
    	Money strInput = new Money("0.12321");
    	assertEquals("0.12", strInput.toString());
    }
    
    @Test
    public void testStrInput1()
    {
    	// Test if it rounds to ceiling.
    	Money strInput = new Money("0.45999");
    	assertEquals("0.46", strInput.toString());
    }
    
    @Test
    public void testStrInput2()
    {
    	// Test if it adds additional 0.
    	Money strInput = new Money("0.7");
    	assertEquals("0.70", strInput.toString());
    }
    
    @Test
    public void testStrInput3()
    {
    	// Test if it rounds fully.
    	Money strInput = new Money("0.9999");
    	assertEquals("1.00", strInput.toString());
    }
    
    @Test
    public void testNegativeStrInput()
    {
    	// Test if it can add negative number;
    	Money strInput = new Money ("-1.999");
    	assertEquals("-2.00",strInput.toString());
    }
    
    @Test
    public void testStrInput4()
    {
    	// Test if it adds additional 0s.
    	Money strInput = new Money("1");
    	assertEquals("1.00", strInput.toString());
    }
    @Test
    public void testAdd0()
    {
    	// Test the Add method
    	Money m1 = new Money();
    	Money m2 = new Money("0.3333");
    	assertEquals("0.33", m1.add(m2).toString());
    }
    
    @Test
    public void testSubstract()
    {
    	// Test if the add method can be used for substracting.
    	Money m1 = new Money("9.87654321");
    	Money m2 = new Money("-9");
    	assertEquals("0.88",m1.add(m2).toString());
    }
    
    @Test
    public void testAdd1()
    {
    	// Test nesting of 2 Add methods.
    	Money m1 = new Money();
    	Money m2 = new Money("0.3333");
    	Money m3 = new Money("8.2");
    	assertEquals("0.33", m1.add(m2).toString());
    	assertEquals("8.53", m1.add(m2).add(m3).toString());
    }
    
    @Test
    public void testAdd2()
    {
    	// Test nesting of 3 Add methods.
    	Money m1 = new Money();
    	Money m2 = new Money("0.3333");
    	Money m3 = new Money("8.2");
    	Money m4 = new Money("1.429");
    	assertEquals("0.33", m1.add(m2).toString());
    	assertEquals("8.53", m1.add(m2).add(m3).toString());
    	assertEquals("9.96", m1.add(m2).add(m3).add(m4).toString());
    }

    @Test
    public void testMultiply0()
     {
    	// Test multiply method for no money
    	Money m1 = new Money();
    	assertEquals("0.00", m1.multiply(2).toString());
    }
    @Test
    public void testMultiply1()
     {
    	Money m1 = new Money("1.111111");
    	assertEquals("1.11", m1.multiply(1).toString());
    }
    @Test
    public void testMultiply2()
     {
    	Money m1 = new Money("1.111111");
    	assertEquals("5.56", m1.multiply(5).toString());
    }
    @Test
    public void testMultiply3()
     {
    	Money m1 = new Money("1.111111");
    	assertEquals("10.00", m1.multiply(9).toString());
    }
    @Test
    public void testMultiply4()
     {
    	Money m1 = new Money("1.111111");
    	assertEquals("11.11", m1.multiply(10).toString());
    }
   
    @Test
    public void testAddPercent0()
    {
    	Money m1 = new Money("3.15");
    	assertEquals("3.56",m1.addPercent(13).toString());
    	
    }
    
    @Test
    public void testAddPercent1()
    {
    	Money m1 = new Money("3.15");
    	assertEquals("5.61",m1.addPercent(78).toString());
    	
    }
    
    @Test
    public void testAddPercent2()
    {
    	Money m1 = new Money("5");
    	assertEquals("27.20",m1.addPercent(444).toString());
    }
    
    @Test
    public void testAddPercent3()
    {
    	Money m1 = new Money("1.23456789");
    	assertEquals("2.68",m1.addPercent(117).toString());
    }
    
    @Test
    public void testAllMethods()
    {
    	/* This test will test all methods
    	 * working together in a single
    	 * banking procedure (deposit).
    	 */
    	// Person invests 5.8£
    	Money m1 = new Money("5.8");
    	assertEquals("5.80",m1.toString());
    	// Bank deposit interest is 3% monthly.
    	// After first month:
    	Money m2 = m1.addPercent(3);
    	assertEquals("5.97",m2.toString());
    	// After second month:
    	Money m3 = m2.addPercent(3);
    	assertEquals("6.15",m3.toString());
    	// Update on bank policies the percent becomes 4
    	// After third month:
    	Money m4 = m3.addPercent(4);
    	assertEquals("6.40",m4.toString());
    	// Person won the bank lottery which triples his balance.
    	Money m5 = m4.multiply(3);
    	assertEquals("19.20",m5.toString());
    	// Person withdraws his money;
    	Money m6 = new Money("-19.20");
    	Money m7 = m5.add(m6);
    	assertEquals("0.00",m7.toString());
    	// End of procedure
    }
    
   /*
    * Put all class modifications above.
    ***********************************************************************
    * END MODIFICATION AREA
    ***********************************************************************
    */


}
