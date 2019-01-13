//This is a custom type to store lines from TSF as objects

public class Transaction {
	public String type;
	public int serviceNum;
	public int numTickets;
	public int destServiceNum;
	public String serviceName;
	public int serviceDate;
	
	public Transaction(String type, int serviceNum, int numTickets, int destServiceNum, String serviceName, int serviceDate) {
		this.type = type;
		this.serviceNum = serviceNum;
		this.numTickets = numTickets;
		this.destServiceNum = destServiceNum;
		this.serviceName = serviceName;
		this.serviceDate = serviceDate;
	}
	
}
