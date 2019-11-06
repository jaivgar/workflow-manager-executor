package eu.arrowhead.client.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.ServerError;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.misc.TypeSafeProperties;


@Path("/") //base path after the port
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WheelAlingResource {
	
	private static TypeSafeProperties props = Utility.getProp();
	static final String SERVICE_URI = "wheelAlingment";
	
	@GET
	@Path(SERVICE_URI)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Wheel Alingment machine got it!";
	}
	
	@POST
	@Path(SERVICE_URI + "/calibration")
	public Response postWork(String filename) {
		
		System.out.println("Order arrived, proceed to download operation file");
		String dataManagerUrl = props.getProperty("dataman_url","http://localhost:9456/datamanager/historian");
		InputStream getStream = getOrder(dataManagerUrl+"/file/", filename);
		if (getStream == null) {
			System.out.println("Operation file could not be found");
			return Response.status(Status.EXPECTATION_FAILED).build();
		}
		//---TODO: Change path when using different files in app.conf
	    String path_download = props.getProperty("path_download", "/media/pi/10D3-4C2C/");
		
		boolean result = saveToDisk(getStream, path_download, filename);
		if (result == false) {
			System.out.println("Error storing file in filesystem");
		}
		return Response.status(Status.OK).build();
		
	}
	
	private InputStream getOrder(String providerUrl, String filename) {
		/*
	      Sending request to the provider, to the acquired URL. The method type and payload should be known beforehand.
	      If needed, compile the request payload here, before sending the request.
	      Supported method types at the moment: GET, POST, PUT, DELETE
	     */
		System.out.println("Sent request to databmanager at :"+ providerUrl);  
		Response getResponse = Utility.sendRequest(providerUrl+filename, "GET", null);
		if (getResponse.getStatus() == 404) {
			System.out.println("File not found in database");
			return null;
		}
		InputStream incomingFile = getResponse.readEntity(InputStream.class);
		  
		return incomingFile;
	}
	
	
	static boolean saveToDisk(InputStream uploadedFile, String path, String filename) {
		  
		try {
			File target = new File(path+filename);
			OutputStream out = new FileOutputStream(target);
			
			int read = 0;
	        byte[] bytes = new byte[1024];
	        
	        while ((read = uploadedFile.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        
	        uploadedFile.close();
	        
	        out.flush();
	        out.close();
	        
	        return true;
			
		}catch (FileNotFoundException exception){
	        exception.printStackTrace();
	        
	    }catch (IOException ioException){
	        ioException.printStackTrace();
	        
	    }catch(Exception e) {
	        e.printStackTrace();
	        
	    }
	  	
	  	return false;
	  }
	  
}
