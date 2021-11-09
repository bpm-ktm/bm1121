package toolrental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/** 
 * Singleton to load configuration data from external file. 
 * Two configuration files are assumed to exist at the top level of project under resources folder.<br/> 
 * a) tool-rental-config.txt : for global properties, if any <br/>
 * b) tools-inventory.txt : to populate the inventory of tools from data in CSV format. 
 * @author Binod
 *
 */
public class ToolRentalAppConfig {
	//singleton instance
	private final static ToolRentalAppConfig INSTANCE = new ToolRentalAppConfig(); 
	
	private static final String CONFIG_FILE = "resources/tool-rental-config.txt";
	private static final String INVENTORY_FILE = "resources/tools-inventory.txt";
	
	//Externally loaded configuration properties are stored in this.
	private Properties prop = new Properties();
	
	private static List<Tool> toolInventory = new ArrayList<Tool>();
	
	private String[] holidays = null;
	
	//make constructor private to not allow any other instantiation
	private ToolRentalAppConfig(){}
	
	public static ToolRentalAppConfig getInstance(){return INSTANCE;}
	
	public void loadConfig() throws IOException { 

		Path path = Paths.get(CONFIG_FILE); 
		
		String absolutePath = path.toAbsolutePath().toString(); 
		File configFile = new File(absolutePath);

		if(!configFile.exists()) {
			throw new FileNotFoundException(absolutePath);
		}
		
		try (InputStream is = new FileInputStream(absolutePath)){
	        prop.load(is);
		}  
		
		System.out.println("Succefully loaded config file from " + CONFIG_FILE);
		
	} 
	
	public void loadInventory() throws IOException, ToolRentalException { 

		Path path = Paths.get(INVENTORY_FILE); 
		
		String absolutePath = path.toAbsolutePath().toString(); 
		File inventoryFile = new File(absolutePath);

		if(!inventoryFile.exists()) {
			throw new FileNotFoundException(absolutePath);
		}
		
		int toolCount = 0;
	    try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
	    	while (true) {
	    		String line = br.readLine();
	    		if(line == null) {
	    			break;
	    		}
	    		line = line.trim();
	    		if(line.startsWith("#") || line.equals("")) {
	    			continue;
	    		}
	    		toolInventory.add(ToolRentalUtils.validateAndCreateTool(line)); 
	    		toolCount++;
	    	} 	    	
	    } 
	    
		System.out.println("Succefully loaded tool inventory from " + INVENTORY_FILE);
		System.out.println("Tool Count = " + toolCount);
	} 
	
	public List<Tool> getToolInventory(){
		List<Tool> umodifiableList = Collections.unmodifiableList(toolInventory);
		return umodifiableList;
	} 
	
	public Properties getConfigProperties() {
		return prop;
	}
	
	/**
	 * This main class is used to test this class during development.
     * 
	 */
	public static void main(String[] args) throws IOException, ToolRentalException {
		ToolRentalAppConfig.getInstance().loadConfig();
		ToolRentalAppConfig.getInstance().loadInventory();
	}
}
