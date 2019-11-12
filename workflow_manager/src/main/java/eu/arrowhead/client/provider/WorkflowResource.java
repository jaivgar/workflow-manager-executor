package eu.arrowhead.client.provider;


import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.misc.TypeSafeProperties;
import eu.arrowhead.client.common.model.Car;
import eu.arrowhead.client.common.model.StateMachine;



@Path("/") //base path after the port
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkflowResource {
	
	static final String SERVICE_URI = "workflow";
	protected TypeSafeProperties props = Utility.getProp();
	
	// Create thread to execute workflow logic
	private static final Executor executor = Executors.newSingleThreadExecutor();
	
	// Create list to hold the productino orders for all the products arriving to the Workstation
	public static List<ProductRecipeDTO> products = new ArrayList<ProductRecipeDTO>();
	
	@GET
	@Path(SERVICE_URI)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Workflow Manager got it!";
	}
	
	@GET
	@Path(SERVICE_URI + "/StateMachine")
	public Response getStateMachine() {
		
		if (WorkflowManager.productArrived) {
			// Before state machine is sent we will try just by transferring the 
			// name of the file necessary to execute the operation and the name
			// of the machine( service machine) that has to execute it
			String filename = props.getProperty("filename","test.xml");
			
			// This data would come from the production order but now is hard coded
			String machine = "Calibration";
			StateMachine test_machine = new StateMachine(filename, machine);
			return Response.status(Status.OK).entity(test_machine).build();
		}
		else {
			System.out.println("The product did not arrived yet to the workstation so there is not State Machine");
			return Response.status(Status.NO_CONTENT).build();
		}
		
	}
	
	
	@POST
	@Path(SERVICE_URI + "/ProductRecipe")
	public Response receiveProductOrder( ProductRecipeDTO input) {
		
		if ((input.getProductID() != null && !input.getProductID().trim().isEmpty()) && input.getSeqOperations().length > 0) {
			
			products.add(input);
			WorkflowManager.productArrived = true;
			System.out.println("New production recipe with ID "+ input.getProductID() +" received!");
			
			executor.execute(new WorkflowCreator(0));
			
			/*executor.execute(() -> {
				Files.readAllBytes(new Path("ashdgajdhsg"));
			});
			*/
			return Response.status(Status.OK).build();
		}
		
		else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}
	
	
	
	//For future demos as we do not have the equipment to POST
	@POST
	@Path(SERVICE_URI + "/Product")
	public Response postProductId() {
		return Response.status(Status.OK).build();
	}
	
	@PUT
	@Path(SERVICE_URI + "/OperationResult")
	public Response storeOperationResult(OperationDTO feedback) {
		
		// We assume always work with the first Production Recipe that arrived (products[0])
		for (int i = 0; i <  products.get(0).getSeqOperations().length; i++){
			
			OperationDTO currentOperation = products.get(0).getSeqOperations()[i];
			
			if (currentOperation.getOperationID() == feedback.getOperationID()) {
				
				currentOperation.setStatus(feedback.getStatus());
				currentOperation.setError(feedback.getError());
				
				// When all the operations in the recipe are done	
				if (i == products.get(0).getSeqOperations().length) {
					executor.execute(new WorkflowCreator(2));
				}
				
				executor.execute(new WorkflowCreator(1));
				
				return Response.status(Status.OK).build();
			}
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}
	
}
