
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import entities.dto.PersonDTO;
import exceptions.PersonNotFoundException;
import facades.PersonFacade;
import utils.EMF_Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3306/person",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPersons() {
        return Response.status(Response.Status.OK)
                .entity(GSON.toJson(FACADE.getAllPersons()))
                .build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") int id) throws PersonNotFoundException{
        return Response.status(Response.Status.OK)
                .entity(GSON.toJson(FACADE.getPerson(id)))
                .build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPerson(String json) throws PersonNotFoundException {
        PersonDTO pDTO = FACADE.editPerson(GSON.fromJson(json, PersonDTO.class));
        return Response.status(Response.Status.OK)
                .entity(GSON.toJson(pDTO))
                .build();
        
    }
    
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") int id)throws PersonNotFoundException {
        return Response.status(Response.Status.OK)
                .entity(GSON.toJson(FACADE.deletePerson(id)))
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPerson(String json){
        PersonDTO pDTO = GSON.fromJson(json, PersonDTO.class);
        PersonDTO resultPersonDTO = FACADE.addPerson(pDTO.getfName(), pDTO.getlName(), pDTO.getPhone());
        return Response.status(Response.Status.OK)
                .entity(GSON.toJson(resultPersonDTO))
                .build();
    }
    
    /*
    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllMovies() {
        List<MovieDTO> movieList = new ArrayList<>();
        FACADE.getAllMovies().forEach((movie) -> movieList.add(new MovieDTO(movie)));     
        if(movieList.size() > 0){
            return Response
                    .status(Response.Status.OK)
                    .entity(GSON.toJson(movieList))
                    .type(MediaType.APPLICATION_JSON)
                    .build();                    
        } else {
            return Response
                    .status(Response.Status.OK)
                    .entity("{\"msg\":\"Movies not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
    
    @Path("/count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieCount(){
        return Response
                .status(Response.Status.OK)
                .entity("{\"count\": " + GSON.toJson(FACADE.getMovieCount()) + "}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
            
    @Path("/name/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieName(@PathParam("name") String name){
        List<Movie> listOfMoviesByName = FACADE.getMoviesByName(name);
        if(listOfMoviesByName.size() > 0){
            return Response
                    .status(Response.Status.OK)
                    .entity(GSON.toJson(listOfMoviesByName))
                    .type(MediaType.APPLICATION_JSON)
                    .build();                    
        } else {
            return Response
                    .status(Response.Status.OK)
                    .entity("{\"msg\":\"Movie not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByID(@PathParam("id") long id){
        Movie movie = FACADE.getMovieByID(id);
        if(movie != null){
            return Response
                    .status(Response.Status.OK)
                    .entity(GSON.toJson(movie))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response
                    .status(Response.Status.OK)
                    .entity("{\"msg\":\"Movie not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
 
*/
}
