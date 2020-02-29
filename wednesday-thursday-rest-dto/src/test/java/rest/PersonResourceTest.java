/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.Person;
import entities.dto.PersonDTO;
import exceptions.PersonNotFoundException;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

/**
 *
 * @author root
 */
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person r1, r2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r1 = new Person("test1", "lastname", "05050505");
        r2 = new Person("test2", "lastname", "05050505");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .when()
                .get("/person")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAllPersons() {
        given()
                .contentType(ContentType.JSON)
                .get("/person")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all", hasSize(2));
    }

    @Test
    public void testGetPersonByID() {
        int personID = r2.getId().intValue();
        given()
                .contentType(ContentType.JSON)
                .get("/person/{id}", personID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(personID));
    }
    
    @Test
    public void testGetPersonByIDNotFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/person/{id}", -1)
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("Person with provided Id not found"));
    }

    @Test
    public void testEditPerson() {
        r1.setLastName("lastNameEdited");
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO(r1))
                .when()
                .put("/person")
                .then()
                .body("lName", equalTo("lastNameEdited"));
    }
    
    @Test
    public void testEditPersonNotFound() {
        r1.setId(-1l);
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO(r1))
                .when()
                .put("/person")
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("Person with provided Id not found"));
    }

    @Test
    public void testAddPerson() {
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO("fNameTEST", "lNameTEST", "50505050"))
                .when()
                .post("/person")
                .then()
                .body("fName", equalTo("fNameTEST"))
                .body("lName", equalTo("lNameTEST"))
                .body("phone", equalTo("50505050"));
    }

    @Test
    public void testDeletePerson() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/person/{id}", r1.getId())
                .then()
                .body("fName", equalTo("test1"))
                .body("lName", equalTo("lastname"))
                .body("phone", equalTo("05050505"));
    }

    @Test
    public void testDeletePersonNotFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/person/{id}", -1)
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("Person with provided Id not found"));
    }
}
