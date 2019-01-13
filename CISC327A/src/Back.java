//The main class for the back-end of the program, containing methods to handle the input it receives from the TSF and CSF files.

import java.util.ArrayList;
import java.io.FileNotFoundException;

public class Back {
	//Global variable to store CSF
	ArrayList<CentralService> CSF;
	//Global variable to store TSF
	ArrayList<Transaction> transactionList;
	
	public Back() throws FileNotFoundException {
		 transactionList = BackIO.makeTSFObjectArray(BackIO.readTSF());
		 CSF = BackIO.makeCSFObjectArray(BackIO.readCSF());
		 routeOutput(transactionList);
	}
	
	//Main method exists temporarily so that Back can be run separately from Front
	public static void main(String[] args) throws FileNotFoundException {
		new Back();
		
	}
	
	//The main function that parses TSF input and routes it to handler functions
	public void routeOutput(ArrayList<Transaction> transactionList) {
		int transactionListLength = transactionList.size();
		Transaction current;
		
		//Parse through arraylist of transactions from TSF file, and send each to a corresponding handler
		for (int i = 0; i < transactionListLength; i++) {
			current = transactionList.get(i);
			
			if (current.type.equals("CRE")) {
				handleCreateService(current);
			}
			else if (current.type.equals("DEL")) {
				handleDeleteService(current);
			}
			else if (current.type.equals("SEL")) {
				handleSellTicket(current);
			}
			else if (current.type.equals("CAN")) {
				handleCancelTicket(current);
			}
			else if (current.type.equals("CHG")) {
				handleChangeTicket(current);
			}
		}
		
		//When done, overwrite the old CSF file and Valid Services File
		try {
			BackIO.writeCSF(CSF);
			BackIO.writeValidServiceFile(CSF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Handler for createservice
	public void handleCreateService(Transaction transaction) {
		int index = checkServiceNumber(transaction.serviceNum); //Need to check capacity as well
		//if the service number doesn't exist in CSF file then return -1
		//if index is not -1 then create service is not valid
		if(index == -1) { // if createservice is valid then create new CSF object
			CSF.add(new CentralService(transaction.serviceNum, 30, 0, transaction.serviceName, transaction.serviceDate));
		}
		
	}
	
	//Handler for deleteservice
	public void handleDeleteService(Transaction transaction) {
		int serviceLocation = checkServiceNumber(transaction.serviceNum);
		
		//Remove service from list if it exists currently and has no tickets sold
		if (serviceLocation > -1) {
			if (transaction.numTickets == 0) {
				CSF.remove(serviceLocation);
			}
		}
	}
	
	//Handler for sellticket
	public void handleSellTicket(Transaction transaction) {
		
		// Index to the transaction's service in the CSF array
		int index = checkServiceNumber(transaction.serviceNum);
		
		// The transaction's service
		CentralService service = CSF.get(index);
		
		// The capacity of the service
		int capacity = service.serviceCapacity;
		
		
		if (!(transaction.numTickets <= 0) && !(transaction.numTickets > capacity) && capacityConstraint(capacity)) {
			service.numTickets += transaction.numTickets;
		}
		
	}
	
	//Handler for cancelticket
	public void handleCancelTicket(Transaction transaction) {
		// Index to the transaction's service in the CSF array
		int index = checkServiceNumber(transaction.serviceNum);
		CentralService service;
		
		// The transaction's service
		if (index != -1) {
			service = CSF.get(index);
			
			if (service.numTickets - transaction.numTickets >= 0) {
				service.numTickets -= transaction.numTickets;
			}
		}
	}
	
	//Handler for changeticket
	public void handleChangeTicket(Transaction transaction) {
		int source = transaction.serviceNum;			//source service number
		int destination = transaction.destServiceNum;
		
		int indexSource = checkServiceNumber(source);	//index of where source servicenumber is located in CSF array
		int indexDestination = checkServiceNumber(destination);
		
		if(indexSource != -1 && indexDestination != -1) {
			CSF.get(indexSource).numTickets -= transaction.numTickets; //remove number of ticket 
			CSF.get(indexDestination).numTickets += transaction.numTickets; 
		}
	}
		
	//A helper function for the capacity constraint
	public boolean capacityConstraint(int capacity) {
		if (!(capacity <= 0) && !(capacity > 1000)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int checkServiceNumber(int serviceNumber) { //returns index of location where service number is the same
													   //if index doesn't exist then return -1
		for(int i=0;i<CSF.size();i++) {
			if(CSF.get(i).serviceNum == serviceNumber) {
				return i;
			}
		}
		return -1;
	}
}
