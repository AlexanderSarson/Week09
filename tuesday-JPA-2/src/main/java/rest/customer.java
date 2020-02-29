/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import dbfacade.CustomerFacade;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author root
 */
@Path("customer")
public class customer {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    CustomerFacade facade = new CustomerFacade(emf);
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of customer
     */
    public customer() {
    }

    /**
     * Retrieves representation of an instance of rest.customer
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of customer
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String getAllCustomers(){
        return new Gson().toJson(facade.getAllCustomers());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("random")
    public String getRandomCustomer(){
        Random rand = new Random();
        int number = rand.nextInt(4);
        return new Gson().toJson(facade.getCustomer(number));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public String getCustomerById(@PathParam("id") int id){
        return new Gson().toJson(facade.getCustomer(id));
    }
}
