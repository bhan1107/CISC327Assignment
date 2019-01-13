import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.PrintWriter;

public class Front {
    public static String filePath = "./TSFolder/"; //global variable telling where the Transaction summary file should be
    public ArrayList<Integer> validServicesList = new ArrayList<Integer>();
    public static int singleSessionTicketNumber = 0;
	//global variable of totaled tickets cancelled in a session
	private static int totalTicketsCancelled = 0;
	Map<String, Integer> CancelTicketCounter= new HashMap<String, Integer>();
	
	//Constructor for front end
	public Front(ArrayList<Integer> validServicesListInput, ArrayList<String> inputList, int sessionNum) {
		validServicesList = new ArrayList<Integer>(validServicesListInput);
		routeInput(validServicesList, inputList, sessionNum);
	}
	
	public void routeInput (List<Integer> validServicesList, List<String> inputList, int sessionNum) {
		//TSF is the string to write to the valid services file
		boolean TSFError = false;
		String TSFLine = "";
		ArrayList<String> TSFLines = new ArrayList<String>();
		int inputListLength = inputList.size();
		
		if (inputListLength == 0) {
			writeTSF(sessionNum);
			return;
		}	
		
		String role = inputList.get(0);
		
		//Check if user entered a valid role
		if (!role.equals("agent") && !role.equals("planner")) {
			System.out.println("ERROR: Illegal role provided");
			return;
		}
		
		int i = 1;
		
		//Check through every line user inputed
		while (i < inputListLength) {
			if (inputList.get(i).equals("createservice")) {
				//Checks that the Strings have only int characters
				if (isActuallyANum(inputList.get(i+1)) && isActuallyANum(inputList.get(i+2))) {
					TSFLine = createService(role, Integer.parseInt(inputList.get(i+1)), inputList.get(i+2), inputList.get(i+3));
				}
				else {
					TSFLine = "error";
				}
				//If operation did not have any issues
				if (!TSFLine.equals("error")) {
					TSFLines.add(TSFLine);
				}
				
				i += 3;
			}
			else if (inputList.get(i).equals("deleteservice")) {
				//Checks that the String has only int characters
				if (isActuallyANum(inputList.get(i+1))) {
					TSFLine = deleteService(role, Integer.parseInt(inputList.get(i+1)), inputList.get(i+2));
				}else {
					TSFLine = "error";
				}
				
				//If operation did not have any issues
				if (!TSFLine.equals("error")) {
					TSFLines.add(TSFLine);
				}
				
				i += 2;
			}
			else if (inputList.get(i).equals("sellticket")) {
				//Checks that the Strings have only int characters
				if (isActuallyANum(inputList.get(i+1)) && isActuallyANum(inputList.get(i+2))) {
					TSFLine = sellTicket(Integer.parseInt(inputList.get(i+1)), Integer.parseInt(inputList.get(i+2)));
				}
				else {
					TSFLine = "error";
				}
				
				//If operation did not have any issues
				if (!TSFLine.equals("error")) {
					TSFLines.add(TSFLine);
				}
				
				i += 2;
			}
			else if (inputList.get(i).equals("cancelticket")) {
				//Checks that the Strings have only int characters
				if (isActuallyANum(inputList.get(i+1)) && isActuallyANum(inputList.get(i+2))) {
					TSFLine = cancelTicket(role, Integer.parseInt(inputList.get(i+1)), Integer.parseInt(inputList.get(i+2)));
				}
				else {
					TSFLine = "error";
				}
				
				//If operation did not have any issues
				if (!TSFLine.equals("error")) {
					TSFLines.add(TSFLine);
				}
				
				i += 2;
			}
			else if (inputList.get(i).equals("changeticket")) {
				//Checks that the Strings have only int characters
				if (isActuallyANum(inputList.get(i+1)) && isActuallyANum(inputList.get(i+2)) && isActuallyANum(inputList.get(i+3))) {
					TSFLine = changeTicket(role, Integer.parseInt(inputList.get(i+1)), Integer.parseInt(inputList.get(i+2)), Integer.parseInt(inputList.get(i+3)));
				}else {
					TSFLine = "error";
				}

				//If operation did not have any issues
				if (!TSFLine.equals("error")) {
					TSFLines.add(TSFLine);
				}
				
				i += 3;
			}
			
			i++;
		}
		
		if (TSFError) {
			writeTSF(sessionNum);
		}
		else {
			try {
				writeTSF(TSFLines, sessionNum);
			} catch (Exception e) {
				System.out.println("ERROR: Could not print to Transaction Summary File");
			}
		}
		
	}
	
	//Generate empty Transaction Summary File in error case
	public static void writeTSF(int sessionNum) {
        PrintWriter outputStream;
		try {
			outputStream = new PrintWriter(filePath + "tsf" + sessionNum + ".txt");
			outputStream.print("EOS");
	        outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Generate valid Transaction Summary File
    public static void writeTSF(ArrayList<String> TSFLines, int sessionNum) throws Exception {
    	int TSFLinesLength = TSFLines.size();
        try {
        	PrintWriter outputStream = new PrintWriter(filePath + "tsf" + sessionNum + ".txt");
            
        	for (int i = 0; i < TSFLinesLength; i++) {
        		outputStream.println(TSFLines.get(i));
        	}
        	outputStream.print("EOS");
        	outputStream.close();
        }
        catch (FileNotFoundException e) {
        	
        	e.printStackTrace();
        }
        
    }
    
    public String changeTicket(String plannerOrAgent, int currentServiceNum, int newServiceNum, int numTickets){
        singleSessionTicketNumber += numTickets;
    	boolean valid_1 = isValidServiceNumber(currentServiceNum);     //check if source service num is valid
        boolean valid_2 = isValidServiceNumber(newServiceNum);         //check if destination service num is valid
        boolean valid_3 = isValidTicketNumber(numTickets, plannerOrAgent);             //validation of number of tickets
        
        if(valid_1 && valid_2 && valid_3){                                      //Transaction summary file is written if and only if all 3 validations are true
            return "CHG " + currentServiceNum + " " + numTickets + " " + newServiceNum + " **** " + "0" + "\n";
        }
        else{
            return "error";                                                     //single false value in all 3 boolean will cause an error
        }
        
    }
    //helper method for changeTicket on validation of service number since helper method for create service cannot used for change ticket
    private boolean isValidServiceNumber(int serviceNum) {
        if (validServicesList.contains(serviceNum)) {
                return true; //Valid
        }
        
        return false;
    }
    
    private boolean isValidTicketNumber(int numTickets, String plannerOrAgent){
        if(numTickets < 1000 && numTickets > 0 && plannerOrAgent.equals("planner")){ //check all planner transaction is accepted
            return true;
        }else if(numTickets < 21 && numTickets > 0 && singleSessionTicketNumber < 21){                           //check if agent transaction is accepted, while number of changeticket is limited to 20
            return true;
        }else{
            return false;
        }
    }
	
	public String createService(String role, int serviceNum, String date, String serviceName) {
		
		//if the user logs in as agent
		if (role.equals("agent")) {
			return "error";
		} 
		
		
		// If the user entered a valid service number, valid service name, and valid date
		if (isValidServiceNum(serviceNum) && isValidServiceName(serviceName) && isValidDate(date)) {
			return "CRE " + serviceNum + " 0 00000 " + serviceName + " " + date + "\n";
		}else {
			return "error";
		}
	}
	
	//createService helper function
	//Checks if the service number is valid
	private boolean isValidServiceNum(int serviceNum) {
		String serviceNumS = Integer.toString(serviceNum);
		if (serviceNumS.length() == 5) {
			if (serviceNumS.charAt(0) != '0') {
				if (!validServicesList.contains(serviceNum)) {
					return true; //Valid
				}
			}
		}
		return false;
	}
	
	//createService helper function
	//Checks if the service name is valid
	private boolean isValidServiceName(String serviceName) {
		//if correct length
		String serviceNameNoSpace = serviceName.replaceAll(" ","");
		if ((serviceName.charAt(0) != ' ')&&(serviceName.charAt(serviceName.length()-1) != ' ')) {
			if (serviceNameNoSpace.length() >= 3 && serviceNameNoSpace.length() <= 39) {
				boolean illegalCharFound = false;
				for (int i = 0; i < serviceNameNoSpace.length(); i++) {
					//if only alpha-numeric + quote
					if (!((serviceNameNoSpace.charAt(i) >= 'A' && serviceNameNoSpace.charAt(i) <='Z')||(serviceNameNoSpace.charAt(i) >= 'a' && serviceNameNoSpace.charAt(i) <= 'z')||(serviceNameNoSpace.charAt(i) >= '0' && serviceNameNoSpace.charAt(i) <= '9')||(serviceNameNoSpace.charAt(i) == '\''))) {
						illegalCharFound = true;
					}
				}
				if (!illegalCharFound){
					return true; //Valid
				}
			}
		}
		return false;	
	}
	
	//createService helper function
	//Checks if the date is valid
	private boolean isValidDate(String date) {
		if (date.length() == 8) {
			//Abstract year month and day from the date string
			int year = Integer.parseInt(date.substring(0,4));
			int month = Integer.parseInt(date.substring(4,6));
			int day = Integer.parseInt(date.substring(6));
			//If in correct year range
			if (year >= 1980 && year <= 2999) {
				//If in correct month range
				if (month >= 1 && month <= 12) {
					//If in correct day range
					if ((day >= 1 && day <= 31) && !(month == 2 && day > 28)) {
						return true; //Valid
					}
				}
			}
		}
		return false;
	}

	public String deleteService(String role, int serviceNumber, String serviceName) {
		if(role.equals("planner") && validServicesList.contains(new Integer(serviceNumber))) {
			validServicesList.remove(new Integer(serviceNumber));
			return "DEL " + serviceNumber + " 0 00000 " + serviceName + " 0" + "\n";
		}else {
			return "error";
			
		}
		
	}
	
	//method to check sell tickets
	public String sellTicket(int serviceNumber, int numTickets) {
		//if it satisfy the service number and number of tickets
		if(validServicesList.contains(serviceNumber) && checkNumTickets(numTickets)) {
			return "SEL "+ serviceNumber + " " + numTickets + " 00000 **** 0" + "\n";
			
		} else {
			return "error";
		}
		
	}
	
	//cancelTickets method
	public String cancelTicket(String role, int serviceNumber, int numTickets) {
		//check if service number and number of tickets are valid
		if(validServicesList.contains(serviceNumber) && checkNumTickets(numTickets)) {
			if(cancelTicketConstraint(numTickets, role, Integer.toString(serviceNumber))) {//if constraints are met then return TSF
				return "CAN "+ serviceNumber + " " + numTickets + " 00000 " + "**** 0" + "\n";
			}                                                          //error
		}
			
		return "error";
		
	}	
	
	//helper method to check if ticket number is valid for cancelTickets
	private boolean cancelTicketConstraint(int numTickets, String role, String serviceNumber) {
		
		//if role is agent bloc
		if(role.equals("agent")) {
			
			//check if serviceNum or ticketNum is not valid
			if(numTickets > 10 || (numTickets +  totalTicketsCancelled) > 20) {
				return false;
			}
			
			boolean exists = false;
			for(String key :  CancelTicketCounter.keySet()) {
				if(key.equals(serviceNumber)) {
					exists = true;
					break;
				}
			}
			if (exists) {
				if(numTickets +  CancelTicketCounter.get(serviceNumber) <= 10) {
					CancelTicketCounter.put(serviceNumber,  CancelTicketCounter.get(serviceNumber) + numTickets);
					 totalTicketsCancelled =  totalTicketsCancelled + numTickets;
					return true;
				} else {
					return false;
				}
			} else {
				CancelTicketCounter.put(serviceNumber, numTickets);
				totalTicketsCancelled =  totalTicketsCancelled + numTickets;
				return true;
			}
			
			
		//if role is planner bloc	
		} else if (role.equals("planner")) {
			return true;
		} else {
			return false;
		}
	}
	

	//helper method to check if ticket number is valid
	private boolean checkNumTickets(int numTickets) {
		if(numTickets < 1) {
			return false;
		} else if (numTickets > 1000) {
			return false;
		} else {
			return true;
		}
	}
	
	//Returns true if the String is an int
	//Prevents crash when a non-numeric String is attempted to be parsed into int
	private boolean isActuallyANum(String num) {
	try {
		Integer.parseInt(num);
		return true;
	}
	catch (NumberFormatException e) {
		return false;
	}
}

}