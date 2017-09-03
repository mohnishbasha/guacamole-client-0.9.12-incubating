package com.glitterlabs.custom;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("custom")
@Produces(MediaType.APPLICATION_JSON)
public class CustomAction {

	
	public CustomAction() {
		for (int i = 0; i < 100; i++) {
			System.out.println("hello");
		}
	}
	
    @Path("cust")    
    public String getData() {
    	
    	System.out.println("hi here");
    	return "hii";
    }
}
