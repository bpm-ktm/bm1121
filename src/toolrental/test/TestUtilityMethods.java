package toolrental.test;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import toolrental.ToolRentalUtils;
import toolrental.AppErrorMessage;
import toolrental.Tool;
import toolrental.ToolRentalException;
import toolrental.ToolType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class to test utility methods defined in ToolRentalUtils.java.
 * @author Binod
 *
 */
public class TestUtilityMethods {	
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test 
	public void isLaborDay() {
		//Tests the isLaborDay() method in ToolRentalUtils.
		
		// These are true Labor Days:
		// 2019 Sep 2, 2020 Sep 7, 2021 Sept 6, 2022 Sept 5

		assertTrue(ToolRentalUtils.isLaborDay(LocalDate.of(2019, 9, 2)));
		assertTrue(ToolRentalUtils.isLaborDay(LocalDate.of(2020, 9, 7)));
		assertTrue(ToolRentalUtils.isLaborDay(LocalDate.of(2021, 9, 6)));
		assertTrue(ToolRentalUtils.isLaborDay(LocalDate.of(2022, 9, 5)));

		// Some random dates, all non-Labor Days.
		assertFalse(ToolRentalUtils.isLaborDay(LocalDate.of(2020, 9, 5)));
		assertFalse(ToolRentalUtils.isLaborDay(LocalDate.of(2021, 9, 1)));
		assertFalse(ToolRentalUtils.isLaborDay(LocalDate.of(2021, 10, 1)));
		assertFalse(ToolRentalUtils.isLaborDay(LocalDate.of(2022, 9, 6)));

	} 
	
	@Test 
	public void isHoliday() {
		//Tests the isHoliday() method in ToolRentalUtils.

		//Labor Day 2019
		assertTrue(ToolRentalUtils.isHoliday(LocalDate.of(2019, 9, 2))); 
		//Independence Day 2020, which falls on Saturday, hence not a holiday.
		assertFalse(ToolRentalUtils.isHoliday(LocalDate.of(2020, 7, 4)));
		//Friday before the Independence Day 2020, hence it is a holiday.
		assertTrue(ToolRentalUtils.isHoliday(LocalDate.of(2020, 7, 3)));
		//Independence Day 2021, which falls on Sunday, hence not a holiday.
		assertFalse(ToolRentalUtils.isHoliday(LocalDate.of(2021, 7, 4)));
		//Monday after the Independence Day 2021, hence it is a holiday.
		assertTrue(ToolRentalUtils.isHoliday(LocalDate.of(2021, 7, 5)));
		
		//Some random dates which are neither Labor Day nor Independence Day.
		//These are all not holidays:		
		assertFalse(ToolRentalUtils.isHoliday(LocalDate.of(2020, 5, 5)));
		assertFalse(ToolRentalUtils.isHoliday(LocalDate.of(2021, 12, 5)));
		assertFalse(ToolRentalUtils.isHoliday(LocalDate.of(2021, 8, 5)));
	}
	 
	@Test
	public void countBillableDays() {
		//Tests the countBillableDays() method in ToolRentalUtils.
				
		//First day (contract day) is not included. 
		
		//Test 1:
		// Nov 1st 2021 to Nov 5th 2021.
		// It should be 4 weekdays, 0 weekend days, 0 holidays.
		LocalDate start = LocalDate.of(2021, 11, 1);
		LocalDate end = LocalDate.of(2021, 11, 5);
		int[] billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 4 && billableDates[1] == 0 && billableDates[2] == 0); 
		start = null; end = null; billableDates = null;

		//Test 2:
		// Oct 29 2021 to Nov 7th 2021.
		// It should be 5 weekdays, 4 weekend days, 0 holidays.
		start = LocalDate.of(2021, 10, 29);
		end = LocalDate.of(2021, 11, 7);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 5 && billableDates[1] == 4 && billableDates[2] == 0);
		start = null; end = null; billableDates = null;
		
		//Test 3:
		// Sept 2nd 2021 to Sept 10th 2021.
		// It should have 5 weekdays, 2 weekend days, 1 holiday (Labor Day).
		start = LocalDate.of(2021, 9, 2);
		end = LocalDate.of(2021, 9, 10);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 5 && billableDates[1] == 2 && billableDates[2] == 1); 
		start = null; end = null; billableDates = null;
		
		//Test 4: 
		// July 4th 2021 to July 10th 2021..
		// It should have 4 weekdays, 1 weekend days, 1 holiday (Independence Day). 
		//This Independence Day falls on a Sunday, so it would be observed on Monday the 5th.
		start = LocalDate.of(2021, 7, 4);//Independence Day
		end = LocalDate.of(2021, 7, 10);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 4 && billableDates[1] == 1 && billableDates[2] == 1); 
		start = null; end = null; billableDates = null;
		
		//Test 5: Same as Test 4, but move start date earlier by one day.
		// July 3rd 2021 to July 10th 2021..
		// It should have 4 weekdays, 2 weekend days, 1 holiday (Independence Day). 
		//This Independence Day falls on a Sunday, so it would be observed on Monday the 5th.
		start = LocalDate.of(2021, 7, 3);
		end = LocalDate.of(2021, 7, 10);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 4 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;
		
		/// From here below are for the required test cases. ///
		
		//Test 6: 9/3/15 to 9/8/15 (5 days)
		//It has 2 weekdays, 2 weekend days, 1 holiday
		start = LocalDate.of(2015, 9, 3);
		end = LocalDate.of(2015, 9, 8);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 2 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;		
		
		//Test 7: 7/2/20 to 7/5/20 (3 days)
		//It has 0 weekdays, 2 weekend days, 1 holiday 
		start = LocalDate.of(2020, 7, 2);
		end = LocalDate.of(2020, 7, 5);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 0 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;		
		
		//Test 8: 7/2/15 to 7/7/15  (5 days)
		//It has 2 weekdays, 2 weekend days, 1 holiday 		
		start = LocalDate.of(2015, 7, 2);
		end = LocalDate.of(2015, 7, 7);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 2 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null; 		
		
		//Test 9: 9/3/15 to 9/9/15  (6 days)
		//It has 3 weekdays, 2 weekend days, 1 holiday  
		start = LocalDate.of(2015, 9, 3);
		end = LocalDate.of(2015, 9, 9);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 3 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;
		
		
		//Test 10: 7/2/15 to 7/11/15  (9 days)
		//It has 5 weekdays, 3 weekend days, 1 holiday  
		start = LocalDate.of(2015, 7, 2);
		end = LocalDate.of(2015, 7, 11);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 5 && billableDates[1] == 3 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;
		
		//Test 11: 7/2/20 to 7/6/20 (4 days) 
		//It has 1 weekdays, 2 weekend days, 1 holiday  
		start = LocalDate.of(2020, 7, 2);
		end = LocalDate.of(2020, 7, 6);
		billableDates = ToolRentalUtils.countBillableDays(start, end);
		assertTrue(billableDates[0] == 1 && billableDates[1] == 2 && billableDates[2] == 1); 	
		start = null; end = null; billableDates = null;	
		
	} 
	
	@Test
	public void validateAndCreateTool() throws ToolRentalException {
		//Tests method with the same name in ToolRentalUtils.
		
		//Let's create a Tool object for 
		//tool type = Ladder, brand = Werner, Tool Code = LADW, 
		//daily charge for weekday = $1.99,
		//daily charge for weekend = $1.99 (if not zero, it means, weekend is charged),
		//daily charge for holiday = 0 (0 means holidays are not charged).
		
		String toolSpec = "Ladder,Werner,LADW,1.99,1.99,0"; 
		Tool myTool = ToolRentalUtils.validateAndCreateTool(toolSpec);
		assertEquals("Ladder",myTool.getType().getToolTtype());
		assertEquals("Werner",myTool.getBrand());
		assertEquals("LADW",myTool.getCode());
		assertTrue(myTool.getType().getWeekdayRent().compareTo(new BigDecimal("1.99")) == 0);
		assertTrue(myTool.getType().getWeekendRent().compareTo(new BigDecimal("1.99")) == 0);
		assertTrue(myTool.getType().getHolidayRent().compareTo(new BigDecimal("0")) == 0);
	} 
	
	@Test 
	public void exceptionTestForValidateAndCreateTool() throws ToolRentalException { 
		//Exception test: This test is to make sure an exception is thrown when 
		//nun-numeric value is provided where a number was expected.
	    
		thrown.expect(ToolRentalException.class);
		thrown.expectMessage("Invalid Number");
		
		//If an invalid number is specified for daily rental rate, it throws exception
		String toolSpec = "Ladder,Werner,LADW,1.99$,1.99,0"; //1.99$ is invalid number
		Tool myTool = ToolRentalUtils.validateAndCreateTool(toolSpec);//this should throw exception

	}
	
	@Test 
	public void roundCurrencyWithHalfUp() { 
	    
		BigDecimal bd1 = new BigDecimal("0.5");//value should not change
		BigDecimal bd2 = new BigDecimal("0.495");//should round to 0.50
		BigDecimal bd3 = new BigDecimal("2.6789");//should round to 2.68
		BigDecimal bd4 = new BigDecimal("1.59");//value should not change
		BigDecimal bd5 = new BigDecimal("1.44");//value should not change
		BigDecimal bd6 = new BigDecimal("0.888");//should round to 0.89 
		BigDecimal bd7 = new BigDecimal("1.3333");//should round to 1.33 
		
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd1).equals(new BigDecimal("0.50")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd2).equals(new BigDecimal("0.50")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd3).equals(new BigDecimal("2.68")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd4).equals(new BigDecimal("1.59")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd5).equals(new BigDecimal("1.44")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd6).equals(new BigDecimal("0.89")));
		assertTrue(ToolRentalUtils.roundCurrencyWithHalfUp(bd7).equals(new BigDecimal("1.33")));		
		
	}
}
