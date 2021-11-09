package toolrental;

/**
 * To track all the application-specific error messages, it helps 
 * to keep them in a central location.
 * @author Binod
 *
 */
public interface AppErrorMessage {
	
	public static final String INVALID_TOOL_SPEC = "Tool spec is not valid : "; 
	public static final String INVALID_RENTAL_DAY_COUNT = "Number of rental days should be 1 or more."; 
	public static final String INVALID_DISCOUNT_PERCENT = "Discount percent should be a whole number in the range of 0 to 100."; 

}
