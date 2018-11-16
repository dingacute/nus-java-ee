package workshop03.business;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import workshop03.model.Customer;
import workshop03.model.DiscountCode;

@Stateless
public class CustomerBean {
    
    @PersistenceContext private EntityManager em;
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    //this returning object could be Customer or Null, force others to check
    //before use it
    public Optional<Customer> findByCustomerId(Integer custId) {
        Customer c = em.find(Customer.class, custId);
        return (Optional.ofNullable(c));
    }
    
    public void addNewCustomer(Customer customer) 
            throws CustomerException{
        System.out.println(">>> discount code" + customer.getDiscountCode().getDicountCode());
        DiscountCode discountCode = em.find(DiscountCode.class, 
                customer.getDiscountCode().getDicountCode());
        if (null == discountCode) {
            throw new CustomerException("Discount code not found");
        }
        
        //new
        customer.setDiscountCode(discountCode);
        
        em.persist(customer);
        //managed
        
    }
}
