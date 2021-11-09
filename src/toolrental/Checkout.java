package toolrental;

import java.util.List;

/**
 * Class to check out Tool(s) for rent to a customer.
 * @author Binod
 *
 */
public class Checkout {
	
	//Tools to checkout. For future proofing, allow multiple tools to be checked out at the same time.
	private List<CheckoutData> checkoutData; 
	
	public Checkout(List<CheckoutData> checkoutData) {
		this.checkoutData = checkoutData;

	}

	public void doCheckout() throws ToolRentalException {
		//Invoke checkout for each tool in the list.
		for(CheckoutData data : checkoutData) {
			data.doCheckoutCalculation();
		}	

	} 
	
	public void createAgreement() {
		//We need separate Customer class and StoreInfo classes.
		//Creating some hard-coded values for this demo.
		
		StringBuffer customerInfo = new StringBuffer("Customer:\nJohn Doe\n"); 
		customerInfo.append("123 Rt 100 \n");
		customerInfo.append("New City\n");
		customerInfo.append("NY 111111\n\n");
		
		StringBuffer storeInfo = new StringBuffer("TOOL RENTAL INC.\n");
		storeInfo.append("New London City\n");
		storeInfo.append("phone: 1112223334444\n\n");
		
		//Pass the date formatter. 
		//In a real scenario, the date format may be specified in an external 
		//configuration file so that it can be easily updated globally.
		//Hard-coding for now.
		String dateFormatPattern = "MM/dd/yy";
		
		RentalAgreement agreement = new RentalAgreement();
		
		System.out.println("\n\n Printing out Rental Agreement ....\n");
		agreement.printRentalAgreement(checkoutData, customerInfo.toString(), storeInfo.toString(), dateFormatPattern);
		
	}
}
