/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author root
 */
public class Tester {
/*
    public static void addAddress(Customer cust) {
        Address add1 = new Address("teststreet", "testcity");
        Address add2 = new Address("teststreet", "testcity");
        Address add3 = new Address("teststreet", "testcity");
        Address add4 =  new Address("teststreet", "testcity");
        cust.addAddress(add1);
        cust.addAddress(add2);
        cust.addAddress(add3);
        cust.addAddress(add4);
    }
*/
    
    
    public static void main(String[] args) {
        //Persistence.generateSchema("pu", null);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        String hobby = "myhobby";
        Customer cust01 = new Customer("test1", "test1");
        Customer cust02 = new Customer("test2", "test2");
        Customer cust03 = new Customer("test3", "test3");
        Customer cust04 = new Customer("test4", "test4");
        Customer cust05 = new Customer("test5", "test5");

        cust01.addPhone("1No", "my phone number1");
        cust01.addPhone("11No", "my phone number1");
        cust02.addPhone("2No", "my phone number2");
        cust02.addPhone("22No", "my phone number2");
        cust03.addPhone("3No", "my phone number3");
        cust03.addPhone("33No", "my phone number3");
        cust04.addPhone("4No", "my phone number4");
        cust04.addPhone("44No", "my phone number4");
        cust05.addPhone("5No", "my phone number5");
        cust05.addPhone("55No", "my phone number5");
        cust01.setHobbies(hobby);
        cust01.setHobbies(hobby);
        cust02.setHobbies(hobby);
        cust02.setHobbies(hobby);
        cust03.setHobbies(hobby);
        cust03.setHobbies(hobby);
        cust04.setHobbies(hobby);
        cust04.setHobbies(hobby);
        cust05.setHobbies(hobby);
        cust05.setHobbies(hobby);
 //       addAddress(cust01);
        try {
            em.getTransaction().begin();
            em.persist(cust01);
            em.persist(cust02);
            em.persist(cust03);
            em.persist(cust04);
            em.persist(cust05);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

}
