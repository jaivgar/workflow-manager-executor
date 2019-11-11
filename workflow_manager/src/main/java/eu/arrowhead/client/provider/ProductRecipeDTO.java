package eu.arrowhead.client.provider;


public class ProductRecipeDTO{
	
	
	//=================================================================================================
	// members
	
	private String productID;			// To return the results of the operations performed in the Workstation to the product, 
										// specific data is used as metadata in the search of the service
	private Operation[] seqOperations; 	// List of operations to be done in the workstations

	//=================================================================================================
	// methods
	
	public ProductRecipeDTO() {}
	
	
	public ProductRecipeDTO( String ID, Operation[] Operations) {
		this.productID = ID;
		this.seqOperations = Operations;		
	}
	
	//=================================================================================================
	
	public String getProductID() {return productID;}
	public Operation[] getSeqOperations() {return seqOperations;}
	
	public void setProductID(String product) { this.productID = product;}
	public void setSeqOperations(Operation[] operations) {this.seqOperations = operations;}
	
	
}

class Operation{
	int operationID;		// To differentiate between two operation with the same machine & config, I need an Operation unique ID
	String serviceName;		// Service that will perform the operation
	String configurationID;	// Configuration for the machine
	boolean status;			// State of the operation (true means is has been done, false that is still incomplete)
	int error;				// Possible errors during the execution of the operation, now following the status codes defined by HTTP.
							// For example: 200 -> OK, 204 -> No content
}
