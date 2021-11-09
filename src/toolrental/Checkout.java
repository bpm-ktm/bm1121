package toolrental;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class to represent data needed to complete a checkout for a specific Tool. 
 * @author Binod
 *
 */
public class CheckoutData {
 /*
  * This class is for future proofing, to allow more than one tool to be rented 
  * at one time (instead of just one), so that we can send a list of CheckoutData to the checkout process.
  */
	
	//Tool to rent out.
	private Tool tool; 
	
	private int totalDayCount;	
	
	private int discountPercent; 
	
	private LocalDate checkoutDate; 
	
	//Below variables are calculated and tracked here to print out in rental agreement.
	private int numWeekDays, numWeekendDays, numHolidays; 
	private BigDecimal grossRentAmount;//before discount, rounded to cents
	private BigDecimal discountAmount; //rounded to cents
	private BigDecimal netRentAmount;//after discount, rounded to cents
	private LocalDate dueDate;
	private int daysCharged;//actual number of days charged
	
	/**
	 * 
	 * @param tool
	 * @param totalDayCount it is the total number of days in the contract period. The number of days actually charged may be less than this depending on the policy.
	 * @param discountPercent
	 * @param checkoutDate
	 */
	public CheckoutData(Tool tool, int totalDayCount, int discountPercent, LocalDate checkoutDate) {
		this.tool = tool;		
		this.totalDayCount = totalDayCount;
		this.discountPercent = discountPercent;
		this.checkoutDate = checkoutDate;
	}
	
	public Tool getTool() {
		return tool;
	}

	public int getRentalDayCount() {
		return totalDayCount;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	} 
	
	public LocalDate getDueDate() {
		return dueDate;
	}
	
	public int getNumWeekDays() {
		return numWeekDays;
	}

	public int getNumWeekendDays() {
		return numWeekendDays;
	}

	public int getNumHolidays() {
		return numHolidays;
	}

	public BigDecimal getGrossRentAmount() {
		return grossRentAmount;
	}

	public BigDecimal getNetRentAmount() {
		return netRentAmount;
	} 
	
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public int getDaysCharged() {
		return daysCharged;
	}
	
	/**
	 * Performs calculations needed for checkout.
	 */
	public void doCheckoutCalculation() throws ToolRentalException{ 
		
		//Check if we have discount in the correct range.
		if(discountPercent < 0 || discountPercent > 100) {
			throw new ToolRentalException(AppErrorMessage.INVALID_DISCOUNT_PERCENT);
		}
		
		//check if we have rental day number is in the correct range.
		if(totalDayCount < 1) {
			throw new ToolRentalException(AppErrorMessage.INVALID_RENTAL_DAY_COUNT);
		}
		
		//Compute the number of weekdays, weekend days, and holidays.
		dueDate = checkoutDate.plusDays(totalDayCount);
		int[] billableDays = ToolRentalUtils.countBillableDays(checkoutDate, dueDate); 
		numWeekDays = billableDays[0];
		numWeekendDays = billableDays[1]; 
		numHolidays = billableDays[2];  
		
		ToolType toolType = tool.getType();
		
		//Do all monetary calculations using BigDecimal class.
		
		BigDecimal discountFraction = new BigDecimal(discountPercent).divide(new BigDecimal(100));
		
		BigDecimal countWeekDays = new BigDecimal(numWeekDays);
		BigDecimal countWeekendDays = new BigDecimal(numWeekendDays);
		BigDecimal countHolidDays = new BigDecimal(numHolidays);
		
		grossRentAmount = toolType.getWeekdayRent().multiply(countWeekDays) 
				.add(toolType.getWeekendRent().multiply(countWeekendDays))
				.add(toolType.getHolidayRent().multiply(countHolidDays));

		grossRentAmount = ToolRentalUtils.roundCurrencyWithHalfUp(grossRentAmount);
		
		discountAmount = grossRentAmount.multiply(discountFraction); 
		
		discountAmount = ToolRentalUtils.roundCurrencyWithHalfUp(discountAmount); 
		
		netRentAmount = grossRentAmount.subtract(discountAmount) ;
		
		//TAXES SKIPPED for this demo.
		
		daysCharged = numWeekDays //weekdays are ALWAYS charged 
				+ (toolType.isChargedWeekend() ? numWeekendDays : 0) 
				+ (toolType.isChargedHoliday() ? numHolidays : 0);
		
	}
	
}
