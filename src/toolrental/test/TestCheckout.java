package toolrental.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Rule;

import toolrental.AppErrorMessage;
import toolrental.Checkout;
import toolrental.CheckoutData;
import toolrental.Tool;
import toolrental.ToolRentalException;
import toolrental.ToolRentalUtils;
import toolrental.ToolType;

import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test code to implement the checkout routine. Tests are included for the six scenarios mentioned at the end of the spec document.
 * @author bpm
 *
 */
public class TestCheckout { 
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void TestForScenario1() throws ToolRentalException {
		//This scenario has discount percentage more than 100.
		//It should fail with a message.
		
        thrown.expect(ToolRentalException.class);
        thrown.expectMessage(AppErrorMessage.INVALID_DISCOUNT_PERCENT);
		
		String toolSpec = "Jackhammer,Ridgid,JAKR,2.99,0,0 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2015, 9, 3);
		int rentalDayCount = 5;
		int discountPercent = 101; 
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout();//this should throw an exception as declared above at the beginning of this test code.
		
		checkout.createAgreement();//this will not be called 
	} 
	
	@Test
	public void TestForScenario2() throws ToolRentalException {
		
		String toolSpec = "Ladder,Werner,LADW,1.99,1.99,0 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2020, 7, 2);
		int rentalDayCount = 3;
		int discountPercent = 10; 
		
		/* First do manual calculation -
		 * Looking at the calendar: 
		 * July 4th fell on Saturday, so July 3rd is holiday.
		 * This date range covers: 0 weekday, 2 weekend days, 1 holiday. 
		 * Ladder is charged only on weekday and weekend and not on holiday, 
		 * which means only 2 days will be charged.
		 * Daily Rental rate is 1.99. 
		 * Amount before discount = 1.99 * 2 = 3.98
		 * discount amount = 3.98 * 0.1 = 0.398, which rounds to 0.40 
		 * Amount after discount = 3.58
		 */	
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout(); 
		
		//Now validate the computed results.
		
		//Amount before discount (rounded to cents) -
		assertTrue(data.getGrossRentAmount().equals(new BigDecimal("3.98")));
		
		//Discount amount (rounded to cents) -
		assertTrue(data.getDiscountAmount().equals(new BigDecimal("0.40")));	
		
		//Net rent amount (rounded to cents) -
		assertTrue(data.getNetRentAmount().equals(new BigDecimal("3.58"))); 
		
		//Number of days charged - 
		assertTrue(data.getDaysCharged() == 2);
		
		checkout.createAgreement();
	} 
	
	@Test
	public void TestForScenario3() throws ToolRentalException {
		
		String toolSpec = "Chainsaw,Stihl,CHNS,1.49,0,1.49 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2015, 7, 2);
		int rentalDayCount = 5;
		int discountPercent = 25; 
		
		/* First do manual calculation -
		 * Looking at the calendar: 
		 * July 4th fell on Saturday, so July 3rd is holiday.
		 * This date range covers: 2 weekday, 2 weekend days, 1 holiday. 
		 * Chainsaw is charged on weekdays and holidays and not on weekend, 
		 * which means only 3 days will be charged.
		 * Daily Rental rate is 1.49. 
		 * Amount before discount = 1.49 * 3 = 4.47
		 * discount amount = 4.47 * 0.25 = 1.1175, which rounds to 1.12 
		 * Amount after discount = 3.35
		 */	
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout(); 
		
		//Now validate the computed results.
		
		//Amount before discount (rounded to cents) -
		assertTrue(data.getGrossRentAmount().equals(new BigDecimal("4.47")));
		
		//Discount amount (rounded to cents) -
		assertTrue(data.getDiscountAmount().equals(new BigDecimal("1.12")));	
		
		//Net rent amount (rounded to cents) -
		assertTrue(data.getNetRentAmount().equals(new BigDecimal("3.35"))); 
		
		//Number of days charged - 
		assertTrue(data.getDaysCharged() == 3);
		
		checkout.createAgreement();
	} 
		
	@Test
	public void TestForScenario4() throws ToolRentalException {
		
		String toolSpec = "Jackhammer,DeWalt,JAKD,2.99,0,0 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2015, 9, 3);
		int rentalDayCount = 6;
		int discountPercent = 0; 
		
		/* First do manual calculation -
		 * Looking at the calendar: 
		 * It covers Labor Day on the 7th.
		 * This date range covers: 3 weekday, 2 weekend days, 1 holiday. 
		 * Jackhammer is charged on weekdays only. 
		 * which means only 3 days will be charged.
		 * Daily Rental rate is 2.99. 
		 * Amount before discount = 2.99 * 3 = 8.97
		 * discount amount = 0 
		 * Amount after discount = 8.97
		 */	
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout(); 
		
		//Now validate the computed results.
		
		//Amount before discount (rounded to cents) -
		assertTrue(data.getGrossRentAmount().equals(new BigDecimal("8.97")));
		
		//Discount amount (rounded to cents) -
		assertTrue(data.getDiscountAmount().equals(new BigDecimal("0.00")));	
		
		//Net rent amount (rounded to cents) -
		assertTrue(data.getNetRentAmount().equals(new BigDecimal("8.97"))); 
		
		//Number of days charged - 
		assertTrue(data.getDaysCharged() == 3);
		
		checkout.createAgreement();
	} 	  
	
	@Test
	public void TestForScenario5() throws ToolRentalException {
		
		String toolSpec = "Jackhammer,Ridgid,JAKR,2.99,0,0 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2015, 7, 2);
		int rentalDayCount = 9;
		int discountPercent = 0; 
		
		/* First do manual calculation -
		 * Looking at the calendar: 
		 * July 4th fell on Saturday, so July 3rd is holiday.
		 * This date range covers: 5 weekdays, 3 weekend days, 1 holiday. 
		 * Jackhammer is charged on weekdays only, 
		 * which means only 5 days will be charged.
		 * Daily Rental rate is 2.99. 
		 * Amount before discount = 2.99 * 5 = 14.95
		 * discount amount = 0 
		 * Amount after discount = 14.95
		 */	
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout(); 
		
		//Now validate the computed results.
		
		//Amount before discount (rounded to cents) -
		assertTrue(data.getGrossRentAmount().equals(new BigDecimal("14.95")));
		
		//Discount amount (rounded to cents) -
		assertTrue(data.getDiscountAmount().equals(new BigDecimal("0.00")));	
		
		//Net rent amount (rounded to cents) -
		assertTrue(data.getNetRentAmount().equals(new BigDecimal("14.95"))); 
		
		//Number of days charged - 
		assertTrue(data.getDaysCharged() == 5);
		
		checkout.createAgreement();
	} 	
	
	@Test
	public void TestForScenario6() throws ToolRentalException {
		
		String toolSpec = "Jackhammer,Ridgid,JAKR,2.99,0,0 ";
		Tool myRentalTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		LocalDate contractDate = LocalDate.of(2020, 7, 2);
		int rentalDayCount = 4;
		int discountPercent = 50; 
		
		/* First do manual calculation -
		 * Looking at the calendar: 
		 * July 4th fell on Saturday, so July 3rd is holiday.
		 * This date range covers: 1 weekdays, 2 weekend days, 1 holiday. 
		 * Jackhammer is charged on weekdays only, 
		 * which means only 1 days will be charged.
		 * Daily Rental rate is 2.99. 
		 * Amount before discount = 2.99 * 1 = 2.99
		 * discount amount = 1.495 which rounds to 1.50
		 * Amount after discount = 1.49
		 */	
		
		CheckoutData data = new CheckoutData(myRentalTool, rentalDayCount, discountPercent, contractDate);
		ArrayList<CheckoutData> toolList = new ArrayList<CheckoutData>();
		toolList.add(data);
		Checkout checkout = new Checkout(toolList);
		
		checkout.doCheckout(); 
		
		//Now validate the computed results.
		
		//Amount before discount (rounded to cents) -
		assertTrue(data.getGrossRentAmount().equals(new BigDecimal("2.99")));
		
		//Discount amount (rounded to cents) -
		assertTrue(data.getDiscountAmount().equals(new BigDecimal("1.50")));	
		
		//Net rent amount (rounded to cents) -
		assertTrue(data.getNetRentAmount().equals(new BigDecimal("1.49"))); 
		
		//Number of days charged - 
		assertTrue(data.getDaysCharged() == 1);
		
		checkout.createAgreement();
	} 	

}
