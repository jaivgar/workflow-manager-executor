package eu.arrowhead.client.provider;


public class OperationDTO{
	
	
	//=================================================================================================
	// members
	
	private int operationID;		// To differentiate between two operation with the same machine & config, I need an Operation unique ID
	private String serviceName;		// Service that will perform the operation
	private String configurationID;	// Configuration for the machine
	private boolean status;			// State of the operation (true means is has been done, false that is still incomplete)
	private int error;				// Possible errors during the execution of the operation, now following the status codes defined by HTTP.
							// For example: 200 -> OK, 204 -> No content

	//=================================================================================================
	// methods
	
	public OperationDTO() {}
	
	
	public OperationDTO(int operID, String servName, String configID, boolean state, int errorCode) {
		this.operationID = operID;
		this.serviceName = servName;
		this.configurationID = configID;
		this.status = state;
		this.error = errorCode;
	}
	
	//=================================================================================================
	
	public int getOperationID() {return operationID;}
	public String getServiceName() {return serviceName;}
	public String getConfigurationID() {return configurationID;}
	public boolean getStatus() {return status;}
	public int getError() {return error;}
	
	public void setOperationID(int OperationID) { this.operationID = OperationID;}
	public void setServiceName(String ServiceName) {this.serviceName = ServiceName;}
	public void setConfigurationID(String ConfigurationID) {this.configurationID = ConfigurationID;}
	public void setStatus(boolean Status) {this.status = Status;}
	public void setError(int Error) {this.error = Error;}
	
	
}


