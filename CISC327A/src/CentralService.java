//This is a custom type to store lines from CSF as objects

public class CentralService {
	public int serviceNum;
	public int serviceCapacity;
	public int numTickets;
	public String serviceName;
	public int serviceDate;
	
	public CentralService(int serviceNum, int serviceCapacity, int numTickets, String serviceName, int serviceDate) {
		this.serviceNum = serviceNum;
		this.serviceCapacity = serviceCapacity;
		this.numTickets = numTickets;
		this.serviceName = serviceName;
		this.serviceDate = serviceDate;
	}
}
