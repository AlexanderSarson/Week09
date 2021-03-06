/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.PersonNotFoundException;

/**
 *
 * @author root
 */
public interface IPersonFacade {

    public PersonDTO addPerson(String fName, String lName, String phone);

    public PersonDTO deletePerson(int id) throws PersonNotFoundException;

    public PersonDTO getPerson(int id) throws PersonNotFoundException;

    public PersonsDTO getAllPersons();

    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException;

}
