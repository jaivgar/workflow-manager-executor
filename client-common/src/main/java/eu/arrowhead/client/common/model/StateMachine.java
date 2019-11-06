package eu.arrowhead.client.common.model;


//Sample model class for mid demo
public class StateMachine {

	private String filename;
	private String machine;
	
	//JSON libraries will use the empty constructor to create a new object during deserialization, so it is important to have one
	public StateMachine() {
	}
	
	public StateMachine(String filename, String machine) {
	    this.filename = filename;
	    this.machine = machine;
	  }
	
	//Getter methods are used during serialization
	public String getfilename() {
		return filename;
    }
	
	//After creating the object, JSON libraries use the setter methods during deserialization
	public void setfilename(String filename) {
	  this.filename = filename;
	}
	
	public String getmachine() {
	  return machine;
	}
	
	public void setmachine(String machine) {
	  this.machine = machine;
	}
}
