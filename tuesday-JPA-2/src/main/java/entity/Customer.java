/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author root
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date created;
    @ElementCollection
    @CollectionTable(
            name = "hobbies",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID")
    )
    @Column(name = "HOBBY")
    private List<String> hobbies = new ArrayList();
    
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "PHONE")
    @Column(name = "Description")
    private Map<String, String> phones = new HashMap<>();
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Address> address = new ArrayList<>();
    
    /*
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<Address> address = new ArrayList<>();
*/
//@OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)

    public Long getId() {
        return id;
    }

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.created = new Date();
    }
    public void addPhone(String phoneNo, String description) {
        phones.put(phoneNo, description);
    }

    public String getPhoneDescription(String phoneNo) {
        return phones.getOrDefault(phoneNo, "Phone Number doesn't exists");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreated() {
        return created;
    }

    public String getHobbies() {
        StringBuilder sb = new StringBuilder();
        hobbies.forEach(hobby -> {
            if (sb.length() == 0) {
                sb.append(hobby);
            } else {
                sb.append(", ")
                        .append(hobby);
            }
        });
        return sb.toString();
    }

    public void setHobbies(String s) {
        this.hobbies.add(s);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Address> getAddress() {
        return address;
    }
    
    public void addAddress(Address add){
        if(!add.getCustomers().contains(this)){
            add.addCustomer(this);
        }
        this.address.add(add);
    }
  
}
