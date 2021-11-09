package toolrental;

import java.math.BigDecimal;

public class ToolType {

	private String toolTtype;
	
	//Daily rental amount. 
	//Future Proofing: Although daily rent is fixed for now, 
	//it may be different in future for holiday or weekend, so 
	//three different rate variables are created here.
	private BigDecimal weekdayRent;

	private BigDecimal weekendRent;
	private BigDecimal holidayRent;
	
	//Default everything to true, but the actual values will come from the config file.
	private boolean chargedWeekday = false;
	private boolean chargedWeekend = false;
	private boolean chargedHoliday = false;
	
	//The last three variables (chargedWeekday, chargedWeekend, chargedHoliday) 
	//are superfluous because they can be derived from the xxxRent variables.
	//Still keeping them separate for more clarity. 
	
	public ToolType(String toolTtype, BigDecimal weekdayRent, BigDecimal weekendRent, BigDecimal holidayRent) {
		this.toolTtype = toolTtype;
		this.weekdayRent = weekdayRent;
		this.weekendRent = weekendRent;
		this.holidayRent = holidayRent;   
		
		//If the daily rent is a positive value, we know the rate need to be applied.
		if (weekdayRent.compareTo(BigDecimal.ZERO) == 1){
			this.chargedWeekday = true;
		} 
		if(weekendRent.compareTo(BigDecimal.ZERO) == 1) {
			this.chargedWeekend = true;
		} 
		if(holidayRent.compareTo(BigDecimal.ZERO) == 1) {
			this.chargedHoliday = true;
		}
	} 
	
	public String getToolTtype() {
		return toolTtype;
	}

	public BigDecimal getWeekdayRent() {
		return weekdayRent;
	}

	public BigDecimal getWeekendRent() {
		return weekendRent;
	}

	public BigDecimal getHolidayRent() {
		return holidayRent;
	}

	public boolean isChargedWeekday() {
		return chargedWeekday;
	}

	public boolean isChargedWeekend() {
		return chargedWeekend;
	}

	public boolean isChargedHoliday() {
		return chargedHoliday;
	} 
	
	@Override
	public boolean equals(Object obj) {  
		//Equality is based on toolTtype property.
		
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (!(obj instanceof ToolType))
	        return false;
	    ToolType other = (ToolType) obj;
	    return toolTtype == null ? other.toolTtype == null : toolTtype.equals(other.toolTtype);
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (toolTtype == null ? 0 : toolTtype.hashCode());
	    return result;
	}

}
