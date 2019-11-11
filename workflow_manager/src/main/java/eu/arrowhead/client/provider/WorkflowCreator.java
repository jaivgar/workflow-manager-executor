package eu.arrowhead.client.provider;


//Using lambda expres Runnable r2 = () -> { function }
public class WorkflowCreator implements Runnable{

	  private ProductRecipeDTO product;
	  
	  public WorkflowCreator( ProductRecipeDTO in) {
		  this.product = in;
	  }
	  
	  @Override
	   public void run() {
		  // TODO Process and sent POST to Workflow Executor
		  
		  
		  
	  }
}
