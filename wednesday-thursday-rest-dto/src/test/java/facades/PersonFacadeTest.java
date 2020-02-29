package facades;

import entities.Person;
import entities.dto.PersonDTO;
import exceptions.PersonNotFoundException;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person r1, r2;

    public PersonFacadeTest() {
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = PersonFacade.getPersonFacade(emf);
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r1 = new Person("test1", "lastName", "0505050");
        r2 = new Person("test2", "lastName", "0505050");
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
    public void testAddPerson() {
        String expectedPhoneNumber = "03030303";
        PersonDTO pDTO = facade.addPerson("fName", "lName", expectedPhoneNumber);
        String resultPhoneNumber = pDTO.getPhone();
        assertEquals(expectedPhoneNumber, resultPhoneNumber);
    }
    
    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        Long expectedDeletedId = r1.getId();
        long resultDeletedId = facade.deletePerson(expectedDeletedId.intValue()).getId();
        assertEquals(expectedDeletedId.longValue(), resultDeletedId);
    }

    @Test
    public void testDeletedPersonNotFound() {
        assertThrows(PersonNotFoundException.class, () -> {facade.deletePerson(-1);});
    }

    @Test
    public void testGetPerson() throws PersonNotFoundException {
        Long expectedId = r1.getId();
        long resultId = facade.getPerson(expectedId.intValue()).getId();
        assertEquals(expectedId.longValue(), resultId);
    }
    
    @Test
    public void testGetPersonNotFound() {
        assertThrows(PersonNotFoundException.class, () -> {facade.getPerson(-1);});
    }
    
    @Test
    public void testGetAllPerson() {
        int expectedSize = 2;
        int resultSize = facade.getAllPersons().getAll().size();
        assertEquals(expectedSize, resultSize);
    }
    
    @Test
    public void testEditPerson() throws PersonNotFoundException {
        PersonDTO pDTO = new PersonDTO(r1);
        String expectedLastName = "this is a test";
        pDTO.setlName(expectedLastName);
        String resultLastName = facade.editPerson(pDTO).getlName();
        assertEquals(expectedLastName, resultLastName);
    }
    
    @Test
    public void testEditPersonNotFound() {
        PersonDTO pDTO = new PersonDTO(r1);
        pDTO.setId(-1l);
        assertThrows(PersonNotFoundException.class, () -> {facade.editPerson(pDTO);});
    }
}
