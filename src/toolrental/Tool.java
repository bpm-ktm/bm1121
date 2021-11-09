package toolrental;

public class Tool {

	private ToolType type;
	private String brand;
	private String code;
	
	/**
	 * 
	 * @param type
	 * @param brand
	 * @param code tool code, it will be stored as uppercase string.
	 */
	public Tool(ToolType type, String brand, String code) {
		this.type = type;
		this.brand = brand;
		this.code = code.toUpperCase();
	}
	
	public ToolType getType() {
		return type;
	}

	public String getBrand() {
		return brand;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public boolean equals(Object obj) {  
		//Equality is based on tool code property.
		
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (!(obj instanceof ToolType))
	        return false;
	    Tool other = (Tool) obj;
	    return code == null ? other.code == null : code.equals(other.code);
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (code == null ? 0 : code.hashCode());
	    return result;
	}

}
