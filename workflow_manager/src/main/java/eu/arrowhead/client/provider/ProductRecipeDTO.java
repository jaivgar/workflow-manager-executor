package eu.arrowhead.client.provider;


public class ProductRecipeDTO{
	
	
	//=================================================================================================
	// members
	
	private String productID;			// To return the results of the operations performed in the Workstation to the product, 
										// specific data is used as metadata in the search of the service
	private OperationDTO[] seqOperations; 	// List of operations to be done in the workstations

	//=================================================================================================
	// methods
	
	public ProductRecipeDTO() {}
	
	
	public ProductRecipeDTO( String ID, OperationDTO[] Operations) {
		this.productID = ID;
		this.seqOperations = Operations;		
	}
	
	//=================================================================================================
	
	public String getProductID() {return productID;}
	public OperationDTO[] getSeqOperations() {return seqOperations;}
	
	public void setProductID(String product) { this.productID = product;}
	public void setSeqOperations(OperationDTO[] operations) {this.seqOperations = operations;}
	
	
}

