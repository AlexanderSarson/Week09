package facades;

import entities.Customer;
import entities.ItemType;
import entities.Order;
import entities.OrderLine;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class FacadeCustomer {

    private static FacadeCustomer instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private FacadeCustomer() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static FacadeCustomer getFacadeCustomer(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FacadeCustomer();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Customer addCustomer(String name, String email){
        EntityManager em = getEntityManager();
        Customer cust = new Customer(name, email);
        try {
            em.getTransaction().begin();
            em.persist(cust);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return cust;
    }
    
    public Customer findCustomerById(long id){
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers(){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Customer> tq = em.createQuery("SELECT c FROM Customer c", Customer.class);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }
    
    public ItemType addItemType(String name, String description, double price){
        EntityManager em = getEntityManager();
        ItemType it = new ItemType(name, description, price);
        try {
            em.getTransaction().begin();
            em.persist(it);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return it;
    }
    
    public ItemType findItemTypeById(long id){
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemType.class, id);
        } finally {
            em.close();
        }
    }
    
    public Order createOrder(Customer customer){
        EntityManager em = getEntityManager();
        try {
            int nextOrderId = (int) em.createQuery("SELECT MAX(o.orderID) FROM Order o").getSingleResult() + 1;
            Order order = new Order(nextOrderId);
            order.setCustomer(customer);
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            return order;
        } finally {
            em.close();
        }
    }
    
    public Order createOrderLine(ItemType itemType, Order order, int quantity){
        EntityManager em = getEntityManager();
        try {
            OrderLine orderLine = new OrderLine(quantity);
            orderLine.setItemType(itemType);
            Order orderFind = em.find(Order.class, order.getId());
            em.getTransaction().begin();
            orderFind.addOrderLine(orderLine);
            em.getTransaction().commit();
            return orderFind;
        } finally {
            em.close();
        }
    }
    
    public List<Order> findOrdersFromCustomer(long customerId){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Order> tq = em.createQuery("SELECT o FROM Order o JOIN o.customer c WHERE c.id = :customerId", Order.class);
            tq.setParameter("customerId", customerId);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }
    
    public double findTotalPriceForOrder(int orderId){
        EntityManager em = getEntityManager();
        try {
            Query totalPrice = em.createQuery("SELECT SUM(it.price * ol.quantity) FROM Order o JOIN o.orderLines ol JOIN ol.itemType it WHERE o.orderID = :orderId");
            totalPrice.setParameter("orderId", orderId);
            return (double) totalPrice.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3306/customer",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        FacadeCustomer FACADE = FacadeCustomer.getFacadeCustomer(EMF);
        
        Customer cust = FACADE.addCustomer("test1", "email");
        
        ItemType it = FACADE.addItemType("børste", "børster ting", 5);
        ItemType it2 = FACADE.addItemType("Ny ting", "Ny ting", 10);
        Order order = FACADE.createOrder(cust);
        FACADE.createOrderLine(it, order, 1);
        FACADE.createOrderLine(it, order, 1);
        FACADE.createOrderLine(it2, order, 1);
        FACADE.createOrderLine(it2, order, 1);

        System.out.println(FACADE.findTotalPriceForOrder(2));
    }

}
