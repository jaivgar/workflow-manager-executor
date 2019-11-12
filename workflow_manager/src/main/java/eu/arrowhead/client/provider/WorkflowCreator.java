package eu.arrowhead.client.provider;

import javax.ws.rs.core.Response;

import eu.arrowhead.client.common.Utility;

//Using lambda expressions Runnable r2 = () -> { function }
public class WorkflowCreator implements Runnable{

	  private int step;
	  
	  public WorkflowCreator( int currentstep) {
		  this.step = currentstep;
	  }
	  
	  @Override
	  public void run() {
		  // TODO Process and sent POST to Workflow Executor

		  switch(step) {
		  /* Execute when the receiveProductOrder() method is called, which happens when a new product arrives
		   * and we have to check if a recipe is already under execution
		   */
		  case 0:
			  // Check if there are other Product Recipes still waiting
			  int j = WorkflowResource.products.size();
			  if (j > 1) { // Then we are already executing another product recipe

				  System.out.println("The Workflow Manager has received the product number " + j );
				  System.out.println("Therefore the Production Recipe is added to the queue until previous operations are finished");
			  }
			  else { // If this is the only one we send the first operation to the Executor

				  OperationDTO first_operation = WorkflowResource.products.get(0).getSeqOperations()[0];
				  System.out.println("The Workflow Manager will send to the Executor the Operation with ID " + first_operation.getOperationID());

				  int result = WorkflowManager.sendOperation(first_operation);
				  if (result == 200) {
					  System.out.println("The Workflow Executor acknowledge and started execution");
				  }
				  else {
					  System.out.println("The Worklfow Executor could not perform the commands \n"
							  + "Result error is: " + result);

				  }

			  }
			  break;
			  
		  /* Execute when the storeOperationResult() is called, which happens when an Operation is finished
		   * and we should send the next one to the Workflow Executor
		   */
		  case 1:
			  
			  int i = 0;
			  OperationDTO currentOperation = WorkflowResource.products.get(0).getSeqOperations()[i];
			  while (currentOperation.getStatus() == true) {
				  i++;
				  currentOperation = WorkflowResource.products.get(0).getSeqOperations()[i];
			  }
			  int result = WorkflowManager.sendOperation(currentOperation);
			  if (result == 200) {
				  System.out.println("The Workflow Executor acknowledge and started execution of Operation " + currentOperation.getOperationID());
			  }
			  else {
				  System.out.println("The Worklfow Executor could not start the execution of the operation"
						  + "Result error is: " + result);

			  }
			  
			  break;
			  
		  /* Execute when the storeOperationResult() is called and is the last operation in the Recipe, then
		   * we need to send the results back to the product and start the execution of the next recipe
		   */
		  case 2:
			  // Send the finished recipe updated to the product
			  WorkflowManager.sendRecipe(WorkflowResource.products.get(0));
			  
			  // Remove it from the products list
			  WorkflowResource.products.remove(0);
			  
			  break;
		  }



	  }
}
