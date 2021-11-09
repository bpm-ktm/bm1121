package toolrental;

import java.util.List;

/**
 * Class to generate rental agreement at the point-of-sale.
 * @author Binod
 *
 */
public class RentalAgreement {

	public void printRentalAgreement (List<CheckoutData> checkoutData, String customerInfo, String storeInfo, String dateFormat) {
		
		final String LINE_SEPARATOR = System.lineSeparator();
		StringBuffer buff = new StringBuffer(storeInfo);
		buff.append(LINE_SEPARATOR); 
		buff.append(customerInfo);
		buff.append(LINE_SEPARATOR); 
		buff.append("Thank you for renting the tool(s) from our store. \nDetails are below:"); 
		buff.append(LINE_SEPARATOR); 
		
		int counter = 1;
		for(CheckoutData data : checkoutData) {
			buff.append("Tool #" + counter++);
			buff.append(LINE_SEPARATOR);
			buff.append("================");
			buff.append(LINE_SEPARATOR); 
			Tool tool = data.getTool();
			ToolType toolType = tool.getType();
			buff.append("Tool Code: "); buff.append(tool.getCode()); 
			buff.append(LINE_SEPARATOR); 
			buff.append("Tool Type: "); buff.append(toolType.getToolTtype()); 
			buff.append(LINE_SEPARATOR); 
			buff.append("Tool Brand: "); buff.append(tool.getBrand()); 
			buff.append(LINE_SEPARATOR); 
			buff.append("Rental Days: "); buff.append(data.getRentalDayCount());
			buff.append(LINE_SEPARATOR); 
			buff.append("Check Out Date: "); 
			buff.append(ToolRentalUtils.formatDate(data.getCheckoutDate(), dateFormat)); 
			buff.append(LINE_SEPARATOR); 
			buff.append("Due Date: "); 
			buff.append(ToolRentalUtils.formatDate(data.getDueDate(), dateFormat)); 
			buff.append(LINE_SEPARATOR); 
			buff.append("Daily rental charge rate: weekdays - "); 
			buff.append("$"+ toolType.getWeekdayRent() + ", weekends -" + "$"+ toolType.getWeekendRent() + ", holidays - " + "$"+ toolType.getHolidayRent());
			buff.append(LINE_SEPARATOR); 
			buff.append("Charge Days (Days actually charged): weekdays - "); 
			buff.append((toolType.isChargedWeekday() ? data.getNumWeekDays() : 0) 
					+ ", weekends -" + (toolType.isChargedWeekend() ? data.getNumWeekendDays() : 0)  
							+ ", holidays - " + (toolType.isChargedHoliday() ? data.getNumHolidays() : 0));
			buff.append(LINE_SEPARATOR); 
			buff.append("Pre-discount charge: "); buff.append("$"+ data.getGrossRentAmount()); 
			buff.append(LINE_SEPARATOR);
			buff.append("Discount percent: "); buff.append(data.getDiscountPercent() + "%"); 
			buff.append(LINE_SEPARATOR);
			buff.append("Discount Amount: "); buff.append("$"+ data.getDiscountAmount()); 
			buff.append(LINE_SEPARATOR);
			buff.append("Final Charge: "); buff.append("$"+ data.getNetRentAmount()); 
			buff.append(LINE_SEPARATOR);
			buff.append("================");

			
			System.out.println(buff.toString());
			
		}
		
	}
}
