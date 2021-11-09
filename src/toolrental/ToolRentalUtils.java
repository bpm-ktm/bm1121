package toolrental;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * Class to collect miscellaneous utility methods.
 * 
 * @author Binod
 *
 */
public class ToolRentalUtils {

	/**
	 * For a given date range, it counts how many weekdays, weekend days, and
	 * holidays occur, excluding the start date and including the end date.
	 * 
	 * @param start
	 * @param end 
	 * @return an array of integers with three elements in order: number of weekdays, weekend days, and holidays.
	 */
	public static int[] countBillableDays(LocalDate start, LocalDate end) {
		int totalDays = 0;
		int weekDays = 0;
		int weekendDays = 0;
		int holidays = 0;
	
		//This for-loop starts from the day after the contract day, which is the general requirement for this project.
		for (LocalDate date = start.plusDays(1); date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
			totalDays++; 
			
			if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				//This is a weekend day. It is not affected by holiday.
				weekendDays++;
			} else {
				//It could be a weekday or a holiday. 
				//Let's count it as weekday here, and will decrement later if it is found to be a holiday.
				weekDays++;
			} 
			
			//Holidays. In general, the effect of holidays is to increment the holiday 
			//count and decrement the weekday count by the same amount. 
			//There are special cases to be considered for the Independence Day, which will be handled later below.
			if(isHoliday(date)) {
				holidays++;
				weekDays--;
			}
		}

		//Return these three numbers in an array of integers.

		return new int[] { weekDays, weekendDays, holidays };
	}

	/**
	 * Tests if a date is a holiday. <br/>
	 * <br/>
	 * 
	 * For this project, only two holidays are to be considered: a) Independence
	 * Day, July 4th - If falls on weekend, it is observed on the closest weekday
	 * (if Sat, then Friday before, if Sunday, then Monday after) <br/>
	 * b) Labor Day - First Monday in September
	 * 
	 * @return
	 */
	public static boolean isHoliday(LocalDate date) {
		if (isLaborDay(date)) {
			return true;
		}		
		
		if(isIndependenceDay(date)) {
			//If the Independence day doesn't fall on a weekend, it is a holiday. 
			if(date.getDayOfWeek() != DayOfWeek.SATURDAY 
					&& date.getDayOfWeek() != DayOfWeek.SUNDAY) {
				return true;
			}
		}
		
		//If it is a Friday and the next day is the Independence Day, it is a holiday.
		if(date.getDayOfWeek() == DayOfWeek.FRIDAY 
				&& isIndependenceDay(date.plusDays(1))) { 
			System.out.println("test 2");
			return true;
		} 
		
		//If it is a Monday and the previous day is the Independence Day, it is a holiday.
		if(date.getDayOfWeek() == DayOfWeek.MONDAY 
				&& isIndependenceDay(date.minusDays(1))) {
			return true;
		}
		return false;
	} 
	
	/**
	 * Tests if a date is Independence Day, which falls on July 4th.
	 * @param date
	 * @return
	 */
	public static boolean isIndependenceDay (LocalDate date) { 
		if (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4) {
			return true;
		}
		return false;
	} 
	
	/**
	 * Tests if a date is Labor Day, which falls on the first Monday in September.
	 * @param date
	 * @return
	 */
	public static boolean isLaborDay (LocalDate date) { 
		// To find out correct Labor day, iterate over the first 7 days of Sept.
		
		if (date.getMonth() == Month.SEPTEMBER) {
			LocalDate firstDayOfSept = LocalDate.of(date.getYear(), 9, 1);
			LocalDate seventhtDayOfSept = LocalDate.of(date.getYear(), 9, 7);
			int laborDay = 0;
			for (LocalDate d1 = firstDayOfSept; d1.isBefore(seventhtDayOfSept.plusDays(1)); d1 = d1.plusDays(1)) {
				if (d1.getDayOfWeek() == DayOfWeek.MONDAY) {
					laborDay = d1.getDayOfMonth();
					break;
				}
			}

			if(laborDay == date.getDayOfMonth()) { //Labor Day was found 
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Provides a way to initialize a Tool object by passing a CSV String. <br/> 
	 * This is useful when creating an inventory of tools, for example, by loading data from a file. <br/>
	 * Validates the String and initializes a Tool object from it. 
	 * @param toolSpec specification for a tool. <br/>
	 * Format is: "toolType,brand,toolCode,weekDayDailyRent,weekendDailyRent,HolidayDailyRent" <br/>
	 * For example: "Ladder,Werner,LADW,1.99,0,0".
	 */
	public static Tool validateAndCreateTool (String toolSpec) throws ToolRentalException{ 
		//Ignore leading and trailing white spaces.
		String trimmed = toolSpec.trim();
		
		/*
		 * Validation rules: 
		 * 1. there must be 6 items, all non-empty
		 * 2. Numerical values must be valid decimals.
		 * 
		 */

		String[] items = trimmed.split(",");
		if(items.length != 6) {
			throw new ToolRentalException (AppErrorMessage.INVALID_TOOL_SPEC + trimmed + ": Not enough data");
		} 
		String toolType = items[0].trim(); 
		String brand = items[1].trim(); 
		String toolCode = items[2].trim();
		String weekdayRent = items[3].trim();
		String weekendRent = items[4].trim();
		String holidayRent = items[5].trim(); 		
		
		if(toolType == "" || brand == "" || toolCode == "" || weekdayRent == "" || weekendRent == "" || holidayRent == "") {
			throw new ToolRentalException (AppErrorMessage.INVALID_TOOL_SPEC + trimmed + ": Not enough data");
		} 
		
		double weekdayRentParsed = 0;
		double weekendRentParsed = 0;
		double holidayRentParsed = 0;
		
		try {
			weekdayRentParsed = Double.parseDouble(weekdayRent); 
			weekendRentParsed = Double.parseDouble(weekendRent);
			holidayRentParsed = Double.parseDouble(holidayRent);
		} catch (NumberFormatException e) {
			throw new ToolRentalException (AppErrorMessage.INVALID_TOOL_SPEC + trimmed + ": Invalid Number");
//			throw new ToolRentalException (": Invalid Number");
		}
		
		//We have all data for a particular tool validated. Create the tool object.
		//Note that while creating BigDecimal for a double value, it is better to use the 
		//string representation of the double value than the double value itself.
		ToolType ttype = new ToolType(toolType, new BigDecimal(weekdayRent), new BigDecimal(weekendRent), new BigDecimal(holidayRent));
		
		return new Tool(ttype, brand, toolCode);
	} 
	
	/**
	 * Rounds a BigDecimal using the HALF_UP strategy, keeping 2 decimal digits, suitable for displaying monetary values.
	 * 
	 * @return
	 */
	public static BigDecimal roundCurrencyWithHalfUp(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Formats a date string as per specified format. An example format string could be "dd/MM/YYYY".
	 * @param date
	 * @param format
	 * @return formatted string, or if an error is encountered, returns the default format of the LocalDate.
	 */
	public static String formatDate(LocalDate date, String format) { 
		DateTimeFormatter formatter = null;
		String formatted = null;
		
		//As formatting a date is not a critical operation, if an error is encountered, 
		//simply catch the error and return a default formatted string.
		//Do not exit the run time.
		
		try {
			formatter = DateTimeFormatter.ofPattern(format);
			if(formatter != null) {
				formatted = formatter.format(date);
			}
		} catch (Exception e) {
			System.out.println("Date formatting failed. Message = " + e.getMessage());
		}
		
		if(formatted != null) {
			return formatted;
		} else {
			return date.toString();
		}
		
	}

	
	
	

}
